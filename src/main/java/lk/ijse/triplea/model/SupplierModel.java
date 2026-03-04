package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.SupplierDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {


    public boolean saveSupplier(SupplierDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO supplier (name, phone, email) VALUES (?, ?, ?)";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getPhone());
        pstm.setString(3, dto.getEmail());

        return pstm.executeUpdate() > 0;
    }


    public boolean updateSupplier(SupplierDTO dto) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "UPDATE supplier SET name=?, phone=?, email=? WHERE supplier_id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getPhone());
        pstm.setString(3, dto.getEmail());
        pstm.setInt(4, dto.getId());

        return pstm.executeUpdate() > 0;
    }


    public boolean deleteSupplier(int id) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM supplier WHERE supplier_id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        return pstm.executeUpdate() > 0;
    }


    public List<SupplierDTO> getAllSuppliers() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM supplier";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        List<SupplierDTO> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
            ));
        }
        return list;
    }

    public static SupplierDTO searchSupplier(int id) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM supplier WHERE supplier_id=?";
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return new SupplierDTO(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email")
            );
        }
        return null;
    }



    public int getSupplierCount() throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        String sql = "SELECT COUNT(*) FROM supplier";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        if(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

}