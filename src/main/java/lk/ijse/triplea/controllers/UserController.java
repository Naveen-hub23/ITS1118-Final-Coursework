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
import lk.ijse.triplea.dto.UserDTO;
import lk.ijse.triplea.model.UserModel;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button btnTogglePassword;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TableView<UserDTO> tableUser;

    @FXML
    private TableColumn<UserDTO, Integer> colUserId;

    @FXML
    private TableColumn<UserDTO, String> colUsername;

    @FXML
    private TableColumn<UserDTO, String> colPhone;

    @FXML
    private TableColumn<UserDTO, String> colRole;

    @FXML
    private TableColumn<UserDTO, String> colStatus;

    private final UserModel userModel = new UserModel();
    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        roleComboBox.setItems(FXCollections.observableArrayList("Owner", "Assistant", "Cashier"));
        statusComboBox.setItems(FXCollections.observableArrayList("ACTIVE", "DEACTIVE"));


        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        loadUserTable();
    }


    private boolean isValied() {

        boolean nameValid = usernameField.getText().matches(RegexUtil.NAME);
        boolean phoneValid = phoneField.getText().matches(RegexUtil.PHONE);


        boolean passValid = !passwordField.getText().isEmpty() || !passwordTextField.getText().isEmpty();

        boolean roleValid = roleComboBox.getValue() != null;
        boolean statusValid = statusComboBox.getValue() != null;

        usernameField.setStyle(nameValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        phoneField.setStyle(phoneValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");


        String passStyle = passValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;";
        passwordField.setStyle(passStyle);
        passwordTextField.setStyle(passStyle);

        roleComboBox.setStyle(roleValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        statusComboBox.setStyle(statusValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");

        return nameValid && phoneValid && passValid && roleValid && statusValid;
    }

    @FXML
    private void saveUser() {

        if (isValied()) {
            try {

                String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();

                UserDTO dto = new UserDTO(
                        usernameField.getText(),
                        password,
                        phoneField.getText(),
                        roleComboBox.getValue(),
                        statusComboBox.getValue()
                );

                if (userModel.saveUser(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "User Saved").show();
                    loadUserTable();
                    clearFields();
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate")) {
                    new Alert(Alert.AlertType.ERROR, "Username already exists").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error saving user: " + e.getMessage()).show();
                }
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Please correct the fields").show();
        }
    }

    @FXML
    private void handleUpdateUser() {

        if (isValied()) {

            try {
                String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();

                UserDTO dto = new UserDTO(
                        usernameField.getText(),
                        password,
                        phoneField.getText(),
                        roleComboBox.getValue(),
                        statusComboBox.getValue()
                );

                if (userModel.updateUser(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "User Updated").show();
                    loadUserTable();
                    clearFields();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating user").show();
            }
        }
    }

    @FXML
    private void handleDeleteUser() {

        try {
            String username = usernameField.getText();
            if (username.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Enter Username").show();
                return;
            }

            if (userModel.deleteUser(username)) {
                new Alert(Alert.AlertType.INFORMATION, "User Deleted!").show();
                loadUserTable();
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error deleting user").show();
        }
    }


    @FXML
    private void handleSearchUser(ActionEvent event) {

        String username = usernameField.getText();
        if (username.isEmpty()) return;

        try {
            UserDTO user = userModel.searchUser(username);

            if (user != null) {

                phoneField.setText(user.getPhone());


                passwordField.setText(user.getPassword());
                passwordTextField.setText(user.getPassword());

                roleComboBox.setValue(user.getRole());
                statusComboBox.setValue(user.getStatus());


                phoneField.requestFocus();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "User not found").show();
                phoneField.clear();
                passwordField.clear();
                passwordTextField.clear();
                roleComboBox.getSelectionModel().clearSelection();
                statusComboBox.getSelectionModel().clearSelection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching user").show();
        }
    }


    @FXML
    void togglePasswordVisible(ActionEvent event) {

        if (isPasswordVisible) {

            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);

            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);

            btnTogglePassword.setText("👁");
            isPasswordVisible = false;
        } else {

            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            btnTogglePassword.setText("🔒");
            isPasswordVisible = true;
        }
    }

    private void loadUserTable() {

        try {
            List<UserDTO> list = userModel.getAllUsers();
            ObservableList<UserDTO> obList = FXCollections.observableArrayList(list);
            tableUser.setItems(obList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetFields() {
        clearFields();
    }

    private void clearFields() {
        usernameField.clear();
        phoneField.clear();
        passwordField.clear();
        passwordTextField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();


        usernameField.setStyle("-fx-border-color: #D85945;");
        phoneField.setStyle("-fx-border-color: #D85945;");
        passwordField.setStyle("-fx-border-color: #D85945;");
        roleComboBox.setStyle("-fx-border-color: #D85945;");
        statusComboBox.setStyle("-fx-border-color: #D85945;");
    }


    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea" + fxmlPath));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
    private void btnLogoutOnAction(ActionEvent event) {
        navigateTo("/Login.fxml", event);
    }

}