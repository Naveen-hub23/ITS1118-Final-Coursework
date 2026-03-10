package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.UserBO;
import lk.ijse.triplea.dto.UserDTO;

public class ForgotPasswordController {
    @FXML
    private TextField txtUsername, txtQuestion, txtAnswer, txtNewPassword;
    private String realAnswer = "";

    UserBO userBO = (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void txtUsernameOnAction(ActionEvent event) {
        try {
            UserDTO user = userBO.searchSecurityDetails(txtUsername.getText());
            if (user != null) {
                txtQuestion.setText(user.getSecurityQuestion());
                realAnswer = user.getSecurityAnswer();
                txtAnswer.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "User not found").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        if (txtAnswer.getText().equalsIgnoreCase(realAnswer)) {
            try {
                if (userBO.updatePassword(txtUsername.getText(), txtNewPassword.getText())) {
                    new Alert(Alert.AlertType.INFORMATION, "Password Changed").show();
                    ((Stage) txtAnswer.getScene().getWindow()).close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong Answer!").show();
        }
    }
}