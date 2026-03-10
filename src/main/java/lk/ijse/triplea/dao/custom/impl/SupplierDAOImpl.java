package lk.ijse.triplea.dao.custom.impl;

import lk.ijse.triplea.dao.CRUDUtil;
import lk.ijse.triplea.dao.custom.SupplierDAO;
import lk.ijse.triplea.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class SupplierDAOImpl implements SupplierDAO {
    @Override
    public boolean add(Supplier entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO supplier (name, phone, email) VALUES (?, ?, ?)",
                entity.getName(), entity.getPhone(), entity.getEmail());
    }

    @Override
    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("UPDATE supplier SET name=?, phone=?, email=? WHERE supplier_id=?",
                entity.getName(), entity.getPhone(), entity.getEmail(), entity.getId());
    }

    @Override
    public boolean delete(Integer id) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("DELETE FROM supplier WHERE supplier_id=?", id);
    }

    @Override
    public Supplier search(Integer id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM supplier WHERE supplier_id=?", id);
        if (rs.next()) {
            return new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"));
        }
        return null;
    }

    @Override
    public ArrayList<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT * FROM supplier");
        ArrayList<Supplier> suppliers = new ArrayList<>();
        while (rs.next()) {
            suppliers.add(new Supplier(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("phone"), rs.getString("email")));
        }
        return suppliers;
    }

    @Override
    public int getSupplierCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CRUDUtil.execute("SELECT COUNT(*) FROM supplier");
        return rs.next() ? rs.getInt(1) : 0;
    }
}

