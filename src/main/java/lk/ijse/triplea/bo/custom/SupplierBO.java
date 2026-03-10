package lk.ijse.triplea.bo.custom;


import lk.ijse.triplea.bo.SuperBO;
import lk.ijse.triplea.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO extends SuperBO {
    boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteSupplier(int id) throws SQLException, ClassNotFoundException;

    SupplierDTO searchSupplier(int id) throws SQLException, ClassNotFoundException;

    ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException;
}
