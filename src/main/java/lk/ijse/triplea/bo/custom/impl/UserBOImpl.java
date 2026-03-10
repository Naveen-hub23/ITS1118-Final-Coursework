package lk.ijse.triplea.bo.custom.impl;

import lk.ijse.triplea.bo.custom.UserBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.UserDAO;
import lk.ijse.triplea.dto.UserDTO;
import lk.ijse.triplea.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;


public class UserBOImpl implements UserBO {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.add(new User(0, dto.getUsername(), dto.getPassword(), dto.getPhone(), dto.getRole(), dto.getStatus()));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.update(new User(0, dto.getUsername(), dto.getPassword(), dto.getPhone(), dto.getRole(), dto.getStatus()));
    }

    @Override
    public boolean deleteUser(String username) throws SQLException, ClassNotFoundException {
        return userDAO.deleteByUsername(username);
    }

    @Override
    public UserDTO searchUser(String username) throws SQLException, ClassNotFoundException {
        User u = userDAO.searchByUsername(username);
        return (u == null) ? null : new UserDTO(u.getId(), u.getUsername(), u.getPassword(), u.getPhone(), u.getRole(), u.getStatus());
    }

    @Override
    public ArrayList<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        ArrayList<User> all = userDAO.getAll();
        ArrayList<UserDTO> allDTO = new ArrayList<>();
        for (User u : all) {
            allDTO.add(new UserDTO(u.getId(), u.getUsername(), u.getPassword(), u.getPhone(), u.getRole(), u.getStatus()));
        }
        return allDTO;
    }

    @Override
    public UserDTO verifyLogin(String username, String password) throws SQLException, ClassNotFoundException {
        User u = userDAO.verifyLogin(username, password);
        return (u == null) ? null : new UserDTO(u.getId(), u.getUsername(), u.getPassword(), u.getPhone(), u.getRole(), u.getStatus());
    }

    @Override
    public UserDTO searchSecurityDetails(String username) throws SQLException, ClassNotFoundException {
        User u = userDAO.searchSecurityDetails(username);
        if (u == null) return null;
        UserDTO dto = new UserDTO();
        dto.setSecurityQuestion(u.getSecurityQuestion());
        dto.setSecurityAnswer(u.getSecurityAnswer());
        return dto;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        return userDAO.updatePassword(username, newPassword);
    }
}

