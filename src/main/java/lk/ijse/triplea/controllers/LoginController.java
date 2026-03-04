package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.UserDTO;
import lk.ijse.triplea.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;



public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public static String currentUserRole = "";
    public static String currentUserName = "";

    @FXML
    private void initialize() {

        passwordField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void login() throws IOException {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();


        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Required", "Please enter both username and password.");
            return;
        }

        try {
            UserDTO user = UserModel.verifyLogin(username, password);

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
                passwordField.clear();
                usernameField.requestFocus();
            }

        }catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error.");
        }
    }

    private void navigateToDashboard() throws IOException {
        // 1. Load the Dashboard FXML
        Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea/Dashboard.fxml"));

        // 2. Create a BRAND NEW Stage for the Dashboard
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(new Scene(root));

        // 3. Configure the new stage (Full Screen, Title, etc.)
        dashboardStage.setTitle("Triple A Cake Decor - Dashboard");
        dashboardStage.setMaximized(true); // Start maximized

        // 4. Show the new Dashboard window
        dashboardStage.show();

        // 5. Close the old Login window
        // (We get the old stage from one of the login fields)
        Stage loginStage = (Stage) usernameField.getScene().getWindow();
        loginStage.close();
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
                java.net.URL resource = getClass().getResource("/lk/ijse/triplea/ForgotPassword.fxml");

                Parent root = FXMLLoader.load(resource);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Reset Password");
                stage.centerOnScreen();
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contact Owner");
            alert.setHeaderText(null);
            alert.setContentText("Please contact the owner.\nPhone: 075-5201694");
            alert.show();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {

        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static boolean isOwner() {
        return "OWNER".equals(currentUserRole);
    }

    public static boolean isAssistant() {
        return "ASSISTANT".equals(currentUserRole);
    }

    public static boolean isCashier() {
        return "CASHIER".equals(currentUserRole);
    }

}
