package lk.ijse.triplea.model;

import lk.ijse.triplea.dto.OrderDetailDTO;
import java.sql.*;
import java.util.List;

public class OrderDetailModel {

    public static boolean saveDetails(List<OrderDetailDTO> details, Connection conn) throws SQLException {

        for (OrderDetailDTO dto : details) {
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO order_details VALUES(?,?,?,?)");
            pstm.setString(1, dto.getOrderId());
            pstm.setInt(2, dto.getItemId());
            pstm.setInt(3, dto.getQty());
            pstm.setDouble(4, dto.getUnitPrice());
            if (pstm.executeUpdate() <= 0) return false;
        }
        return true;
    }

}