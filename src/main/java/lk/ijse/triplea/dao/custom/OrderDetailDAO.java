package lk.ijse.triplea.dao.custom;


import lk.ijse.triplea.dao.SuperDAO;
import lk.ijse.triplea.entity.OrderDetail;

import java.sql.SQLException;

public interface OrderDetailDAO extends SuperDAO {

    boolean save(OrderDetail entity) throws SQLException, ClassNotFoundException;
}