package lk.ijse.triplea.dao.custom.impl;


import lk.ijse.triplea.dao.CRUDUtil;
import lk.ijse.triplea.dao.custom.OrderDetailDAO;
import lk.ijse.triplea.entity.OrderDetail;

import java.sql.SQLException;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public boolean save(OrderDetail entity) throws SQLException, ClassNotFoundException {

        return CRUDUtil.execute("INSERT INTO order_details VALUES(?,?,?,?)",
                entity.getOrderId(), entity.getItemId(), entity.getQty(), entity.getUnitPrice());
    }
}