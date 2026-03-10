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
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.UserBO;
import lk.ijse.triplea.dto.UserDTO;
import lk.ijse.triplea.util.RegexUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private TextField usernameField, phoneField, passwordTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnTogglePassword;
    @FXML
    private ComboBox<String> roleComboBox, statusComboBox;
    @FXML
    private TableView<UserDTO> tableUser;
    @FXML
    private TableColumn<UserDTO, Integer> colUserId;
    @FXML
    private TableColumn<UserDTO, String> colUsername, colPhone, colRole, colStatus;

    private boolean isPasswordVisible = false;

    // Injecting Business Logic via Factory
    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        loadUserTable();
    }

    private boolean isValid() {
        boolean nameValid = usernameField.getText().matches(RegexUtil.NAME);
        boolean phoneValid = phoneField.getText().matches(RegexUtil.PHONE);
        boolean passValid = !passwordField.getText().isEmpty() || !passwordTextField.getText().isEmpty();

        usernameField.setStyle(nameValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");
        phoneField.setStyle(phoneValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;");

        String passStyle = passValid ? "-fx-border-color: #51fb2b;" : "-fx-border-color: red;";
        passwordField.setStyle(passStyle);
        passwordTextField.setStyle(passStyle);

        return nameValid && phoneValid && passValid && roleComboBox.getValue() != null && statusComboBox.getValue() != null;
    }

    @FXML
    private void saveUser(ActionEvent event) {
        if (isValid()) {
            try {
                String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();
                UserDTO dto = new UserDTO(usernameField.getText(), password, phoneField.getText(), roleComboBox.getValue(), statusComboBox.getValue());

                if (userBO.saveUser(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "User Saved Successfully").show();
                    loadUserTable();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Username already exists or Database Error").show();
            }
        }
    }

    @FXML
    private void handleUpdateUser(ActionEvent event) {
        if (isValid()) {
            try {
                String password = isPasswordVisible ? passwordTextField.getText() : passwordField.getText();
                UserDTO dto = new UserDTO(usernameField.getText(), password, phoneField.getText(), roleComboBox.getValue(), statusComboBox.getValue());

                if (userBO.updateUser(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "User Updated Successfully").show();
                    loadUserTable();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Update Failed").show();
            }
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        try {
            String username = usernameField.getText();
            if (username.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter username to delete").show();
                return;
            }
            if (userBO.deleteUser(username)) {
                new Alert(Alert.AlertType.INFORMATION, "User Deleted").show();
                loadUserTable();
                clearFields();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Delete Failed").show();
        }
    }

    @FXML
    private void handleSearchUser(ActionEvent event) {
        String username = usernameField.getText();
        if (username.isEmpty()) return;

        try {
            UserDTO user = userBO.searchUser(username);
            if (user != null) {
                phoneField.setText(user.getPhone());
                passwordField.setText(user.getPassword());
                passwordTextField.setText(user.getPassword());
                roleComboBox.setValue(user.getRole());
                statusComboBox.setValue(user.getStatus());
            } else {
                new Alert(Alert.AlertType.WARNING, "User not found").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetFields(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void togglePasswordVisible(ActionEvent event) {
        if (isPasswordVisible) {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            btnTogglePassword.setText("👁");
        } else {
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            btnTogglePassword.setText("🔒");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    private void loadUserTable() {
        try {
            tableUser.setItems(FXCollections.observableArrayList(userBO.getAllUsers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.clear();
        phoneField.clear();
        passwordField.clear();
        passwordTextField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        usernameField.setStyle(null);
        phoneField.setStyle(null);
        passwordField.setStyle(null);
        passwordTextField.setStyle(null);
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