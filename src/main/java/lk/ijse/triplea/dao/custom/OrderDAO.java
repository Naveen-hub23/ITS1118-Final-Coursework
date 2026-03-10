package lk.ijse.triplea.dao.custom;

import lk.ijse.triplea.dao.CrudDAO;
import lk.ijse.triplea.entity.Order;

import java.sql.SQLException;
import java.util.Map;


public interface OrderDAO extends CrudDAO<Order> {

    Map<String, Double> getDailySalesChartData() throws SQLException, ClassNotFoundException;

    String getNextId() throws SQLException, ClassNotFoundException;
}

