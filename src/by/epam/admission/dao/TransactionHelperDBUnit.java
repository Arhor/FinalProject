package by.epam.admission.dao;

import by.epam.admission.pool.ConnectionPoolDBUnit;
import by.epam.admission.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionHelperDBUnit {

    private static final Logger LOG = LogManager.getLogger(TransactionHelperDBUnit.class);

    private ProxyConnection connection;

    private ArrayList<AbstractDAO> currentDAOs;

    public TransactionHelperDBUnit() {
        currentDAOs = new ArrayList<>();
    }

    public void startTransaction(AbstractDAO dao, AbstractDAO...daos) {
        if (connection == null) {
            connection = ConnectionPoolDBUnit.POOL.getConnection();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        }
        dao.setConnection(connection);
        currentDAOs.add(dao);
        LOG.debug(dao.getClass().getSimpleName() + " - added to transaction");
        for (AbstractDAO concreteDao : daos) {
            concreteDao.setConnection(connection);
            currentDAOs.add(concreteDao);
            LOG.debug(dao.getClass().getSimpleName() + " - added to transaction");
        }
    }

    public void endTransaction() {
        /*
         * IF statement prevents adding the same connection
         * to connection pool more then once
         */
        for (AbstractDAO dao : currentDAOs) {
            dao.setConnection(null);
            LOG.debug(dao.getClass().getSimpleName() + " - lost test connection");
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