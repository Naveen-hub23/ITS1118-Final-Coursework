package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.dto.ItemDTO;

public class LowStockController {
    @FXML
    private TableView<ItemDTO> tblLowStock;
    @FXML
    private TableColumn<ItemDTO, String> colName;
    @FXML
    private TableColumn<ItemDTO, Integer> colQty;

    // Use Factory to get BO
    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        loadData();
    }

    private void loadData() {
        try {
            tblLowStock.setItems(FXCollections.observableArrayList(itemBO.getLowStockItems()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}