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
import lk.ijse.csms.dto.*;
import lk.ijse.csms.dto.tm.ItemCartTM;
import lk.ijse.csms.dto.tm.RepairCartTM;
import lk.ijse.csms.model.CustomerModel;
import lk.ijse.csms.model.ItemModel;
import lk.ijse.csms.model.RepairModel;
import lk.ijse.csms.model.TransactionModel;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class PlaceOrderFormController {

    public TextField txtType;
    @FXML
    public JFXButton btnItemUpdate;
    @FXML
    public JFXButton btnRepairUpdate;

    @FXML
    public JFXButton btnItemRemove;

    @FXML
    public JFXButton btnRepairRemove;
    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnItemAdd;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnRepairAdd;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private ComboBox<String> cmbItemDescription;

    @FXML
    private ComboBox<String> cmbRepairId;

    @FXML
    private TableColumn<ItemCartTM, String> colItemAction;

    @FXML
    private TableColumn<RepairCartTM, String> colRepairActon;

    @FXML
    private TableColumn<ItemCartTM, String> colItemCode;

    @FXML
    private TableColumn<ItemCartTM, String> colItemDescription;

    @FXML
    private TableColumn<ItemCartTM, Integer> colItemQty;

    @FXML
    private TableColumn<ItemCartTM, Double> colItemTotal;

    @FXML
    private TableColumn<ItemCartTM, Double> colItemUnitPrice;

    @FXML
    private TableColumn<RepairCartTM, String> colRepairDescription;

    @FXML
    private TableColumn<RepairCartTM, String> colRepairId;

    @FXML
    private TableColumn<RepairCartTM, Double> colRepairPrice;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<ItemCartTM> tblItem;

    @FXML
    private TableView<RepairCartTM> tblRepair;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtQtyOnStock;

    @FXML
    private TextField txtRepairDescription;

    @FXML
    private TextField txtRepairPrice;

    @FXML
    private TextField txtTransactionId;

    @FXML
    private TextField txtUnitPrice;

    private final ObservableList<ItemCartTM> itemTmObservableList = FXCollections.observableArrayList();
    private final ObservableList<RepairCartTM> repairTmObservableList = FXCollections.observableArrayList();
    double netTotal;
    boolean isValidQuantity;
    boolean isValidPrice;
    boolean isItemUpdate;
    boolean isRepairUpdate;

    public void initialize(){
        btnItemAdd.setDisable(true);
        btnItemUpdate.setDisable(true);
        btnItemRemove.setDisable(true);

        isItemUpdate =false;
        btnItemUpdate.setVisible(false);
        btnItemRemove.setVisible(false);

        btnRepairAdd.setDisable(true);
        btnRepairUpdate.setDisable(true);
        btnRepairRemove.setDisable(true);

        isRepairUpdate=false;
        btnRepairUpdate.setVisible(false);
        btnRepairRemove.setVisible(false);

        txtQty.setDisable(true);
        txtRepairPrice.setDisable(true);
        try {
            setCellFactory();
            loadCmb();
            reset();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
    }

    private void setCellFactory() {
        //item table
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colItemAction.setCellValueFactory(new PropertyValueFactory<>("edit"));

        //repair table
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        colRepairDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRepairPrice.setCellValueFactory(new PropertyValueFactory<>("repairPrice"));
        colRepairActon.setCellValueFactory(new PropertyValueFactory<>("edit"));
    }

    private void reset() throws SQLException, ClassNotFoundException {
        txtTransactionId.setText(TransactionModel.getNextTransactionId());
        cmbCustomerId.setValue(null);
        txtCustomerName.clear();
        btnPlaceOrder.setDisable(true);
    }

    private void loadCmb() throws SQLException, ClassNotFoundException {
        cmbCustomerId.getItems().clear();
        cmbItemCode.getItems().clear();
        cmbItemDescription.getItems().clear();
        cmbRepairId.getItems().clear();
        List<Customer> customers= CustomerModel.getAllCustomer();
        for (Customer customer : customers){
            cmbCustomerId.getItems().add(customer.getCustomerId());
        }
        List<Item> items= ItemModel.getAllItem();
        for (Item item:items){
            cmbItemCode.getItems().add(item.getItemCode());
            cmbItemDescription.getItems().add(item.getDescription());
        }
        List<Repair> repairList= RepairModel.getAllNonReturnRepair();
        for (Repair repair:repairList){
            cmbRepairId.getItems().add(repair.getRepairId());
        }
    }

    @FXML
    void PlaceOrderOnAction(ActionEvent event) {
        Optional<ButtonType> btnResult = new Alert(Alert.AlertType.CONFIRMATION, "Total cost : "+NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal)+"\nAre you confirm ? ").showAndWait();
        if (btnResult.get() == ButtonType.OK) {
            String transactionId = txtTransactionId.getText();
            String customerId = String.valueOf(cmbCustomerId.getValue());
            List<ItemTransactionDetails> itemDetails = new ArrayList<>();
            List<RepairTransactionDetails> repairDetails = new ArrayList<>();
            for (int i = 0; i < tblItem.getItems().size(); i++) {
                ItemCartTM itemTm = itemTmObservableList.get(i);
                itemDetails.add(new ItemTransactionDetails(transactionId, itemTm.getItemCode(), itemTm.getQuantity(), itemTm.getUnitPrice()));
            }
            for (int i = 0; i < tblRepair.getItems().size(); i++) {
                RepairCartTM repairTm = repairTmObservableList.get(i);
                repairDetails.add(new RepairTransactionDetails(transactionId, repairTm.getRepairId(), repairTm.getRepairPrice()));
            }
            String type=itemDetails.size()>0 && repairDetails.size()>0?"items/repair":itemDetails.size()>0?"items":repairDetails.size()>0?"repair":"";
            Transaction transaction = new Transaction(transactionId, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), customerId, type, itemDetails,repairDetails);
            try {
                if (TransactionModel.addTransaction(transaction)) {
                    loadCmb();
                    reset();
                    resetTableOnAction(event);

                    cmbItemCode.setValue(null);
                    cmbItemDescription.setValue(null);
                    cmbRepairId.setValue(null);

                    txtType.clear();
                    txtUnitPrice.clear();
                    txtQtyOnStock.clear();
                    txtQty.clear();
                    txtRepairDescription.clear();
                    txtRepairPrice.clear();
                    txtQty.setDisable(true);
                    txtRepairPrice.setDisable(true);

                    btnItemAdd.setDisable(true);
                    btnRepairAdd.setDisable(true);

                    lblTotal.setText("Rs.0.00");
                    netTotal = 0.00;

                    new Alert(Alert.AlertType.INFORMATION,  "Payment added...!").show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "Payment adding fail..!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void repairQtyKeyReleasedOnAction(KeyEvent event) {
        if ((!txtRepairPrice.getText().isEmpty()) && Pattern.compile("^(\\d+)||((\\d+\\.)(\\d){2})$").matcher(txtRepairPrice.getText()).matches()){
            txtRepairPrice.setStyle("-fx-border-color: #76ff03");
            isValidPrice =true;

            btnRepairAdd.setDisable(false);
            btnRepairUpdate.setDisable(false);
        }else {
            txtRepairPrice.setStyle("-fx-border-color: #ff0000");
            isValidPrice =false;

            btnRepairAdd.setDisable(true);
            btnRepairUpdate.setDisable(true);
        }
    }

    @FXML
    void cmbCustomerIdOnAction(ActionEvent event) {
        try {
            String cusId=cmbCustomerId.getValue();
            String name=CustomerModel.searchCustomerName(cusId);
            txtCustomerName.setText(name);
            if ((!itemTmObservableList.isEmpty()) || (!repairTmObservableList.isEmpty()))
                btnPlaceOrder.setDisable(false);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void cmbItemCodeOnAction(ActionEvent event) {
        try {
            Item item=ItemModel.search(cmbItemCode.getSelectionModel().getSelectedItem());
            cmbItemDescription.setValue(item.getDescription());
            itemPane(item);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }catch (NullPointerException e){
            txtQtyOnStock.setText(null);
            txtType.setText(null);
            txtUnitPrice.setText(null);
        }
        btnItemAdd.setDisable(true);
        btnItemUpdate.setDisable(true);
        btnItemRemove.setDisable(true);

        btnItemAdd.setVisible(true);
        btnItemUpdate.setVisible(false);
        btnItemRemove.setVisible(false);

        txtQty.setDisable(false);
        txtQty.clear();
        txtQty.requestFocus();
    }
    private void itemPane(Item item) {
        int qtyOnStock=item.getQtyOnStock();
        if (!isItemUpdate){
            if (!tblItem.getItems().isEmpty()) {
                for (int i = 0; i < tblItem.getItems().size(); i++) {
                    if (colItemCode.getCellData(i).equals(item.getItemCode())) {
                        qtyOnStock -= colItemQty.getCellData(i);
                        break;
                    }

                }
            }
        }
        isItemUpdate =false;
        txtQtyOnStock.setText(String.valueOf(qtyOnStock));
        txtType.setText(item.getItemType());
        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
    }
    @FXML
    void cmbItemDescriptionOnAction(ActionEvent event) {
        try {
            Item item=ItemModel.search(cmbItemDescription.getSelectionModel().getSelectedItem());
            cmbItemCode.setValue(item.getItemCode());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void cmbRepairIdOnActon(ActionEvent event) {
        try {
            String repairId=cmbRepairId.getValue();
            String description=RepairModel.searchRepairId(repairId);
            txtRepairDescription.setText(description);

            btnRepairAdd.setDisable(true);
            btnRepairAdd.setVisible(true);
            btnRepairUpdate.setDisable(true);
            btnRepairUpdate.setVisible(false);
            btnRepairRemove.setDisable(true);
            btnRepairRemove.setVisible(false);


            txtRepairPrice.setDisable(false);
            txtRepairPrice.clear();
            txtRepairPrice.requestFocus();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    @FXML
    void resetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        reset();

    }
    @FXML
    void itemAddOnAction(ActionEvent event) {
        String code = String.valueOf(cmbItemCode.getValue());
        String description = String.valueOf(cmbItemDescription.getValue());
        String type=txtType.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        int qtyOnStock = Integer.parseInt(txtQtyOnStock.getText())-qty;
        txtQtyOnStock.setText(String.valueOf(qtyOnStock));
        double total = unitPrice * qty;
        netTotal +=total;
        txtQty.clear();
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        btnItemAdd.setDisable(true);
        if (!cmbCustomerId.getSelectionModel().isEmpty())
            btnPlaceOrder.setDisable(false);
        else
            btnPlaceOrder.setDisable(true);
        if (!itemTmObservableList.isEmpty()){
            for ( int i=0; i<tblItem.getItems().size(); i++){
                if (colItemCode.getCellData(i).equals(code)){
                    qty += colItemQty.getCellData(i);
                    total = unitPrice * qty;

                    itemTmObservableList.get(i).setQuantity(qty);
                    itemTmObservableList.get(i).setTotal(total);
                    tblItem.refresh();
                    return;
                }
            }
        }
        JFXButton edit = new JFXButton("📝");
        edit.setStyle("-fx-text-fill: #00E676;" +
                "-fx-background-color: rgba(255,0,0,0);"+
                "-fx-font-size: 15px"
        );
        edit.setMaxWidth(250);
        ItemCartTM itemCartTM = new ItemCartTM(code, description, qty, unitPrice, total, edit);
        itemTmObservableList.add(itemCartTM);
        edit.setOnMouseClicked(mouseEvent -> {
            tblItem.getSelectionModel().select(itemCartTM);
            fillItemFields(itemCartTM);
        });
        tblItem.setItems(itemTmObservableList);
    }

    private void fillItemFields(ItemCartTM itemCartTM) {
        cmbItemCode.setValue(null);
        isItemUpdate =true;
        cmbItemCode.setValue(itemCartTM.getItemCode());
        txtQty.setText(String.valueOf(itemCartTM.getQuantity()));

        btnItemRemove.setDisable(false);
        btnItemAdd.setVisible(false);
        btnItemUpdate.setVisible(true);
        btnItemRemove.setVisible(true);
    }

    @FXML
    void repairAddOnAction(ActionEvent event) {
        String id = String.valueOf(cmbRepairId.getValue());
        String description = String.valueOf(txtRepairDescription.getText());
        double repairPrice=Double.parseDouble(txtRepairPrice.getText());
        netTotal +=repairPrice;
        txtRepairPrice.clear();
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        btnRepairAdd.setDisable(true);
        if (!cmbCustomerId.getSelectionModel().isEmpty())
            btnPlaceOrder.setDisable(false);
        else
            btnPlaceOrder.setDisable(true);

        JFXButton edit = new JFXButton("📝");
        edit.setStyle("-fx-text-fill: #00E676;" +
                "-fx-background-color: rgba(255,0,0,0);"+
                "-fx-font-size: 15px"
        );
        edit.setMaxWidth(250);

        RepairCartTM repairCartTM = new RepairCartTM(id, description, repairPrice, edit);
        repairTmObservableList.add(repairCartTM);

        edit.setOnMouseClicked(mouseEvent -> {
            tblRepair.getSelectionModel().select(repairCartTM);
            fillRepairFields(repairCartTM);
        });

        tblRepair.setItems(repairTmObservableList);

        cmbRepairId.getItems().remove(id);
        cmbRepairId.setValue(null);
        txtRepairPrice.setDisable(true);
    }

    private void fillRepairFields(RepairCartTM repairCartTM) {
        isRepairUpdate=true;
        cmbRepairId.setValue(repairCartTM.getRepairId());
        txtRepairPrice.setText(String.valueOf(repairCartTM.getRepairPrice())+"0");
        btnRepairAdd.setVisible(false);
        btnRepairUpdate.setVisible(true);
        btnRepairRemove.setVisible(true);
        btnRepairRemove.setDisable(false);
    }

    @FXML
    void itemQtyKeyReleasedOnAction(KeyEvent event) {
        int qtyOnStock=Integer.parseInt(txtQtyOnStock.getText());
        int qty=txtQty.getText().isEmpty()?0:Integer.parseInt(txtQty.getText());
        if (txtQty.getText().isEmpty() || 0 > qtyOnStock-qty){
            btnItemAdd.setDisable(true);
            btnItemUpdate.setDisable(true);
            txtQty.setStyle("-fx-border-color: #ff0000");
            isValidQuantity =false;
        }else {
            btnItemAdd.setDisable(false);
            btnItemUpdate.setDisable(false);
            txtQty.setStyle("-fx-border-color: #76ff03");
            isValidQuantity =true;
        }
    }

    @FXML
    void qtyKeyTypedOnAction(KeyEvent event) {
        if (!event.getCharacter().matches("[\\d]")) {
            event.consume();
        }
    }

    public void priceKeyTypedOnAction(KeyEvent keyEvent) {
        if (!keyEvent.getCharacter().matches("[\\d\\.]")) {
            keyEvent.consume();
        }
    }



    @FXML
    void resetTableOnAction(ActionEvent event) {
        itemTmObservableList.clear();
        tblItem.refresh();
        repairTmObservableList.clear();
        tblRepair.refresh();
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        if (isValidQuantity) {
            if (isItemUpdate)
                itemUpdateOnAction(event);
            else
                itemAddOnAction(event);
        }
    }

    @FXML
    void txtRepairPriceOnAction(ActionEvent event) {
        if (isValidPrice) {
            if (isRepairUpdate)
                repairUpdateOnAction(event);
            else
                repairAddOnAction(event);
        }
    }

    public void itemUpdateOnAction(ActionEvent actionEvent) {
        double unitPrice=Double.parseDouble(txtUnitPrice.getText());
        ItemCartTM tm=tblItem.getSelectionModel().getSelectedItem();
        int qty=tm.getQuantity();
        netTotal-=qty*unitPrice;
        qty=Integer.parseInt(txtQty.getText());
        tm.setQuantity(qty);
        tm.setTotal(qty*unitPrice);
        netTotal+=qty*unitPrice;
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        tblItem.refresh();
        isItemUpdate=false;
        btnItemAdd.setVisible(true);
        btnItemUpdate.setVisible(false);
        btnItemRemove.setVisible(false);
        tblItem.getSelectionModel().clearSelection();
    }

    public void repairUpdateOnAction(ActionEvent actionEvent) {
        isRepairUpdate=false;
        RepairCartTM tm=tblRepair.getSelectionModel().getSelectedItem();
        double price=tm.getRepairPrice();
        netTotal-=price;
        price=Double.parseDouble(txtRepairPrice.getText());
        netTotal+=price;
        tm.setRepairPrice(price);
        tblRepair.refresh();
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        cmbRepairId.setValue(null);
        txtRepairPrice.setDisable(true);
    }

    public void resetItem(ActionEvent actionEvent) {
        cmbItemCode.setValue(null);
        cmbItemDescription.setValue(null);
        txtQty.setDisable(true);
    }

    public void resetRepair(ActionEvent actionEvent) {
        cmbRepairId.setValue(null);
        txtRepairPrice.setDisable(true);
    }

    @FXML
    public void itemRemoveOnAction(ActionEvent actionEvent) {
        ItemCartTM itemTm =tblItem.getSelectionModel().getSelectedItem();
        double total= itemTm.getTotal();
        netTotal-=total;
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        tblItem.getItems().removeAll(tblItem.getSelectionModel().getSelectedItem());
        cmbItemCode.setValue(null);
        if (repairTmObservableList.isEmpty() && itemTmObservableList.isEmpty()){
            btnPlaceOrder.setDisable(true);
        }

    }

    @FXML
    public void repairRemoveOnAction(ActionEvent actionEvent) {
        RepairCartTM repairTm =tblRepair.getSelectionModel().getSelectedItem();
        double total= repairTm.getRepairPrice();
        netTotal-=total;
        lblTotal.setText(NumberFormat.getCurrencyInstance(new Locale("en","in")).format(netTotal));
        tblRepair.getItems().removeAll(repairTm);
        cmbRepairId.getItems().add(repairTm.getRepairId());
        cmbRepairId.setValue(null);
        if (repairTmObservableList.isEmpty() && itemTmObservableList.isEmpty()){
            btnPlaceOrder.setDisable(true);
        }
    }
}
