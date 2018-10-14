/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class ConnectionPoolTest {

    private static final Logger LOG = LogManager.getLogger(
            ConnectionPoolTest.class);

    private static ConnectionPool pool = ConnectionPool.POOL;

    @Test
    public void connectionLeakTest() {
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                ProxyConnection connection = pool.getConnection();
                pool.releaseConnection(connection);
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOG.error("Interrupted exception: ", e);
                Thread.currentThread().interrupt();
            }
        }
        pool.closePool();
        int actual = pool.countAvailableConnections() + pool.countUsedConnections();
        int expected = 0;
        Assert.assertEquals(actual,expected);
    }
}
