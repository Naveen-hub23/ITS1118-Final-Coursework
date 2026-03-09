package lk.ijse.triplea.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.triplea.model.ItemModel;
import lk.ijse.triplea.model.OrderModel;
import lk.ijse.triplea.model.SupplierModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class DashboardController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblSupplierCount;

    @FXML
    private Label lblLowStock;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblDate;

    @FXML
    private AreaChart<String, Number> incomeChart;


    private final ItemModel itemModel = new ItemModel();
    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    public void initialize() {
        loadDashboardCounts();
        initClock();
        loadDailySalesChart();
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

    private void initClock() {
        EventHandler<ActionEvent> clockAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDateTime now = LocalDateTime.now();

                lblTime.setText(now.format(DateTimeFormatter.ofPattern("hh:mm a")));

                lblDate.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        };

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), clockAction));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private void loadDashboardCounts() {

        try {
            int itemCount = itemModel.getItemCount();
            lblItemCount.setText(String.valueOf(itemCount));

            int supplierCount = supplierModel.getSupplierCount();
            lblSupplierCount.setText(String.valueOf(supplierCount));


            int lowStock = itemModel.getLowStockCount();
            lblLowStock.setText(String.valueOf(lowStock));


            if (lowStock > 0) {
                lblLowStock.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading dashboard: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleLowStockClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea/LowStockForm.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Low Stock Items");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading LowStockForm: " + e.getMessage()).show();
        }
    }


    private void showAccessDenied() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setContentText("You have no access");
        alert.showAndWait();
    }

    private void loadDailySalesChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        try {
            Map<String, Double> salesData = OrderModel.getDailySales();

            for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            incomeChart.getData().clear();
            incomeChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }

    @FXML
    void btnItemManagementOnAction(ActionEvent event) {
        if (LoginController.isOwner() || LoginController.isAssistant()) {
            navigateTo("/Item.fxml", event);
        } else {
            showAccessDenied();
        }
    }

    @FXML
    void btnSupplierManagementOnAction(ActionEvent event) {
        if (LoginController.isOwner() || LoginController.isAssistant()) {
            navigateTo("/Supplier.fxml", event);
        } else {
            showAccessDenied();
        }
    }

    @FXML
    void btnBillingOnAction(ActionEvent event) {
        if (LoginController.isOwner() || LoginController.isCashier()) {
            navigateTo("/Billing.fxml", event);
        } else {
            showAccessDenied();
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        if (LoginController.isOwner()) {
            navigateTo("/Reports.fxml", event);
        } else {
            showAccessDenied();
        }
    }

    @FXML
    void btnUserManagementOnAction(ActionEvent event) {
        if (LoginController.isOwner()) {
            navigateTo("/User.fxml", event);
        } else {
            showAccessDenied();
        }
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        navigateTo("/Login.fxml", event);
    }
}
