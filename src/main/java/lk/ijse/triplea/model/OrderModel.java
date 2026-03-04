package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.OrderDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderModel {

    public static String getNextOrderId() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        ResultSet rst = conn.createStatement().executeQuery("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            int nextIdNum = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("O%03d", nextIdNum);
        }
        return "O001";
    }

    public static boolean save(OrderDTO dto, Connection conn) throws SQLException {

        PreparedStatement pstm = conn.prepareStatement("INSERT INTO orders VALUES(?,?,?)");
        pstm.setString(1, dto.getOrderId());
        pstm.setDate(2, dto.getDate());
        pstm.setDouble(3, dto.getTotal());
        return pstm.executeUpdate() > 0;
    }

    public static Map<String, Double> getDailySales() throws SQLException {

        Map<String, Double> data = new LinkedHashMap<>();
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT * FROM (" +
                "    SELECT order_date, SUM(total_amount) as daily_total " +
                "    FROM orders " +
                "    GROUP BY order_date " +
                "    ORDER BY order_date DESC " +
                "    LIMIT 7" +
                ") AS recent_sales " +
                "ORDER BY order_date ASC";

        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            String date = rs.getString("order_date");
            double total = rs.getDouble("daily_total");
            data.put(date, total);
        }
        return data;
    }


    public void printOrderReport() throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObject = this.getClass().getResourceAsStream("/lk/ijse/triplea/reports/orders.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        JasperPrint jp = JasperFillManager.fillReport(jr,null,conn);

        JasperViewer.viewReport(jp,false);
    }

    public static void printInvoice(String orderId, double cashReceived) throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObject = OrderModel.class.getResourceAsStream("/lk/ijse/triplea/reports/invoice.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("OrderID", orderId);

        parameters.put("CashReceived", cashReceived);

        JasperPrint jp = JasperFillManager.fillReport(jr, parameters, conn);

        JasperViewer.viewReport(jp, false);
    }

}
