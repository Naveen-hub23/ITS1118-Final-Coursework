package lk.ijse.triplea.bo.custom.impl;

import lk.ijse.triplea.bo.custom.OrderBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.OrderDAO;

import java.sql.SQLException;
import java.util.Map;

public class OrderBOImpl implements OrderBO {
    // Get OrderDAO from Factory
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public Map<String, Double> getDailySalesChartData() throws SQLException, ClassNotFoundException {
        return orderDAO.getDailySalesChartData();
    }
}