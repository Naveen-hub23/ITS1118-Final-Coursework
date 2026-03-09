package lk.ijse.triplea.bo.custom;

import lk.ijse.triplea.bo.SuperBO;
import lk.ijse.triplea.dto.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {
    boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteItem(int id) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(int id) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;
}