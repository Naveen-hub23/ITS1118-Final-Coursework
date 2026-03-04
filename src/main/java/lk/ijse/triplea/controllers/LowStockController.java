package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.model.ItemModel;

import java.sql.SQLException;
import java.util.List;

public class LowStockController {

    @FXML
    private TableView<ItemDTO> tblLowStock;

    @FXML
    private TableColumn<ItemDTO, String> colName;

    @FXML
    private TableColumn<ItemDTO, Integer> colQty;

    private final ItemModel itemModel = new ItemModel();

    @FXML
    public void initialize() {

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        loadData();
    }

    private void loadData() {

        try {
            List<ItemDTO> lowStockItems = itemModel.getLowStockItems();

            ObservableList<ItemDTO> tableData = FXCollections.observableArrayList(lowStockItems);

            tblLowStock.setItems(tableData);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}