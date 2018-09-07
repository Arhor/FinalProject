package by.epam.admission.pool;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DaoTester {

    private static IDatabaseTester tester = null;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL =
            "jdbc:mysql://localhost:3306/admission_committee" +
                    "?verifyServerCertificate=false" +
                    "&useSSL=false" +
                    "&requireSSL=false" +
                    "&useLegacyDatetimeCode=false" +
                    "&amp&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "dragonlance";

    @BeforeClass
    public void setUpClass() throws Exception {
        tester = new JdbcDatabaseTester(DRIVER, URL, USER, PASSWORD);
        tester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        tester.setTearDownOperation(DatabaseOperation.NONE);
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new File("resources/all-tables-dataset.xml"));
        tester.setDataSet(dataSet);
        tester.onSetup();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        tester.onTearDown();
    }

    @Test
    public void testSelect() throws Exception {
        Connection connection = tester.getConnection().getConnection();

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM `subjects`");
            while (rs.next()) {
                System.out.println(rs.getString("id") + " " + rs.getString("name_ru"));
            }
        }

        connection.close();
    }

}
