package lk.ijse.triplea.bo.custom.impl;

import lk.ijse.triplea.bo.custom.OrderBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.OrderDAO;
import lk.ijse.triplea.db.DBConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

public class OrderBOImpl implements OrderBO {
    // Get OrderDAO from Factory
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public Map<String, Double> getDailySalesChartData() throws SQLException, ClassNotFoundException {
        return orderDAO.getDailySalesChartData();
    }

    @Override
    public void printOrderReport() throws Exception {
        InputStream reportStream = getClass().getResourceAsStream("/lk/ijse/triplea/reports/orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
    }
}