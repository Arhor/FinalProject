/*
 * class: ConnectionPool
 */

package by.epam.admission.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;

import com.mysql.cj.jdbc.Driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implements thread-safety connection pool for SQL data base
 * 
 * @author Maxim Burishinets
 * @version 1.1 15 Aug 2018
 */
public enum ConnectionPool {
    
    POOL;
    
    public final int POOL_SIZE;
    
    private final Logger LOG = LogManager.getLogger(ConnectionPool.class);
    
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOLSIZE = "db.poolsize";
    
    private LinkedBlockingQueue<ProxyConnection> availableConnections;
    private LinkedBlockingQueue<ProxyConnection> usedConnections;

    ConnectionPool() {
        ResourceBundle prop = DatabaseManager.readProperties();
        availableConnections = new LinkedBlockingQueue<>();
        usedConnections = new LinkedBlockingQueue<>();
        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            LOG.debug("driver " + driver + " registered...");
        } catch (SQLException e) {
            LOG.fatal("Driver registration error", e);
            throw new RuntimeException();
        }
        POOL_SIZE = Integer.parseInt(prop.getString(DB_POOLSIZE));
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection;
            try {
                connection = DriverManager.getConnection(
                        prop.getString(DB_URL),
                        prop.getString(DB_USER),
                        prop.getString(DB_PASSWORD));
                LOG.debug("connection established...");
                availableConnections.put(new ProxyConnection(connection));
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            } catch (InterruptedException e) {
                LOG.error("Interrupted exception", e);
                Thread.currentThread().interrupt();
            }
        }
        if (countAvailableConnections() != POOL_SIZE) {
            LOG.fatal("Connection pool initiation error");
            throw new RuntimeException();
        }
    }

    public ProxyConnection getConnection() {
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = availableConnections.take();
            usedConnections.put(proxyConnection);
            LOG.debug(String.format("connection given to [%9s] : "
                    + "available - %d, used - %d",
                    Thread.currentThread().getName(),
                    availableConnections.size(),
                    usedConnections.size()));
        } catch (InterruptedException e) {
            LOG.error("Interrupted exception", e);
            Thread.currentThread().interrupt();
        }
        return proxyConnection;
    }

    public void releaseConnection(ProxyConnection proxyConnection) {
        try {
            usedConnections.remove(proxyConnection);
            if (!proxyConnection.getAutoCommit()) {
                proxyConnection.setAutoCommit(true);
            }
            availableConnections.put(proxyConnection);
            LOG.debug(String.format(
                    "connection released [%-9s] : " +
                    "available - %d, used - %d",
                    Thread.currentThread().getName(),
                    availableConnections.size(),
                    usedConnections.size()));
        } catch (SQLException e) {
            LOG.error("SQL exception", e);
        } catch (InterruptedException e) {
            LOG.error("Interrupted exception", e);
            Thread.currentThread().interrupt();
        }
    }

    public void closePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                availableConnections.take().closeConnection();
                LOG.debug("connection closed...left: "
                + (availableConnections.size()
                + usedConnections.size()));
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            } catch (InterruptedException e) {
                LOG.error("Interrupted exception", e);
                Thread.currentThread().interrupt();
            }
        }
        LOG.debug("all connections are closed...left: "
                + (availableConnections.size()
                + usedConnections.size()));
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                java.sql.Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
                LOG.debug("driver deregister" + driver);
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            }
        }
    }

    public int countAvailableConnections() {
        return availableConnections.size();
    }

    public int countUsedConnections() {
        return usedConnections.size();
    }
}
