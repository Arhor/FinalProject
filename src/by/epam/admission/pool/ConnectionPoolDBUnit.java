package by.epam.admission.pool;

import com.mysql.cj.jdbc.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;

public enum ConnectionPoolDBUnit {

    POOL;

    private final Logger LOG = LogManager.getLogger(ConnectionPoolDBUnit.class);

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/admission_committee" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dragonlance";

    private static final int DB_POOLSIZE = 10;

    private LinkedBlockingQueue<ProxyConnection> availableConnections;
    private LinkedBlockingQueue<ProxyConnection> usedConnections;

    private IDatabaseTester tester;

    ConnectionPoolDBUnit() {
        try {
            tester = new JdbcDatabaseTester(
                    DB_DRIVER,
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            LOG.fatal("JDBC driver not found");
            throw new RuntimeException();
        }
        availableConnections = new LinkedBlockingQueue<>();
        usedConnections = new LinkedBlockingQueue<>();
        for (int i = 0; i < DB_POOLSIZE; i++) {
            Connection connection;
            try {
                connection = tester.getConnection().getConnection();
                LOG.debug("test connection established...");
                availableConnections.put(new ProxyConnection(connection));
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            } catch (InterruptedException e) {
                LOG.error("Interrupted exception", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOG.error("Unknown exception", e);
            }
        }
        if (countAvailableConnections() != DB_POOLSIZE) {
            LOG.fatal("Connection pool initiation error");
            throw new RuntimeException();
        }
    }

    public ProxyConnection getConnection() {
        ProxyConnection proxyConnection = null;
        try {
            proxyConnection = availableConnections.take();
            usedConnections.put(proxyConnection);
            LOG.debug(String.format("test connection given to [%9s] : "
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
                    "test connection released [%-9s] : " +
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
        for (int i = 0; i < DB_POOLSIZE; i++) {
            try {
                availableConnections.take().closeConnection();
                LOG.debug("test connection closed...left: "
                        + (availableConnections.size()
                        + usedConnections.size()));
            } catch (SQLException e) {
                LOG.error("SQL exception", e);
            } catch (InterruptedException e) {
                LOG.error("Interrupted exception", e);
                Thread.currentThread().interrupt();
            }
        }
        LOG.debug("all test connections are closed...left: "
                + (availableConnections.size()
                + usedConnections.size()));
    }

    public int countAvailableConnections() {
        return availableConnections.size();
    }

    public int countUsedConnections() {
        return usedConnections.size();
    }

    public IDatabaseTester getTester() {
        return tester;
    }

}
