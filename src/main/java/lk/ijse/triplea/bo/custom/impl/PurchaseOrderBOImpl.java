package lk.ijse.triplea.bo.custom.impl;



import lk.ijse.triplea.bo.custom.PurchaseOrderBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.ItemDAO;
import lk.ijse.triplea.dao.custom.OrderDAO;
import lk.ijse.triplea.dao.custom.OrderDetailDAO;
import lk.ijse.triplea.db.DBConnection;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.dto.OrderDTO;
import lk.ijse.triplea.dto.OrderDetailDTO;
import lk.ijse.triplea.entity.Item;
import lk.ijse.triplea.entity.Order;
import lk.ijse.triplea.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO orderDetailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public String getNextOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.getNextId();
    }

    @Override
    public boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false); // Transaction Start

            // 1. Save Order
            if (!orderDAO.add(new Order(dto.getOrderId(), dto.getDate(), dto.getTotal()))) {
                connection.rollback();
                return false;
            }
            
            // 2. Save Order Details & Update Stock
            for (OrderDetailDTO detail : dto.getDetails()) {
                if (!orderDetailDAO.save(new OrderDetail(detail.getOrderId(), detail.getItemId(), detail.getQty(), detail.getUnitPrice()))) {
                    connection.rollback();
                    return false;
                }

                // ItemDAO needs an updateQty method
                if (!itemDAO.updateQty(detail.getItemId(), detail.getQty())) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public ItemDTO searchItem(int id) throws SQLException, ClassNotFoundException {
        Item i = itemDAO.search(id);
        return i == null ? null : new ItemDTO(i.getId(), i.getName(), i.getQty(), i.getUnitPrice());
    }

    @Override
    public ItemDTO searchItemByName(String name) throws SQLException, ClassNotFoundException {
        // Assume you add this to ItemDAO
        return null;
    }

    @Override
    public List<String> getAllItemNames() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll();
        List<String> names = new ArrayList<>();
        for (Item i : all) names.add(i.getName());
        return names;
    }
}