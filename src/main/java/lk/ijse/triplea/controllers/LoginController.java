package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.UserBO;
import lk.ijse.triplea.dto.UserDTO;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public static String currentUserRole = "";
    public static String currentUserName = "";

    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    private void initialize() {
        passwordField.setOnAction(event -> {
            try { login(); } catch (IOException e) { e.printStackTrace(); }
        });
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "Please enter both credentials.");
            return;
        }

        try {
            UserDTO user = userBO.verifyLogin(username, password);
            if (user != null) {
                if ("DEACTIVE".equalsIgnoreCase(user.getStatus())) {
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "Contact Owner.");
                    return;
                }
                currentUserName = user.getUsername();
                currentUserRole = user.getRole().toUpperCase();
                navigateToDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void navigateToDashboard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/tripleaadheretola/view/Dashboard.fxml"));
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(new Scene(root));
        dashboardStage.setTitle("Triple A Cake Decor - Dashboard");
        dashboardStage.setMaximized(true);
        dashboardStage.show();
        ((Stage) usernameField.getScene().getWindow()).close();
    }

    @FXML
    private void forgotPassword() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Enter Username first").show();
            return;
        }

        if (username.equalsIgnoreCase("owner")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/tripleaadheretola/view/ForgotPassword.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Reset Password");
                stage.show();
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Contact Owner", "Please contact owner: 075-5201694");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}