package lk.ijse.triplea.dao.custom.impl;

import lk.ijse.triplea.dao.CRUDUtil;
import lk.ijse.triplea.dao.custom.OrderDAO;
import lk.ijse.triplea.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class OrderDAOImpl implements OrderDAO {
    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CRUDUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            int nextIdNum = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("O%03d", nextIdNum);
        }
        return "O001";
    }

    @Override
    public boolean add(Order entity) throws SQLException, ClassNotFoundException {
        return CRUDUtil.execute("INSERT INTO orders VALUES(?,?,?)", entity.getOrderId(), entity.getDate(), entity.getTotal());
    }

    // Unused CRUD methods for Order
    @Override
    public boolean update(Order entity) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Order search(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Order> getAll() {
        return null;
    }

    @Override
    public Map<String, Double> getDailySalesChartData() throws SQLException, ClassNotFoundException {
        Map<String, Double> data = new LinkedHashMap<>();

        // SQL to get last 7 days of sales
        String sql = "SELECT order_date, SUM(total_amount) AS daily_total " +
                "FROM orders " +
                "GROUP BY order_date " +
                "ORDER BY order_date ASC " +
                "LIMIT 7";

        ResultSet rs = CRUDUtil.execute(sql);

        while (rs.next()) {
            data.put(rs.getString("order_date"), rs.getDouble("daily_total"));
        }
        return data;
    }
}

