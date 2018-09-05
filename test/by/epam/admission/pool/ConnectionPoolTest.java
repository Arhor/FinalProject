/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class ConnectionPoolTest {

    private static final Logger LOG = LogManager.getLogger(ConnectionPoolTest.class);

    @Test
    public void mainTest() {
        for (int i = 0; i < 10000; i++) {
            new Thread() {
                public void run() {
                    ProxyConnection connection = ConnectionPool.POOL.getConnection();
                    try {
                        Thread.sleep((int)(Math.random() * 10));
                        ConnectionPool.POOL.releaseConnection(connection);
                    } catch (InterruptedException e) {
                        LOG.error("Interrupted exception: ", e);
                        Thread.currentThread().interrupt();
                    }
                }
            }.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.error("Interrupted exception: ", e);
            Thread.currentThread().interrupt();
        }
    }

    @AfterClass
    public void tearDown() {
        ConnectionPool.POOL.closeConnections();
    }
}
