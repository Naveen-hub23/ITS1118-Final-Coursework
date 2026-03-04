package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.dto.OrderDetailDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {


    public boolean saveItem(ItemDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO item (name, qty, unit_price) VALUES (?, ?, ?)";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setInt(2, dto.getQty());
        pstm.setDouble(3, dto.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }


    public boolean updateItem(ItemDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE item SET name=?, qty=?, unit_price=? WHERE id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setInt(2, dto.getQty());
        pstm.setDouble(3, dto.getUnitPrice());
        pstm.setInt(4, dto.getId());

        return pstm.executeUpdate() > 0;
    }


    public boolean deleteItem(int id) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM item WHERE id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        return pstm.executeUpdate() > 0;
    }


    public List<ItemDTO> getAllItems() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM item";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<ItemDTO> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new ItemDTO(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("qty"),
                    rs.getDouble("unit_price")
            ));
        }
        return list;
    }

    public static ItemDTO searchItemById(int id) throws SQLException {

       Connection conn = DBConnection.getInstance().getConnection();
       String sql = "SELECT * FROM item WHERE id = ?";
       PreparedStatement pstm = conn.prepareStatement(sql);
       pstm.setInt(1, id);

       ResultSet rs = pstm.executeQuery();
         if (rs.next()) {
              return new ItemDTO(
                     rs.getInt("id"),
                     rs.getString("name"),
                     rs.getInt("qty"),
                     rs.getDouble("unit_price")
              );
         }
         return null;
    }

    public static ItemDTO searchItemByName(String name) throws SQLException {

       Connection conn = DBConnection.getInstance().getConnection();
       String sql = "SELECT * FROM item WHERE LOWER(name) = LOWER(?)";
         PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, name);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return new ItemDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("qty"),
                        rs.getDouble("unit_price")
                );
            }
            return null;
    }

    public static List<String> getAllItemNames() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM item";

        List<String> names = new ArrayList<>();
        ResultSet rs = conn.prepareStatement(sql).executeQuery();

        while (rs.next()) {
            names.add(rs.getString("name"));
        }
        return names;
    }

    public static boolean updateStock(List<OrderDetailDTO> details, Connection conn) throws SQLException {

        for (OrderDetailDTO detail : details) {
            PreparedStatement pstm = conn.prepareStatement("UPDATE item SET qty = qty - ? WHERE id = ?");
            pstm.setInt(1, detail.getQty());
            pstm.setInt(2, detail.getItemId());
            if (pstm.executeUpdate() <= 0) return false;
        }
        return true;
    }

    public static ItemDTO searchItem(int id) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM item WHERE id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return new ItemDTO(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("qty"),
                    rs.getDouble("unit_price")
            );
        }
        return null;
    }

    public static int getItemQty(int itemId) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT qty FROM item WHERE id = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, itemId);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }



    public int getItemCount() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM item";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        if(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public void printItemReport() throws SQLException, JRException {

        Connection conn = DBConnection.getInstance().getConnection();

        InputStream reportObject = this.getClass().getResourceAsStream("/lk/ijse/triplea/reports/items.jrxml");

        JasperReport jr = JasperCompileManager.compileReport(reportObject);

        JasperPrint jp = JasperFillManager.fillReport(jr,null,conn);

        JasperViewer.viewReport(jp,false);
    }


    public int getLowStockCount() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM item WHERE qty <= 20";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    public List<ItemDTO> getLowStockItems() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM item WHERE qty <= 20";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<ItemDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new ItemDTO(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("qty"),
                    rs.getDouble("unit_price")
            ));
        }
        return list;
    }


}