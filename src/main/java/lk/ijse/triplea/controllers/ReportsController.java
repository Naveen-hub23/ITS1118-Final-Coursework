package lk.ijse.triplea.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lk.ijse.triplea.model.ItemModel;
import lk.ijse.triplea.model.OrderModel;

import java.io.IOException;

public class ReportsController {

    private final ItemModel itemModel = new ItemModel();

    private final OrderModel orderModel = new OrderModel();

    @FXML
    private VBox paneItemReport;

    @FXML
    private VBox paneOrderReport;

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
    private void btnBackOnAction (ActionEvent event){
        navigateTo("/Dashboard.fxml", event);
    }

    @FXML
    void btnItemReportOnAction(MouseEvent event)  {
        try{
            itemModel.printItemReport();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating Item report").show();
        }
    }

    @FXML
    void btnOrderReportOnAction(MouseEvent event) {
        try{
          orderModel.printOrderReport();
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating Order report").show();
        }
    }

}