package lk.ijse.csms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.csms.dto.Item;
import lk.ijse.csms.dto.Supplier;
import lk.ijse.csms.dto.Supplies;
import lk.ijse.csms.dto.SuppliesDetails;
import lk.ijse.csms.dto.tm.SuppliesCartTM;
import lk.ijse.csms.model.ItemModel;
import lk.ijse.csms.model.SupplierModel;
import lk.ijse.csms.model.SuppliesModel;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

public class SuppliesFormController {
    @FXML
    public Label lblTotal;

    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private ComboBox<String> cmbCode;

    @FXML
    private ComboBox<String> cmbDescription;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private ComboBox<String> cmbSupplierName;

    @FXML
    public TableColumn<SuppliesCartTM, String> colType;

    @FXML
    public TableColumn<SuppliesCartTM, String> colDescription;
    @FXML
    private TableColumn<SuppliesCartTM, JFXButton> colAction;

    @FXML
    private TableColumn<SuppliesCartTM, String> colCode;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colNewStockQty;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colOldStockQty;

    @FXML
    private TableColumn<SuppliesCartTM, Integer> colQty;
    @FXML
    public TableColumn<SuppliesCartTM, Double> colTotal;

    @FXML
    private TableColumn<SuppliesCartTM, Double> colUnitPrice;

    @FXML
    private TableView<SuppliesCartTM> tblSupplies;

    @FXML
    private TextField txtQty;

    @FXML
    public TextField txtType;

    @FXML
    public TextField txtQtyOnStock;

    @FXML
    private TextField txtSuppliesId;

    @FXML
    private TextField txtUnitPrice;
    private final ObservableList<SuppliesCartTM> tmObservableArray = FXCollections.observableArrayList();
    private double netTotal;
    private boolean isValidQuantity;
    public void initialize(){
        btnAdd.setDisable(true);
        txtQty.setDisable(true);
        try {
            loadTable();
            loadCmb();
            reset();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void loadCmb() throws SQLException, ClassNotFoundException {
        for (Supplier supplier : SupplierModel.getAllSupplier()){
            cmbSupplierId.getItems().add(supplier.getSupplierId());
            cmbSupplierName.getItems().add(supplier.getName());
        }
        for (Item item : ItemModel.getAllItem()){
            cmbCode.getItems().add(item.getItemCode());
            cmbDescription.getItems().add(item.getDescription());
        }
    }

    private void reset() throws SQLException, ClassNotFoundException {
        txtSuppliesId.setText(SuppliesModel.getNextSuppliesId());
        cmbSupplierId.setValue(null);
        cmbSupplierName.setValue(null);
        btnPlaceOrder.setDisable(true);
        resetTable();
    }

    private void loadTable() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOldStockQty.setCellValueFactory(new PropertyValueFactory<>("oldQuantity"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colNewStockQty.setCellValueFactory(new PropertyValueFactory<>("newQuantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    @FXML
    void PlaceOrderOnAction(ActionEvent event) {
        ButtonType result=new Alert(Alert.AlertType.CONFIRMATION, "Place Supplies\nTotal cost : "+NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal)+"\nAre you confirm ? ").showAndWait().get();
        if (result==ButtonType.OK) {
            String suppliesId = txtSuppliesId.getText();
            String supplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
            ArrayList<SuppliesDetails> suppliesDetails = new ArrayList<>();
            for (int i = 0; i < tblSupplies.getItems().size(); i++) {
                SuppliesCartTM tm = tmObservableArray.get(i);
                suppliesDetails.add(new SuppliesDetails(suppliesId, tm.getItemCode(), tm.getQuantity(), tm.getUnitPrice()));
            }
            Supplies supplies = new Supplies(suppliesId, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), supplierId, suppliesDetails);
            try {
                if (SuppliesModel.addSupplies(supplies)) {
                    reset();
                    cmbCode.setValue(null);
                    cmbDescription.setValue(null);
                    txtType.clear();
                    txtUnitPrice.clear();
                    txtQtyOnStock.clear();
                    txtQty.clear();
                    txtQty.setDisable(true);
                    btnAdd.setDisable(true);
                    lblTotal.setText("Rs.0.00");
                    netTotal = 0.00;
                    new Alert(Alert.AlertType.INFORMATION, "supplies added...!").show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "supplies adding fail...!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void addOnAction(ActionEvent event) {
        String code = String.valueOf(cmbCode.getValue());
        String description = String.valueOf(cmbDescription.getValue());
        String type=txtType.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int oldQty = Integer.parseInt(txtQtyOnStock.getText());
        int qty = Integer.parseInt(txtQty.getText());
        int newQty = qty + oldQty;
        double total = unitPrice * qty;
        netTotal +=total;
        txtQty.clear();
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        btnAdd.setDisable(true);
        if (!cmbSupplierId.getSelectionModel().isEmpty())
            btnPlaceOrder.setDisable(false);
        if (!tmObservableArray.isEmpty()){
            for ( int i=0; i<tblSupplies.getItems().size(); i++){
                if (colCode.getCellData(i).equals(code)){
                    qty += colQty.getCellData(i);
                    newQty += colNewStockQty.getCellData(i);
                    total = unitPrice * qty;

                    tmObservableArray.get(i).setQuantity(qty);
                    tmObservableArray.get(i).setNewQuantity(newQty);
                    tmObservableArray.get(i).setTotal(total);
                    tblSupplies.refresh();
                    return;
                }
            }
        }
        JFXButton delete = new JFXButton("📝");
        delete.setStyle("-fx-text-fill: #00E676;" +
                "-fx-background-color: rgba(255,0,0,0);"+
                "-fx-font-size: 15px"
        );
        delete.setMaxWidth(250);
        SuppliesCartTM suppliesCartTM = new SuppliesCartTM(code, description, type, qty, unitPrice, oldQty, newQty, total, delete);
        tmObservableArray.add(suppliesCartTM);
        delete.setOnMouseClicked(mouseEvent -> {
            tblSupplies.getSelectionModel().select(suppliesCartTM);
            ButtonType result = new Alert(Alert.AlertType.CONFIRMATION, "Removing item from cart\nAre You Sure ?").showAndWait().get();
            if (result == ButtonType.OK){
                netTotal -= suppliesCartTM.getTotal();
                lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
                tblSupplies.getItems().removeAll(suppliesCartTM);
                if (tmObservableArray.isEmpty()){
                    btnPlaceOrder.setDisable(true);
                }
            }
        });
        tblSupplies.setItems(tmObservableArray);
    }

    @FXML
    void cmbCodeOnAction(ActionEvent event) {
        try {
            Item item=ItemModel.search(cmbCode.getSelectionModel().getSelectedItem());
            itemPane(item);
            cmbDescription.setValue(item.getDescription());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }catch (NullPointerException e){}
    }

    private void itemPane(Item item) {
        txtType.setText(item.getItemType());
        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        txtQtyOnStock.setText(String.valueOf(item.getQtyOnStock()));
        btnAdd.setDisable(true);
        txtQty.setDisable(false);
        txtQty.requestFocus();
    }

    @FXML
    void cmbDescriptionOnAction(ActionEvent event) {
        try {
            Item item=ItemModel.search(cmbDescription.getSelectionModel().getSelectedItem());
            cmbCode.setValue(item.getItemCode());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }catch (NullPointerException e){}
    }

    @FXML
    void cmbSupplierIdOnAction(ActionEvent event) {
        try {
            String supplierName= SupplierModel.searchId(cmbSupplierId.getSelectionModel().getSelectedItem());
            cmbSupplierName.getSelectionModel().select(supplierName);
            if (!tmObservableArray.isEmpty())
                btnPlaceOrder.setDisable(false);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }catch (NullPointerException e){}
    }

    @FXML
    void cmbSupplierNameOnAction(ActionEvent event) {
        try {
            String supplierId = SupplierModel.searchName(cmbSupplierName.getSelectionModel().getSelectedItem());
            cmbSupplierId.getSelectionModel().select(supplierId);
            if (!tmObservableArray.isEmpty())
                btnPlaceOrder.setDisable(false);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }catch (NullPointerException e){}
    }

    @FXML
    void resetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        reset();

    }
    @FXML
    public void resetTableOnAction(ActionEvent actionEvent) {
        resetTable();
    }

    private void resetTable() {
        tmObservableArray.clear();
        tblSupplies.refresh();
    }

    @FXML
    public void qtyKeyReleasedOnAction(KeyEvent keyEvent) {
        if (txtQty.getText().isEmpty()){
            btnAdd.setDisable(true);
            txtQty.setStyle("-fx-border-color: #ff0000");
            isValidQuantity =false;
        }else {
            btnAdd.setDisable(false);
            txtQty.setStyle("-fx-border-color: #76ff03");
            isValidQuantity =true;
        }
    }
    @FXML
    public void qtyKeyTypedOnAction(KeyEvent keyEvent) {
        if (!keyEvent.getCharacter().matches("\\d")) {
            keyEvent.consume();
        }
    }

    @FXML
    public void qtyOnAction(ActionEvent actionEvent) {
        if (isValidQuantity)
            addOnAction(actionEvent);
    }
}
