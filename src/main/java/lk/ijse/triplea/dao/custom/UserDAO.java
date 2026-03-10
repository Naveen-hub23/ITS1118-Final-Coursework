package lk.ijse.triplea.dao.custom;

import lk.ijse.triplea.dao.CrudDAO;
import lk.ijse.triplea.entity.User;

import java.sql.SQLException;


public interface UserDAO extends CrudDAO<User> {
    User verifyLogin(String username, String password) throws SQLException, ClassNotFoundException;

    User searchSecurityDetails(String username) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException;

    User searchByUsername(String username) throws SQLException, ClassNotFoundException;

    boolean deleteByUsername(String username) throws SQLException, ClassNotFoundException;
}

