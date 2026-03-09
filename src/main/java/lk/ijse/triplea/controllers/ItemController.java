package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
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

    // Dependency Injection via Factory (Layered Architecture approach)
    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        loadItemTable();
    }

    private boolean isValid() {
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
        if (isValid()) {
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

                if (itemBO.saveItem(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Item Saved Successfully").show();
                    loadItemTable();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please correct the highlighted fields").show();
        }
    }

    @FXML
    private void handleUpdateItem() {
        try {
            if (idField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select an item first").show();
                return;
            }

            ItemDTO dto = new ItemDTO(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    Integer.parseInt(qtyField.getText()),
                    Double.parseDouble(unitPriceField.getText())
            );

            if (itemBO.updateItem(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Item Updated Successfully").show();
                loadItemTable();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Update Failed: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleDeleteItem() {
        try {
            if (idField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter or select an ID").show();
                return;
            }
            int id = Integer.parseInt(idField.getText());

            if (itemBO.deleteItem(id)) {
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully").show();
                loadItemTable();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Delete Failed: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleSearchItem(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String idText = idField.getText();
            if (idText.isEmpty()) return;

            try {
                ItemDTO item = itemBO.searchItem(Integer.parseInt(idText));

                if (item != null) {
                    nameField.setText(item.getName());
                    qtyField.setText(String.valueOf(item.getQty()));
                    unitPriceField.setText(String.valueOf(item.getUnitPrice()));
                } else {
                    new Alert(Alert.AlertType.WARNING, "No Item found for ID: " + idText).show();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Search Error").show();
            }
        }
    }

    private void loadItemTable() {
        try {
            List<ItemDTO> list = itemBO.getAllItems();
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
        idField.setStyle(null);
        nameField.setStyle(null);
        qtyField.setStyle(null);
        unitPriceField.setStyle(null);
    }

    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            // Updated path to reflect the new package structure
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/tripleaadheretola/view" + fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Navigation Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleResetFields() {
        clearFields();
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
    private void btnLogoutOnAction(ActionEvent event) {
        navigateTo("/Login.fxml", event);
    }
}