/*
 * class: ConnectionPoolTest
 */

package by.epam.admission.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
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
    public void mainTest() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    ProxyConnection connection = pool.getConnection();
                    try {
                        Thread.sleep((int)(Math.random() * 10));
                        pool.releaseConnection(connection);
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

//    @Test
//    public void serializationTest()
//            throws IOException, ClassNotFoundException, InterruptedException {
//        ConnectionPool pool_1 = ConnectionPool.POOL;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(pool_1);
//        oos.close();
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        ObjectInputStream ois = new ObjectInputStream(bais);
//
//        ConnectionPool pool_2 = (ConnectionPool) ois.readObject();
//        ois.close();
//        Thread.sleep(10000);
//        Assert.assertTrue(pool_1 == pool_2);
//    }


    @BeforeClass
    public void setUpClass() {
        IDatabaseTester tester = pool.getTester();
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
    }

    @AfterClass
    public void tearDown() {
        pool.closePool();
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/test-dataset_temp.xml"));
        pool.getTester().setDataSet(dataSet);
        pool.getTester().onSetup();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        pool.getTester().onTearDown();
    }
}
