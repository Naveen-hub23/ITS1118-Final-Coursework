package lk.ijse.triplea.bo.custom;



import lk.ijse.triplea.bo.SuperBO;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.dto.OrderDTO;
import java.sql.SQLException;
import java.util.List;

public interface PurchaseOrderBO extends SuperBO {

    String getNextOrderId() throws SQLException, ClassNotFoundException;
    ItemDTO searchItem(int id) throws SQLException, ClassNotFoundException;
    ItemDTO searchItemByName(String name) throws SQLException, ClassNotFoundException;
    List<String> getAllItemNames() throws SQLException, ClassNotFoundException;
    boolean placeOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;
}