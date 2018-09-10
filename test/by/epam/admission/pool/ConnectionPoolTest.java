/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.JdbcDatabaseTester;
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

    private static final Logger LOG = LogManager.getLogger(ConnectionPoolTest.class);

    @Test
    public void mainTest() {
        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    ProxyConnection connection = ConnectionPoolDBUnit.POOL.getConnection();
                    try {
                        Thread.sleep((int)(Math.random() * 10));
                        ConnectionPoolDBUnit.POOL.releaseConnection(connection);
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
    public void serializationTest() throws IOException, ClassNotFoundException, InterruptedException {
        ConnectionPool pool_1 = ConnectionPool.POOL;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(pool_1);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        ConnectionPool pool_2 = (ConnectionPool) ois.readObject();
        ois.close();
        Thread.sleep(10000);
        Assert.assertTrue(pool_1 == pool_2);
    }


    @BeforeClass
    public void setUpClass() throws Exception {
        ConnectionPoolDBUnit.POOL.tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        ConnectionPoolDBUnit.POOL.tester.setTearDownOperation(DatabaseOperation.NONE);
    }

    @AfterClass
    public void tearDown() {
        ConnectionPoolDBUnit.POOL.closePool();
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/all-tables-dataset.xml"));
        ConnectionPoolDBUnit.POOL.tester.setDataSet(dataSet);
        ConnectionPoolDBUnit.POOL.tester.onSetup();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        ConnectionPoolDBUnit.POOL.tester.onTearDown();
    }
}
