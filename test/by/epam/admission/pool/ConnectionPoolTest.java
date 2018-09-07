/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class ConnectionPoolTest {

    private static final Logger LOG = LogManager.getLogger(ConnectionPoolTest.class);

    @Test
    public void mainTest() {
        for (int i = 0; i < 100; i++) {
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

    @Test
    public void serializationTest() throws IOException, ClassNotFoundException {
        ConnectionPool pool_1 = ConnectionPool.POOL;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(pool_1);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        ConnectionPool pool_2 = (ConnectionPool) ois.readObject();
        ois.close();
        Assert.assertTrue(pool_1 == pool_2);
    }

    @AfterClass
    public void tearDown() {
        ConnectionPool.POOL.closePool();
    }
}
