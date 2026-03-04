package lk.ijse.triplea.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.CartDTO;
import lk.ijse.triplea.dto.ItemDTO;
import lk.ijse.triplea.model.ItemModel;
import lk.ijse.triplea.model.OrderModel;
import lk.ijse.triplea.dto.OrderDTO;
import lk.ijse.triplea.dto.OrderDetailDTO;
import lk.ijse.triplea.model.PlaceOrderModel;
import lk.ijse.triplea.util.RegexUtil;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BillingController {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    private TextField txtCash;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblBalance;

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

    private List<String> allItemNames = new ArrayList<>();
    private ContextMenu contextMenu = new ContextMenu();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tblCart.setItems(cartList);

        setNextOrderId();

        loadAllItemNames();
        setupAutoSuggestion();
    }

    private void setNextOrderId() {

        try {
            lblOrderId.setText(OrderModel.getNextOrderId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void txtCashOnKey(KeyEvent event) {

        String cashText = txtCash.getText();

        if (cashText.matches(RegexUtil.PRICE)) {
            try {
                double cash = Double.parseDouble(cashText);
                double balance = cash - netTotal;
                lblBalance.setText(String.format("Rs %.2f", balance));


                lblBalance.setTextFill(balance < 0 ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
                txtCash.setStyle("-fx-border-color: #C95F4D;");
            } catch (Exception e) {
                lblBalance.setText("Rs 0.00");
            }
        }else{
            txtCash.setStyle("-fx-border-color: red;");
            lblBalance.setText("Invalid Format");
        }
    }

    @FXML
    void btnRemoveItemOnAction(ActionEvent event) {

        CartDTO selectedItem = tblCart.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartList.remove(selectedItem);
            calculateNetTotal();
        }
    }

    @FXML
    void btnCompleteBillOnAction(ActionEvent event) {

        if (cartList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").show();
            return;
        }


        String cashText = txtCash.getText();
        if (!cashText.matches(lk.ijse.triplea.util.RegexUtil.PRICE)) {
            new Alert(Alert.AlertType.ERROR, "Invalid cash").show();
            txtCash.requestFocus();
            txtCash.setStyle("-fx-border-color: red;");
            return;
        }


        double cashAmount = Double.parseDouble(cashText);
        if (cashAmount < netTotal) {
            new Alert(Alert.AlertType.WARNING, "Insufficient cash").show();
            txtCash.requestFocus();
            return;
        }


        try {
            String orderId = lblOrderId.getText();
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

            List<OrderDetailDTO> details = new ArrayList<>();
            for (CartDTO cart : cartList) {
                details.add(new OrderDetailDTO(orderId, cart.getId(), cart.getQty(), cart.getUnitPrice()));
            }

            OrderDTO orderDTO = new OrderDTO(orderId, date, netTotal, details);

            if (PlaceOrderModel.placeOrder(orderDTO)) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully").show();

                OrderModel.printInvoice(orderId, cashAmount);


                cartList.clear();
                calculateNetTotal();
                txtCash.clear();
                lblBalance.setText("Rs 0.00");
                setNextOrderId();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }catch(JRException e) {
            new Alert(Alert.AlertType.ERROR, "Printing Error: " + e.getMessage()).show();
        }
    }


    @FXML
    void txtIdOnAction(ActionEvent event) {

        String idText = txtId.getText();
        if (idText.isEmpty()) {
            return;
        }

        try {
            ItemDTO item = ItemModel.searchItem(Integer.parseInt(idText));
            fillItemData(item);
        }catch(Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Search Error").show();
        }
    }


    @FXML
    void btnAddToBillOnAction(ActionEvent event) throws SQLException {

        String idText = txtId.getText();
        String qtyText = txtQty.getText();

        if (!idText.matches(RegexUtil.ID)) {
            txtId.setStyle("-fx-border-color: red;");
            return;
        }
        if (!qtyText.matches(RegexUtil.QTY)) {
            txtQty.setStyle("-fx-border-color: red;");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            int requestedQty = Integer.parseInt(qtyText);

            int stockAvailable = ItemModel.getItemQty(id);
            int qtyInCart = 0;

            CartDTO existingItem = null;
            for(CartDTO item : cartList) {
                if(item.getId() == id) {
                    qtyInCart = item.getQty();
                    existingItem = item;
                    break;
                }

            }
            if(stockAvailable < (qtyInCart + requestedQty)) {
                new Alert(Alert.AlertType.WARNING,
                        "Insufficient Stock\nAvailable: " + stockAvailable).show();
                txtQty.requestFocus();
                return;
            }

            String name = txtName.getText();
            double price = Double.parseDouble(txtUnitPrice.getText());
            double total = requestedQty * price;

            if(existingItem != null){
                existingItem.setQty(qtyInCart + requestedQty);
                existingItem.setTotal(existingItem.getQty() * price);
                tblCart.refresh();
            }else{
                cartList.add(new CartDTO(id, name, requestedQty, price, total));
            }
            calculateNetTotal();
            clearFields();

            txtId.setStyle("-fx-border-color: #C95F4D;");
            txtQty.setStyle("-fx-border-color: #C95F4D;");
        }catch(SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error").show();
            return;
        }
    }

    private void calculateNetTotal() {

        netTotal = cartList.stream().mapToDouble(CartDTO::getTotal).sum();
        lblTotalAmount.setText(String.format("Rs %.2f", netTotal));
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtQty.clear();
        txtUnitPrice.clear();
        txtId.requestFocus();
    }


    private void loadAllItemNames() {
        try {
            allItemNames = ItemModel.getAllItemNames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void txtNameOnAction(ActionEvent event) {
        try {
            ItemDTO item = ItemModel.searchItemByName(txtName.getText());
            fillItemData(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void fillItemData(ItemDTO item) {

        if (item != null) {
            txtId.setText(String.valueOf(item.getId()));
            txtName.setText(item.getName());
            txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
            txtQty.requestFocus();
        } else {
            new Alert(Alert.AlertType.WARNING, "Item not found").show();
        }
    }

    private void setupAutoSuggestion() {

        txtName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (newValue.isEmpty()) {
                    contextMenu.hide();
                    return;
                }

                List<String> suggestions = new ArrayList<>();

                for (String name : allItemNames) {
                    if (name.toLowerCase().startsWith(newValue.toLowerCase())) {
                        suggestions.add(name);
                    }
                }

                if (!suggestions.isEmpty()) {
                    contextMenu.getItems().clear();

                    for (String suggestion : suggestions) {
                        MenuItem item = new MenuItem(suggestion);

                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                txtName.setText(suggestion);
                                txtName.positionCaret(suggestion.length());
                                contextMenu.hide();
                                txtNameOnAction(null);
                            }
                        });

                        contextMenu.getItems().add(item);
                    }

                    if (!contextMenu.isShowing()) {
                        contextMenu.show(txtName, Side.BOTTOM, 0, 0);
                    }
                }else {
                    contextMenu.hide();
                }
            }
        });

        txtName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    contextMenu.hide();
                }
            }
        });
    }

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
    private void btnBackToDashboard(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }



}

