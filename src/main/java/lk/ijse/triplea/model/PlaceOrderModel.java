package lk.ijse.triplea.model;

import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.OrderDTO;
import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderModel {

    public static boolean placeOrder(OrderDTO orderDTO) throws SQLException {

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);

            if (OrderModel.save(orderDTO, conn)) {
                if (OrderDetailModel.saveDetails(orderDTO.getDetails(), conn)) {
                    if (ItemModel.updateStock(orderDTO.getDetails(), conn)) {
                        conn.commit();
                        return true;
                    }
                }
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

}
