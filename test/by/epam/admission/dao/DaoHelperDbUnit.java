/*
 * class: DaoHelperDbUnit
 */

package by.epam.admission.dao;

import by.epam.admission.exception.ProjectException;
import by.epam.admission.pool.ConnectionPoolDBUnit;
import by.epam.admission.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Burishinets Maxim
 * @version 1.0 19 Aug 2018
 */
public class DaoHelperDbUnit {

    private static final Logger LOG =
            LogManager.getLogger(DaoHelperDbUnit.class);

    private ProxyConnection connection;
    private ArrayList<AbstractDao> currentDAOs;

    public DaoHelperDbUnit() {
        currentDAOs = new ArrayList<>();
    }

    public void startTransaction(AbstractDao dao, AbstractDao...daos)
            throws ProjectException {
        if (connection == null) {
            connection = ConnectionPoolDBUnit.POOL.getConnection();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ProjectException("Transaction error", e);
        }
        dao.setConnection(connection);
        currentDAOs.add(dao);
        LOG.debug(dao.getClass().getSimpleName() +
                " - received test connection");
        for (AbstractDao concreteDao : daos) {
            concreteDao.setConnection(connection);
            currentDAOs.add(concreteDao);
            LOG.debug(dao.getClass().getSimpleName() +
                    " - received test connection");
        }
    }

    public void endTransaction() {
        /*
         * IF statement prevents adding the same connection
         * to connection pool more then once
         */
        for (AbstractDao dao : currentDAOs) {
            dao.setConnection(null);
            LOG.debug(dao.getClass().getSimpleName() +
                    " - lost test connection");
        }
        currentDAOs.clear();
        if (connection != null) {
            ConnectionPoolDBUnit.POOL.releaseConnection(connection);
            connection = null;
        }
    }

    public void commit() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                LOG.error("SQL exception during commit", e);
            }
        }
    }

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