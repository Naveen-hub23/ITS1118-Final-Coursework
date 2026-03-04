package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.UserDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    public boolean saveUser(UserDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO user (username, password_hash, phone, role, status) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, dto.getUsername());
        pstm.setString(2, dto.getPassword());
        pstm.setString(3, dto.getPhone());
        pstm.setString(4, dto.getRole());
        pstm.setString(5, dto.getStatus());

        return pstm.executeUpdate() > 0;
    }

    public boolean updateUser(UserDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE user SET password_hash=?, phone=?, role=?, status=? WHERE username=?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, dto.getPassword());
        pstm.setString(2, dto.getPhone());
        pstm.setString(3, dto.getRole());
        pstm.setString(4, dto.getStatus());
        pstm.setString(5, dto.getUsername());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM user WHERE username=?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, username);
        return pstm.executeUpdate() > 0;
    }


    public List<UserDTO> getAllUsers() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<UserDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getString("status")
            ));
        }
        return list;
    }

    public UserDTO searchUser(String username) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, username);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getString("status")
            );
        }
        return null;
    }

    public static UserDTO verifyLogin(String username, String password) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user WHERE username = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, username);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            String dbPassword = rs.getString("password_hash");
            if (dbPassword.equals(password)) {
                return new UserDTO(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("status")
                );
            }
        }
        return null;
    }


    public UserDTO searchSecurityDetails(String username) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT security_question, security_answer FROM user WHERE username = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, username);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            UserDTO user = new UserDTO();
            user.setSecurityQuestion(rs.getString("security_question"));
            user.setSecurityAnswer(rs.getString("security_answer"));
            return user;
        }
        return null;
    }

    public boolean updatePassword(String username, String newPassword) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE user SET password_hash = ? WHERE username = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, newPassword);
        pstm.setString(2, username);

        return pstm.executeUpdate() > 0;
    }
}