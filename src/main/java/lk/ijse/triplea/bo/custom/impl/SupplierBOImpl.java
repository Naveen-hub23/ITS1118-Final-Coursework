package lk.ijse.triplea.bo.custom.impl;


import lk.ijse.triplea.bo.custom.SupplierBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.SupplierDAO;
import lk.ijse.triplea.dto.SupplierDTO;
import lk.ijse.triplea.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOImpl implements SupplierBO {

    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.add(new Supplier(0, dto.getName(), dto.getPhone(), dto.getEmail()));
    }

    @Override
    public boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(dto.getId(), dto.getName(), dto.getPhone(), dto.getEmail()));
    }

    @Override
    public boolean deleteSupplier(int id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    @Override
    public SupplierDTO searchSupplier(int id) throws SQLException, ClassNotFoundException {
        Supplier s = supplierDAO.search(id);
        if (s != null) {
            return new SupplierDTO(s.getId(), s.getName(), s.getPhone(), s.getEmail());
        }
        return null;
    }

    @Override
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> all = supplierDAO.getAll();
        ArrayList<SupplierDTO> allDTO = new ArrayList<>();
        for (Supplier s : all) {
            allDTO.add(new SupplierDTO(s.getId(), s.getName(), s.getPhone(), s.getEmail()));
        }
        return allDTO;
    }
}

