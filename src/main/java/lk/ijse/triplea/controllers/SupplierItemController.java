package lk.ijse.triplea.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.triplea.dto.SupplierItemDTO;
import lk.ijse.triplea.model.ItemModel;
import lk.ijse.triplea.model.SupplierItemModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierItemController implements Initializable {

    @FXML
    private TextField txtSupplierId;

    @FXML
    private ComboBox<String> cmbItems;

    @FXML
    private TextField txtCost;

    @FXML
    private TableView<SupplierItemDTO> tblSupplierItems;

    @FXML
    private TableColumn<SupplierItemDTO, Integer> colSupId;

    @FXML
    private TableColumn<SupplierItemDTO, String> colItemName;

    @FXML
    private TableColumn<SupplierItemDTO, Double> colCost;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colSupId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("costPrice"));

        loadAllItemsCombo();
        loadAllTableData();
    }

    private void loadAllTableData() {

        try {
            List<SupplierItemDTO> list = SupplierItemModel.getAllSupplierItems();
            tblSupplierItems.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void txtSupplierIdOnAction(ActionEvent event) {

        cmbItems.requestFocus();
        cmbItems.show();
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {

        if (isValidEntry()) {

            try {

                int supId = Integer.parseInt(txtSupplierId.getText());

                String itemName = cmbItems.getValue();

                int itemId = ItemModel.searchItemByName(itemName).getId();

                double cost = Double.parseDouble(txtCost.getText());

                SupplierItemDTO dto = new SupplierItemDTO(supId, itemId, itemName, cost);

                if (SupplierItemModel.saveSupplierItem(dto)) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();

                    loadAllTableData();

                    txtCost.clear();
                    cmbItems.getSelectionModel().clearSelection();
                }

            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Supplier ID and Cost must be numbers").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
        }
    }

    private void loadAllItemsCombo() {

        try {
            cmbItems.setItems(FXCollections.observableArrayList(ItemModel.getAllItemNames()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEntry() {

        return !txtSupplierId.getText().isEmpty()
                && cmbItems.getValue() != null
                && !txtCost.getText().isEmpty();
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
    private void btnBackOnAction(ActionEvent event) {
        navigateTo("/Dashboard.fxml", event);
    }
}