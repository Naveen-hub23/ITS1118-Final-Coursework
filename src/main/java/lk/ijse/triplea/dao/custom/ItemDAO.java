package lk.ijse.triplea.dao.custom;

import lk.ijse.triplea.dao.CrudDAO;
import lk.ijse.triplea.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<Item> {

        ArrayList<Item> getLowStockItems() throws SQLException, ClassNotFoundException;
        int getItemCount() throws SQLException, ClassNotFoundException;
        int getLowStockCount() throws SQLException, ClassNotFoundException;
        boolean updateQty(int itemId, int qty) throws SQLException, ClassNotFoundException;

}