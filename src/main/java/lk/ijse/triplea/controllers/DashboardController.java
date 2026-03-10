package lk.ijse.triplea.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.bo.custom.OrderBO;
import lk.ijse.triplea.bo.custom.SupplierBO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DashboardController {

    @FXML
    private AnchorPane rootNode;
    @FXML
    private Label lblItemCount, lblSupplierCount, lblLowStock, lblTime, lblDate;
    @FXML
    private AreaChart<String, Number> incomeChart;

    // Injecting Business Objects via Factory
    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);
    private final SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);
    private final OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @FXML
    public void initialize() {
        loadDashboardCounts();
        initClock();
        loadDailySalesChart();
    }

    private void initClock() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            lblTime.setText(now.format(DateTimeFormatter.ofPattern("hh:mm a")));
            lblDate.setText(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadDashboardCounts() {
        try {
            lblItemCount.setText(String.valueOf(itemBO.getItemCount()));
            lblSupplierCount.setText(String.valueOf(supplierBO.getSupplierCount()));

            int lowStock = itemBO.getLowStockCount();
            lblLowStock.setText(String.valueOf(lowStock));

            if (lowStock > 0) {
                lblLowStock.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDailySalesChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        try {
            Map<String, Double> salesData = orderBO.getDailySalesChartData();
            if (salesData != null) {
                for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                }
                incomeChart.getData().clear();
                incomeChart.getData().add(series);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    void handleLowStockClick(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/triplea/LowStockForm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Low Stock Items");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void showAccessDenied() {
        new Alert(Alert.AlertType.WARNING, "Access Denied: You do not have permission").show();
    }
}