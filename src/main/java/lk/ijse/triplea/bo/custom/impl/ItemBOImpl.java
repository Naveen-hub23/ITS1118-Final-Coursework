package lk.ijse.triplea.bo.custom.impl;

import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.dao.DAOFactory;
import lk.ijse.triplea.dao.custom.ItemDAO;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {

    // Injecting DAO via Factory
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        // Convert DTO to Entity
        return itemDAO.add(new Item(0, dto.getName(), dto.getQty(), dto.getUnitPrice()));
    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        return itemDAO.update(new Item(dto.getId(), dto.getName(), dto.getQty(), dto.getUnitPrice()));
    }

    @Override
    public boolean deleteItem(int id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(id);
    }

    @Override
    public ItemDTO searchItem(int id) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(id);
        if (item != null) {
            // Convert Entity back to DTO for the Controller
            return new ItemDTO(item.getId(), item.getName(), item.getQty(), item.getUnitPrice());
        }
        return null;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll();
        ArrayList<ItemDTO> allDTO = new ArrayList<>();
        for (Item i : all) {
            allDTO.add(new ItemDTO(i.getId(), i.getName(), i.getQty(), i.getUnitPrice()));
        }
        return allDTO;
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        return itemDAO.getItemCount();
    }

    @Override
    public int getLowStockCount() throws SQLException, ClassNotFoundException {
        return itemDAO.getLowStockCount();
    }

    @Override
    public ArrayList<ItemDTO> getLowStockItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> items = itemDAO.getLowStockItems();
        ArrayList<ItemDTO> dtos = new ArrayList<>();
        for (Item i : items) {
            dtos.add(new ItemDTO(i.getId(), i.getName(), i.getQty(), i.getUnitPrice()));
        }
        return dtos;
    }
}