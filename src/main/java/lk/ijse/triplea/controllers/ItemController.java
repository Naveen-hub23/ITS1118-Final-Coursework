package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.model.ItemModel;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField qtyField;

    @FXML
    private TextField unitPriceField;

    @FXML
    private TableView<ItemDTO> tableItem;

    @FXML
    private TableColumn<ItemDTO, Integer> colId;

    @FXML
    private TableColumn<ItemDTO, String> colName;

    @FXML
    private TableColumn<ItemDTO, Integer> colQty;

    @FXML
    private TableColumn<ItemDTO, Double> colUnitPrice;

    private final ItemModel itemModel = new ItemModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        loadItemTable();

    }

    private boolean isValied() {

        boolean id = idField.getText().matches(RegexUtil.ID);
        boolean name = nameField.getText().matches(RegexUtil.NAME);
        boolean qty = qtyField.getText().matches(RegexUtil.QTY);
        boolean price = unitPriceField.getText().matches(RegexUtil.PRICE);

        idField.setStyle(id ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        nameField.setStyle(name ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        qtyField.setStyle(qty ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        unitPriceField.setStyle(price ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");

        return id && name && qty && price;
    }



    @FXML
    private void saveItem() {
        if(isValied()) {
            try {

                if (nameField.getText().isEmpty() || qtyField.getText().isEmpty() || unitPriceField.getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
                    return;
                }


                ItemDTO dto = new ItemDTO(
                        nameField.getText(),
                        Integer.parseInt(qtyField.getText()),
                        Double.parseDouble(unitPriceField.getText())
                );

                if (itemModel.saveItem(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Saved").show();
                    loadItemTable();
                    clearFields();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid input").show();

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error saving item").show();
            }
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Please correct the fields").show();
        }
    }

    @FXML
    private void handleUpdateItem() {

        try {
            ItemDTO dto = new ItemDTO(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    Integer.parseInt(qtyField.getText()),
                    Double.parseDouble(unitPriceField.getText())
            );

            if (itemModel.updateItem(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Item Updated").show();
                loadItemTable();
                clearFields();
            }
        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating item").show();
        }
    }

    @FXML
    private void handleDeleteItem() {

        try {
            if(idField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter ID").show();
                return;
            }
            int id = Integer.parseInt(idField.getText());

            if (itemModel.deleteItem(id)) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted").show();
                loadItemTable();
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error deleting item").show();
        }
    }

    @FXML
    private void handleResetFields() {
        clearFields();
    }

    @FXML
    private void handleSearchItem(KeyEvent event) {

       if(event.getCode() == KeyCode.ENTER){
         String idText = idField.getText();
         if(idText.isEmpty()) return;

         try{
             ItemDTO item = ItemModel.searchItem(Integer.parseInt(idText));

             if(item != null){
                 nameField.setText(item.getName());
                 qtyField.setText(String.valueOf(item.getQty()));
                 unitPriceField.setText(String.valueOf(item.getUnitPrice()));
             }else{
                 new Alert(Alert.AlertType.WARNING, "Invalid ID").show();
                 clearFields();
             }
         }catch(SQLException e){
             e.printStackTrace();
             new Alert(Alert.AlertType.ERROR, "Error searching item").show();
         }catch(NumberFormatException e){
             new Alert(Alert.AlertType.ERROR, "Enter Valid ID").show();
         }

       }
    }

    private void loadItemTable() {

        try {
            List<ItemDTO> list = itemModel.getAllItems();
            ObservableList<ItemDTO> obList = FXCollections.observableArrayList(list);
            tableItem.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {

        idField.clear();
        nameField.clear();
        qtyField.clear();
        unitPriceField.clear();
    }


    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea" + fxmlPath));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // --- THE FIX ---
            // Instead of making a new Scene, just replace the content (Root)
            // This keeps the Maximized state, Width, and Height exactly as they are.
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Navigation Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void btnDashboardOnAction(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }

    @FXML
    private void btnItemManagementOnAction(ActionEvent event) {
        navigateTo("/Item.fxml", event);
    }

    @FXML
    private void btnSupplierManagementOnAction(ActionEvent event) {
        navigateTo("/Supplier.fxml", event);
    }

    @FXML
    private void btnBillingOnAction(ActionEvent event) {
        navigateTo("/Billing.fxml", event);
    }

    @FXML
    private void btnReportsOnAction(ActionEvent event) {
        navigateTo("/Reports.fxml", event);
    }

    @FXML
    private void btnUserManagementOnAction(ActionEvent event) {
        navigateTo("/User.fxml", event);
    }

    @FXML
    private void btnSupplierItemsOnAction(ActionEvent event) {
        // Change "SupplierItemForm.fxml" to whatever you named that file!
        navigateTo("/SupplierItem.fxml", event);
    }

    @FXML
    private void btnLogoutOnAction(ActionEvent event) {
        navigateTo("/Login.fxml", event);
    }

}