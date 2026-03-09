package lk.ijse.triplea.dao;

import java.sql.SQLException;
import java.util.ArrayList;


public interface CrudDAO<T> extends SuperDAO {
    boolean add(T entity) throws SQLException, ClassNotFoundException;

    boolean update(T entity) throws SQLException, ClassNotFoundException;

    boolean delete(Integer id) throws SQLException, ClassNotFoundException;

    T search(Integer id) throws SQLException, ClassNotFoundException;

    ArrayList<T> getAll() throws SQLException, ClassNotFoundException;
}

