package lk.ijse.triplea.dao.custom;

import lk.ijse.triplea.dao.CrudDAO;
import lk.ijse.triplea.entity.Supplier;

import java.sql.SQLException;


public interface SupplierDAO extends CrudDAO<Supplier> {

    int getSupplierCount() throws SQLException, ClassNotFoundException;
}
