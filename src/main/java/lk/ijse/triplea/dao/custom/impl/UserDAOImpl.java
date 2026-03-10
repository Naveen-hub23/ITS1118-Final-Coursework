package lk.ijse.triplea.dao.custom.impl;

import lk.ijse.triplea.dao.CRUDUtil;
import lk.ijse.triplea.dao.custom.UserDAO;
import lk.ijse.triplea.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserDAOImpl implements UserDAO {
    @Override
    public boolean add(User entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO user (username, password_hash, phone, role, status) VALUES (?, ?, ?, ?, ?)",
                entity.getUsername(), entity.getPassword(), entity.getPhone(), entity.getRole(), entity.getStatus());
    }

    @Override
    public boolean update(User entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE user SET password_hash=?, phone=?, role=?, status=? WHERE username=?",
                entity.getPassword(), entity.getPhone(), entity.getRole(), entity.getStatus(), entity.getUsername());
    }

    @Override
    public boolean deleteByUsername(String username) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM user WHERE username=?", username);
    }

    @Override
    public User searchByUsername(String username) throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM user WHERE username = ?", username);
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                    rs.getString("phone"), rs.getString("role"), rs.getString("status"));
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM user");
        ArrayList<User> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                    rs.getString("phone"), rs.getString("role"), rs.getString("status")));
        }
        return list;
    }

    @Override
    public User verifyLogin(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM user WHERE username = ? AND password_hash = ?", username, password);
        if (rs.next()) {
            return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                    rs.getString("phone"), rs.getString("role"), rs.getString("status"));
        }
        return null;
    }

    @Override
    public User searchSecurityDetails(String username) throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT security_question, security_answer FROM user WHERE username = ?", username);
        if (rs.next()) {
            User user = new User();
            user.setSecurityQuestion(rs.getString("security_question"));
            user.setSecurityAnswer(rs.getString("security_answer"));
            return user;
        }
        return null;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE user SET password_hash = ? WHERE username = ?", newPassword, username);
    }

    // Boilerplate for CrudDAO interface compatibility
    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public User search(Integer id) throws SQLException, ClassNotFoundException {
        return null;
    }
}

