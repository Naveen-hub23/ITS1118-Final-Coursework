package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.bo.BOFactory;
import lk.ijse.triplea.bo.custom.PurchaseOrderBO;
import lk.ijse.triplea.dto.CartDTO;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.dto.OrderDTO;
import lk.ijse.triplea.dto.OrderDetailDTO;
import lk.ijse.triplea.util.RegexUtil;
import lk.ijse.triplea.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingController {
    @FXML
    private TextField txtId, txtName, txtQty, txtUnitPrice, txtCash;
    @FXML
    private Label lblTotalAmount, lblOrderId, lblBalance;
    @FXML
    private TableView<CartDTO> tblCart;
    @FXML
    private TableColumn<CartDTO, Integer> colId, colQty;
    @FXML
    private TableColumn<CartDTO, String> colName;
    @FXML
    private TableColumn<CartDTO, Double> colPrice, colTotal;

    private ObservableList<CartDTO> cartList = FXCollections.observableArrayList();
    private double netTotal = 0.0;

    PurchaseOrderBO purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PURCHASE_ORDER);

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tblCart.setItems(cartList);
        setNextOrderId();
    }

    private void setNextOrderId() {
        try {
            lblOrderId.setText(purchaseOrderBO.getNextOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCompleteBillOnAction(ActionEvent event) {
        if (cartList.isEmpty()) return;

        try {
            String orderId = lblOrderId.getText();
            double cash = Double.parseDouble(txtCash.getText());
            List<OrderDetailDTO> details = new ArrayList<>();
            for (CartDTO cart : cartList) {
                details.add(new OrderDetailDTO(orderId, cart.getId(), cart.getQty(), cart.getUnitPrice()));
            }

            OrderDTO orderDTO = new OrderDTO(orderId, new Date(System.currentTimeMillis()), netTotal, details);

            if (purchaseOrderBO.placeOrder(orderDTO)) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed!").show();
                printInvoice(orderId, cash);
                clearAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Jasper Report Logic
    public void printInvoice(String orderId, double cashReceived) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            InputStream report = getClass().getResourceAsStream("/lk/ijse/tripleaadheretola/reports/invoice.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(report);
            Map<String, Object> params = new HashMap<>();
            params.put("OrderID", orderId);
            params.put("CashReceived", cashReceived);
            JasperPrint jp = JasperFillManager.fillReport(jr, params, conn);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAll() {
        cartList.clear();
        lblTotalAmount.setText("Rs 0.00");
        txtCash.clear();
        setNextOrderId();
    }

    @FXML
    void btnAddToBillOnAction(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtId.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtUnitPrice.getText());

            cartList.add(new CartDTO(id, txtName.getText(), qty, price, qty * price));
            netTotal += (qty * price);
            lblTotalAmount.setText(String.format("Rs %.2f", netTotal));
            txtId.clear();
            txtName.clear();
            txtQty.clear();
            txtUnitPrice.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void txtIdOnAction(ActionEvent event) {
        try {
            ItemDTO item = purchaseOrderBO.searchItem(Integer.parseInt(txtId.getText()));
            if (item != null) {
                txtName.setText(item.getName());
                txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                txtQty.requestFocus();
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
    private void btnBackToDashboard(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }
}