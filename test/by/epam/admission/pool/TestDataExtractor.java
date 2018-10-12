package by.epam.admission.pool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.testng.annotations.Test;

@Test
public class TestDataExtractor {

    private static final Logger LOG = LogManager.getLogger(TestDataExtractor.class);

    public void TestExtract() {
        Connection jdbcConnection = ConnectionPool.POOL.getConnection();
        FileOutputStream fos = null;
        try {
            IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
            QueryDataSet partialDataSet = new QueryDataSet(connection);
            partialDataSet.addTable("users");
            partialDataSet.addTable("enrollees");
            partialDataSet.addTable("subjects");
            partialDataSet.addTable("faculties");
            partialDataSet.addTable("enrollees_has_subjects");
            partialDataSet.addTable("faculties_has_subjects");
            partialDataSet.addTable("admission_list");
            fos = new FileOutputStream("resources/test-dataset_temp.xml");
            FlatXmlDataSet.write(partialDataSet, fos);
            System.out.println("Dataset written successfully");
        } catch (DatabaseUnitException | IOException e) {
            LOG.error("Data extraction failed", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error("Output stream close error", e);
                }
            }
        }
    }
}