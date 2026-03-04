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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.SupplierDTO;
import lk.ijse.triplea.model.SupplierModel;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TableView<SupplierDTO> tableSupplier;

    @FXML
    private TableColumn<SupplierDTO, Integer> colId;

    @FXML
    private TableColumn<SupplierDTO, String> colName;

    @FXML
    private TableColumn<SupplierDTO, String> colPhone;

    @FXML
    private TableColumn<SupplierDTO, String> colEmail;

    private final SupplierModel supplierModel = new SupplierModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadSupplierTable();
    }

    private boolean isSupplierValied() {

        boolean id = idField.getText().matches(RegexUtil.ID);
        boolean name = nameField.getText().matches(RegexUtil.NAME);
        boolean phone = phoneField.getText().matches(RegexUtil.PHONE);
        boolean email = emailField.getText().matches(RegexUtil.EMAIL);


        idField.setStyle(id ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        nameField.setStyle(name ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        phoneField.setStyle(phone ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        emailField.setStyle(email ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");

        return id && name &&  phone && email;
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

    @FXML
    private void saveSupplier() {

        if(isSupplierValied()) {
            try {
                if (nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Name and Phone are required").show();
                    return;
                }

                SupplierDTO dto = new SupplierDTO(
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText()
                );

                if (supplierModel.saveSupplier(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved").show();
                    loadSupplierTable();
                    clearFields();
                }
            }catch(Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error saving supplier").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Please correct the fields").show();
        }

    }

    @FXML
    private void handleUpdateSupplier() {

            try {
                SupplierDTO dto = new SupplierDTO(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText()
                );

                if (supplierModel.updateSupplier(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Updated").show();
                    loadSupplierTable();
                    clearFields();
                }
            }catch(Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Update Failed").show();
            }

    }

    @FXML
    private void handleDeleteSupplier() {

        try {
            if (idField.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter ID").show();
                return;
            }
            int id = Integer.parseInt(idField.getText());

            if (supplierModel.deleteSupplier(id)) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted").show();
                loadSupplierTable();
                clearFields();
            }
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Delete Failed").show();
        }
    }


    @FXML
    private void handleSearchSupplier(KeyEvent event) {

        if(event.getCode() == KeyCode.ENTER) {
            String idText = idField.getText();

            if(idText.isEmpty()) return;

            try{
                SupplierDTO supplier = SupplierModel.searchSupplier(Integer.parseInt(idText));

                if(supplier != null) {
                    nameField.setText(supplier.getName());
                    phoneField.setText(supplier.getPhone());
                    emailField.setText(supplier.getEmail());
                }else{
                    new Alert(Alert.AlertType.WARNING, "Supplier Not Found").show();
                    clearFields();
                }
            }catch(SQLException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error searching supplier").show();
            }catch(NumberFormatException e){
                new Alert(Alert.AlertType.ERROR, "Search Failed").show();
            }
        }

    }

    @FXML
    private void handleResetFields() {

        clearFields();
    }

    private void loadSupplierTable() {

        try {
            List<SupplierDTO> list = supplierModel.getAllSuppliers();
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
        navigateTo("/SupplierItem.fxml", event);
    }

    @FXML
    private void btnLogoutOnAction(ActionEvent event) {
        navigateTo("/Login.fxml", event);
    }

}