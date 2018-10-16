/*
 * class: AbstractDao
 */

package by.epam.admission.dao;

import by.epam.admission.exception.ProjectException;
import by.epam.admission.model.Entity;
import by.epam.admission.pool.ProxyConnection;

/**
 * Class AbstractDao represents abstract database access object for entire
 * application
 *
 * @author Maxim Burishinets
 * @version 1.0 20 Aug 2018
 * @see Entity
 * @param <K> Generic type that represents key for concrete method
 * @param <T> Generic type that represents concrete Entity subclass
 */
public abstract class AbstractDao<K, T extends Entity> {

    protected ProxyConnection connection;

    /**
     * Method checks `available` status of record in corresponding database
     * table by ID
     *
     * @param id concrete entity ID
     * @return 'true' if entity is available, otherwise 'false'
     * @throws ProjectException wrapped various types of SQL exception
     */
    public abstract boolean checkStatus(K id) throws ProjectException;

    /**
     * Method takes ID of concrete entity to find in database. Returns 'null'
     * if there is not corresponding record in the database
     *
     * @param id concrete entity ID
     * @return concrete entity object initialized according on corresponding
     *         database record
     * @throws ProjectException wrapped various types of SQL exception
     */
    public abstract T findEntityById(K id) throws ProjectException;

    /**
     * Method used to delete corresponding database record by its ID
     *
     * @param id concrete entity ID
     * @return 'true' if deletion affected more than 0 rows, otherwise 'false'
     * @throws ProjectException wrapped various types of SQL exception
     */
    public abstract boolean delete(K id) throws ProjectException;

    /**
     * @param entity concrete {@link Entity} object which stance represents
     *               a record to insert to database
     * @return 'true' if database record insertion was successful,
     *         otherwise 'false'
     * @throws ProjectException wrapped various types of SQL exception
     */
    public abstract boolean create(T entity) throws ProjectException;

    /**
     * @param entity concrete {@link Entity} object which stance represents
     *               a record to be updated in database
     * @return 'true' if deletion affected more than 0 rows, otherwise 'false'
     * @throws ProjectException wrapped various types of SQL exception
     */
    public abstract boolean update(T entity) throws ProjectException;

    /**
     * @param connection {@link ProxyConnection} object that will be used to
     *                   execute queries to database
     */
    void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }
}
