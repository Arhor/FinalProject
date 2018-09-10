/*
 * class: AbstractDao
 */

package by.epam.admission.dao;

import java.util.List;

import by.epam.admission.exception.DaoException;
import by.epam.admission.exception.NotSupportedOperationException;
import by.epam.admission.model.Entity;
import by.epam.admission.pool.ProxyConnection;

/**
 * @author Maxim Burishinets
 * @version 1.0 20 Aug 2018
 */
public abstract class AbstractDao<K, T extends Entity> {

    protected ProxyConnection connection;

    public abstract List<T> findAll();
    
    public abstract T findEntityById(K id);
    
    public abstract boolean delete(K id) throws NotSupportedOperationException, DaoException;
    
    public abstract boolean delete(T entity) throws NotSupportedOperationException, DaoException;
    
    public abstract boolean create(T entity)
            throws NotSupportedOperationException, DaoException;
    
    public abstract T update(T entity) throws NotSupportedOperationException, DaoException;

    void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }
}
