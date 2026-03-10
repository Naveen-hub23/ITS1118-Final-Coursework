package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.SupplierBO;
import lk.ijse.triplea.dto.SupplierDTO;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private TextField idField, nameField, phoneField, emailField;
    @FXML
    private TableView<SupplierDTO> tableSupplier;
    @FXML
    private TableColumn<SupplierDTO, Integer> colId;
    @FXML
    private TableColumn<SupplierDTO, String> colName, colPhone, colEmail;

    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadSupplierTable();
    }

    private boolean isSupplierValid() {
        boolean id = idField.getText().matches(RegexUtil.ID);
        boolean name = nameField.getText().matches(RegexUtil.NAME);
        boolean phone = phoneField.getText().matches(RegexUtil.PHONE);
        boolean email = emailField.getText().matches(RegexUtil.EMAIL);

        idField.setStyle(id ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        nameField.setStyle(name ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        phoneField.setStyle(phone ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        emailField.setStyle(email ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");

        return id && name && phone && email;
    }

    @FXML
    private void saveSupplier() {
        if (isSupplierValid()) {
            try {
                SupplierDTO dto = new SupplierDTO(nameField.getText(), phoneField.getText(), emailField.getText());
                if (supplierBO.saveSupplier(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved").show();
                    loadSupplierTable();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Save Error").show();
            }
        }
    }

    @FXML
    private void handleUpdateSupplier() {
        try {
            SupplierDTO dto = new SupplierDTO(Integer.parseInt(idField.getText()), nameField.getText(), phoneField.getText(), emailField.getText());
            if (supplierBO.updateSupplier(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated").show();
                loadSupplierTable();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Update Error").show();
        }
    }

    @FXML
    private void handleDeleteSupplier() {
        try {
            if (idField.getText().isEmpty()) return;
            if (supplierBO.deleteSupplier(Integer.parseInt(idField.getText()))) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted").show();
                loadSupplierTable();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Delete Error").show();
        }
    }

    @FXML
    private void handleSearchSupplier(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                SupplierDTO supplier = supplierBO.searchSupplier(Integer.parseInt(idField.getText()));
                if (supplier != null) {
                    nameField.setText(supplier.getName());
                    phoneField.setText(supplier.getPhone());
                    emailField.setText(supplier.getEmail());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Not Found").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Search Failed").show();
            }
        }
    }

    @FXML
    private void tableClick(MouseEvent event) {
        SupplierDTO selected = tableSupplier.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idField.setText(String.valueOf(selected.getId()));
            nameField.setText(selected.getName());
            phoneField.setText(selected.getPhone());
            emailField.setText(selected.getEmail());
        }
    }

    private void loadSupplierTable() {
        try {
            List<SupplierDTO> list = supplierBO.getAllSuppliers();
            tableSupplier.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea" + fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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