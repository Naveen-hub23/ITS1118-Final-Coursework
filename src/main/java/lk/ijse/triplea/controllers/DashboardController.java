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
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.ItemBO;
import lk.ijse.triplea.bo.custom.OrderBO;
import lk.ijse.triplea.bo.custom.SupplierBO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DashboardController {
    @FXML private Label lblItemCount, lblSupplierCount, lblLowStock, lblTime, lblDate;
    @FXML private AreaChart<String, Number> incomeChart;

    // Inject required BOs
    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);
    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);
    OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDER);

    @FXML
    public void initialize() {
        refreshDashboard();
        startClock();
    }

    private void refreshDashboard() {
        try {
            // Get data from BOs
            lblItemCount.setText(String.valueOf(itemBO.getItemCount()));
            lblSupplierCount.setText(String.valueOf(supplierBO.getSupplierCount()));

            int lowStock = itemBO.getLowStockCount();
            lblLowStock.setText(String.valueOf(lowStock));
            if (lowStock > 0) lblLowStock.setStyle("-fx-text-fill: red;");

            // Load Chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Map<String, Double> sales = orderBO.getDailySalesChartData();
            sales.forEach((date, total) -> series.getData().add(new XYChart.Data<>(date, total)));
            incomeChart.getData().clear();
            incomeChart.getData().add(series);

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startClock() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
            lblDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    void handleLowStockClick() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/tripleaadheretola/view/LowStockForm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void navigate(String fxml, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/tripleaadheretola/view" + fxml));
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Role-Based Actions
    @FXML void btnItemManagementOnAction(ActionEvent e) {
        if (LoginController.isOwner() || LoginController.isAssistant()) navigate("/Item.fxml", e);
        else new Alert(Alert.AlertType.WARNING, "Access Denied").show();
    }

    @FXML void btnLogoutOnAction(ActionEvent e) { navigate("/Login.fxml", e); }
    // ... other button actions follow same pattern
}