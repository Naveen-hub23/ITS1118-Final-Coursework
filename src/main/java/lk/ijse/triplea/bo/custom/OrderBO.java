package lk.ijse.triplea.bo.custom;


import lk.ijse.triplea.bo.SuperBO;
import java.sql.SQLException;
import java.util.Map;

public interface OrderBO extends SuperBO {

    Map<String, Double> getDailySalesChartData() throws SQLException, ClassNotFoundException;

    void printOrderReport() throws Exception;
}