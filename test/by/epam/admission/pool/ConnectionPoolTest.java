/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;

/**
 * @author Maxim Burishinets
 * @version 1.0 30 Aug 2018
 */
public class ConnectionPoolTest {

    private static final Logger LOG = LogManager.getLogger(
            ConnectionPoolTest.class);

    private static ConnectionPoolDBUnit pool = ConnectionPoolDBUnit.POOL;

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

    @BeforeClass
    public void setUpClass() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(
                new File("resources/test-dataset_temp.xml"));
        pool.getTester().setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        pool.getTester().setTearDownOperation(DatabaseOperation.NONE);
        pool.getTester().setDataSet(dataSet);
        pool.getTester().onSetup();
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        pool.getTester().onTearDown();
    }
}
