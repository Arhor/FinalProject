/*
 * class: AbstractDAO
 */

package by.epam.admission.dao;

import java.sql.SQLException;
import java.util.List;

import by.epam.admission.exception.DAOException;
import by.epam.admission.exception.NotSupportedOperationException;
import by.epam.admission.model.Entity;
import by.epam.admission.pool.ProxyConnection;

/**
 * @author Maxim Burishinets
 * @version 1.0 20 Aug 2018
 */
public abstract class AbstractDAO<K, T extends Entity> {

    protected ProxyConnection connection;

    public abstract List<T> findAll();
    
    public abstract T findEntityById(K id);
    
    public abstract boolean delete(K id) throws NotSupportedOperationException, SQLException, DAOException;
    
    public abstract boolean delete(T entity) throws NotSupportedOperationException, DAOException;
    
    public abstract boolean create(T entity)
            throws NotSupportedOperationException, DAOException;
    
    public abstract T update(T entity) throws NotSupportedOperationException;

    void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }
}
