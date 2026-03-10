package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.bo.custom.OrderBO;

import java.io.IOException;

public class ReportsController {

    // Injecting BOs via Factory
    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);
    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @FXML
    private VBox paneItemReport;

    @FXML
    private VBox paneOrderReport;

    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea" + fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Navigation Error").show();
        }
    }

    @FXML
    private void btnBackOnAction(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }

    @FXML
    void btnItemReportOnAction(MouseEvent event) {
        try {
            // Calling Business Layer instead of Model
            itemBO.printItemReport();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating Item report").show();
        }
    }

    @FXML
    void btnOrderReportOnAction(MouseEvent event) {
        try {
            // Calling Business Layer instead of Model
            orderBO.printOrderReport();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating Order report").show();
        }
    }
}