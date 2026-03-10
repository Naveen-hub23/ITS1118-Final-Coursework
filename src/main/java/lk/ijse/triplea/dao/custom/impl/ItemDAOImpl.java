package lk.ijse.triplea.dao.custom.impl;

import lk.ijse.triplea.dao.CRUDUtil;
import lk.ijse.triplea.dao.custom.ItemDAO;
import lk.ijse.triplea.entity.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean add(Item entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO item (name, qty, unit_price) VALUES (?, ?, ?)",
                entity.getName(), entity.getQty(), entity.getUnitPrice());
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE item SET name=?, qty=?, unit_price=? WHERE id=?",
                entity.getName(), entity.getQty(), entity.getUnitPrice(), entity.getId());
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM item WHERE id=?", id);
    }

    @Override
    public Item search(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM item WHERE id=?", id);
        if (rs.next()) {
            return new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4));
        }
        return null;
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM item");
        ArrayList<Item> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
        }
        return list;
    }

    @Override
    public boolean updateQty(int itemId, int qty) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE item SET qty = qty - ? WHERE id = ?", qty, itemId);
    }

    @Override
    public ArrayList<Item> getLowStockItems() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM item WHERE qty <= 20");
        ArrayList<Item> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
        }
        return list;
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT COUNT(*) FROM item");
        return rs.next() ? rs.getInt(1) : 0;
    }

    @Override
    public int getLowStockCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT COUNT(*) FROM item WHERE qty <= 20");
        return rs.next() ? rs.getInt(1) : 0;
    }
}