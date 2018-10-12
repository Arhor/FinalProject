package by.epam.admission.pool;

import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConnection;
        import org.dbunit.database.IDatabaseConnection;
        import org.dbunit.database.QueryDataSet;
        import org.dbunit.dataset.DataSetException;
        import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.testng.annotations.Test;

@Test
public class TestDataExtractor {

    public void TestExtract() {
        Connection jdbcConnection = ConnectionPool.POOL.getConnection();
        IDatabaseConnection connection = null;
        try {
            connection = new DatabaseConnection(jdbcConnection);
        } catch (DatabaseUnitException e) {
            e.printStackTrace();
        }

        QueryDataSet partialDataSet = new QueryDataSet(connection);

        try {
            partialDataSet.addTable("users");
            partialDataSet.addTable("enrollees");
            partialDataSet.addTable("subjects");
            partialDataSet.addTable("faculties");
            partialDataSet.addTable("enrollees_has_subjects");
            partialDataSet.addTable("faculties_has_subjects");
            partialDataSet.addTable("admission_list");
        } catch (AmbiguousTableNameException e) {
            e.printStackTrace();
        }


        try {
            FlatXmlDataSet.write(partialDataSet, new FileOutputStream("resources/test-dataset_temp.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
        System.out.println("Dataset written");
    }
}