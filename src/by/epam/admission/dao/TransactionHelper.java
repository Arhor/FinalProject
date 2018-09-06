package by.epam.admission.dao;

import by.epam.admission.pool.ConnectionPool;
import by.epam.admission.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionHelper {

    private static final Logger LOG = LogManager.getLogger(TransactionHelper.class);

    private ProxyConnection connection;

    private ArrayList<AbstractDAO> currentDAOs;

    public TransactionHelper() {
        currentDAOs = new ArrayList<>();
    }

    public void startTransaction(AbstractDAO dao, AbstractDAO...daos) {
        if (connection == null) {
            connection = ConnectionPool.POOL.getConnection();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        dao.setConnection(connection);
        currentDAOs.add(dao);
        LOG.debug(dao.getClass().getSimpleName() + " - added to transaction list");
        for (AbstractDAO concreteDao : daos) {
            concreteDao.setConnection(connection);
            currentDAOs.add(concreteDao);
            LOG.debug(dao.getClass().getSimpleName() + " - added to transaction list");
        }
    }

    public void endTransaction() {
        /*
         * IF statement prevents adding the same connection
         * to connection pool more then once
         */
        for (AbstractDAO dao : currentDAOs) {
            dao.setConnection(null);
            LOG.debug(dao.getClass().getSimpleName() + " - lost connection");
        }
        currentDAOs.clear();
        if (connection != null) {
            ConnectionPool.POOL.releaseConnection(connection);
            connection = null;
        }
    }

    public void commit() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            }
        }
    }

    public void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            }
        }
    }
}