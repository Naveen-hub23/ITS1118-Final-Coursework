package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.UserDTO;
import lk.ijse.triplea.model.UserModel;

import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtQuestion;

    @FXML
    private TextField txtAnswer;

    @FXML
    private TextField txtNewPassword;

    private String realAnswer = "";


    private final UserModel userModel = new UserModel();

    @FXML
    void txtUsernameOnAction(ActionEvent event) {

        String username = txtUsername.getText();

        try {
            UserDTO user = userModel.searchSecurityDetails(username);

            if (user != null) {
                txtQuestion.setText(user.getSecurityQuestion());
                realAnswer = user.getSecurityAnswer();
                txtAnswer.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "User not found").show();
            }

        }catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "DB Error").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {

        String userAnswer = txtAnswer.getText();
        String newPass = txtNewPassword.getText();
        String username = txtUsername.getText();


        if (userAnswer.equalsIgnoreCase(realAnswer)) {

            try {
                boolean isUpdated = userModel.updatePassword(username, newPass);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Password Changed Successfully").show();

                    Stage stage = (Stage) txtAnswer.getScene().getWindow();
                    stage.close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed").show();
                }

            }catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Wrong Answer!").show();
        }

    }

}