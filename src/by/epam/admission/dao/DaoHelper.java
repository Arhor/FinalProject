/*
 * class: DaoHelper
 */

package by.epam.admission.dao;

import by.epam.admission.exception.ProjectException;
import by.epam.admission.pool.ConnectionPool;
import by.epam.admission.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class DaoHelper provides various services to maintain transaction action for
 * one or more database tables via their DAOs
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class DaoHelper {

    private static final Logger LOG = LogManager.getLogger(DaoHelper.class);

    private ProxyConnection connection;
    private ArrayList<AbstractDao> currentDAOs;

    public DaoHelper() {
        currentDAOs = new ArrayList<>();
    }

    /**
     * Method takes passed DAO (optionally more than one), adds it to list of
     * DAO objects used in current transaction and set one {@link ProxyConnection}
     * to all of them
     *
     * @param dao concrete implementation of {@link AbstractDao}
     * @param daos optional {@link AbstractDao} objects
     * @throws ProjectException wrapped SQL exception
     */
    public void startTransaction(AbstractDao dao, AbstractDao...daos)
            throws ProjectException {
        if (connection == null) {
            connection = ConnectionPool.POOL.getConnection();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ProjectException("Transaction error", e);
        }
        dao.setConnection(connection);
        currentDAOs.add(dao);
        LOG.debug(dao.getClass().getSimpleName() +
                " - received connection");
        for (AbstractDao concreteDao : daos) {
            concreteDao.setConnection(connection);
            currentDAOs.add(concreteDao);
            LOG.debug(dao.getClass().getSimpleName() +
                    " - received connection");
        }
    }

    /**
     * Closing current transaction:
     *     1) sets connection of used DAOs to 'null'
     *     2) clears list of used DAOs
     *     3) releases current connection back to connection pool and sets it
     *        to 'null'
     */
    public void endTransaction() {
        /*
         * IF statement prevents adding the same connection
         * to connection pool more then once
         */
        for (AbstractDao dao : currentDAOs) {
            dao.setConnection(null);
            LOG.debug(dao.getClass().getSimpleName() + " - lost connection");
        }
        currentDAOs.clear();
        if (connection != null) {
            ConnectionPool.POOL.releaseConnection(connection);
            connection = null;
        }
    }

    /**
     * The method is used to commit the changes made
     */
    public void commit() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                LOG.error("SQL exception during commit", e);
            }
        }
    }

    /**
     * The method is used to rollback the changes made.
     */
    public void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOG.error("SQL exception during rollback", e);
            }
        }
    }
}