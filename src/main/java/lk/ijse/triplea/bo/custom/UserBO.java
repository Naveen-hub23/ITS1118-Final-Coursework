package lk.ijse.triplea.bo.custom;

import lk.ijse.triplea.bo.SuperBO;
import lk.ijse.triplea.dto.UserDTO;

import java.sql.SQLException;
import java.util.ArrayList;


public interface UserBO extends SuperBO {
    boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteUser(String username) throws SQLException, ClassNotFoundException;

    UserDTO searchUser(String username) throws SQLException, ClassNotFoundException;

    ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException;

    UserDTO verifyLogin(String username, String password) throws SQLException, ClassNotFoundException;

    UserDTO searchSecurityDetails(String username) throws SQLException, ClassNotFoundException;

    boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException;
}

