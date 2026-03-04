package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.SupplierItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierItemModel {

    public List<SupplierItemDTO> getItemsBySupplierId(int supId) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        String sql = "SELECT si.supplier_id, si.item_id, i.name, si.cost_price " +
                "FROM supplier_item si " +
                "JOIN item i ON si.item_id = i.id " +
                "WHERE si.supplier_id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, supId);
        ResultSet rs = pstm.executeQuery();

        List<SupplierItemDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new SupplierItemDTO(
                    rs.getInt("supplier_id"),
                    rs.getInt("item_id"),
                    rs.getString("name"),
                    rs.getDouble("cost_price")
            ));
        }
        return list;
    }


    public static boolean saveSupplierItem(SupplierItemDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO supplier_item (supplier_id, item_id, cost_price) VALUES (?, ?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, dto.getSupplierId());
        pstm.setInt(2, dto.getItemId());
        pstm.setDouble(3, dto.getCostPrice());

        return pstm.executeUpdate() > 0;
    }


    public static List<SupplierItemDTO> getAllSupplierItems() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT si.supplier_id, si.item_id, i.name, si.cost_price " +
                "FROM supplier_item si " +
                "JOIN item i ON si.item_id = i.id";

        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<SupplierItemDTO> list = new ArrayList<>();
        while(rs.next()){
            list.add(new SupplierItemDTO(
                    rs.getInt("supplier_id"),
                    rs.getInt("item_id"),
                    rs.getString("name"),
                    rs.getDouble("cost_price")
            ));
        }
        return list;
    }
}