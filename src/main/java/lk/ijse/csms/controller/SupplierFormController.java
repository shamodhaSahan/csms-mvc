package lk.ijse.csms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lk.ijse.csms.dto.Supplier;
import lk.ijse.csms.dto.tm.SupplierTM;
import lk.ijse.csms.model.SupplierModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SupplierFormController {

    @FXML
    public JFXButton btnDelete;
    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<SupplierTM, JFXButton> colAction;

    @FXML
    private TableColumn<SupplierTM, String> colAddress;

    @FXML
    private TableColumn<SupplierTM, String> colEmail;

    @FXML
    private TableColumn<SupplierTM, String> colId;

    @FXML
    private TableColumn<SupplierTM, String> colName;

    @FXML
    private TableColumn<SupplierTM, String> colTelNumber;

    @FXML
    private TableView<SupplierTM> tbl;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTelNumber;
    private final ObservableList<SupplierTM> supplierObservableList = FXCollections.observableArrayList();
    private boolean isValidName=false;
    private boolean isValidAddress=false;
    private boolean isValidTelephoneNumber=false;
    private boolean isValidEmail=false;
    private boolean isUpdate=false;
    public void initialize(){
        setCellFactory();
        reset();
    }

    private void setCellFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTelNumber.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("edit"));
    }

    private void refreshTable() {
        supplierObservableList.clear();
        try {
            for (Supplier supplier : SupplierModel.getAllSupplier()){
                JFXButton edit = new JFXButton("📝");
                edit.setStyle("-fx-text-fill: #00E676;" +
                        "-fx-background-color: rgba(255,0,0,0);"+
                        "-fx-font-size: 15px"
                );
                edit.setMaxWidth(250);
                SupplierTM supplierTM = new SupplierTM(supplier.getSupplierId(), supplier.getName(), supplier.getAddress(), supplier.getTelephoneNumber(), supplier.getEmail(), edit);
                supplierObservableList.add(supplierTM);

                edit.setOnMouseClicked(mouseEvent -> {
                    tbl.getSelectionModel().select(supplierTM);
                    btnUpdate.setDisable(false);
                    btnDelete.setDisable(false);
                    btnAdd.setDisable(true);
                    isUpdate=true;
                    fillCustomerFields(supplierTM);
                });
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        tbl.setItems(supplierObservableList);
    }

    private void fillCustomerFields(SupplierTM supplierTM) {
        txtId.setText(supplierTM.getSupplierId());
        txtName.setText(supplierTM.getName());
        txtAddress.setText(supplierTM.getAddress());
        txtTelNumber.setText(supplierTM.getTelephoneNumber());
        txtEmail.setText(supplierTM.getEmail());
    }

    private void reset() {
        try {
            txtId.setText(SupplierModel.getNextSupplierId());
            txtName.clear();
            txtAddress.clear();
            txtTelNumber.clear();
            txtEmail.clear();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        refreshTable();
        isUpdate = false;
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.setDisable(false);
    }

    @FXML
    void addOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidAddress)
            txtAddress.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else {
            String supplierId=txtId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            try {
                String existsSupplierId=SupplierModel.searchName(name);
                if (existsSupplierId==null){
                    if (SupplierModel.addSupplier(new Supplier(supplierId,name,address,telNumber,email))){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION, "supplier data added...!").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "Data not added...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "This name is the same as as another supplier's name,supplier id "+existsSupplierId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void validateAll() {
        nameValidation();
        addressValidation();
        telephoneNumberValidation();
        emailValidation();
    }
    private void nameValidation(){
        if (Pattern.compile("^[a-zA-Z\\s]{3,50}$").matcher(txtName.getText()).matches()){
            txtName.setStyle("-fx-border-color: #76ff03");
            isValidName=true;
        }else {
            txtName.setStyle("-fx-border-color: #ff0000");
            isValidName=false;
        }
    }
    private void addressValidation(){
        if (!txtAddress.equals("")){
            txtAddress.setStyle("-fx-border-color: #76ff03");
            isValidAddress=true;
        }else {
            txtAddress.setStyle("-fx-border-color: #ff0000");
            isValidAddress=false;
        }
    }
    private void telephoneNumberValidation() {
        if (Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$").matcher(txtTelNumber.getText()).matches()){
            txtTelNumber.setStyle("-fx-border-color: #76ff03");
            isValidTelephoneNumber=true;
        }else {
            txtTelNumber.setStyle("-fx-border-color: #ff0000");
            isValidTelephoneNumber=false;
        }
    }

    private void emailValidation(){
        if (Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$").matcher(txtEmail.getText()).matches()){
            txtEmail.setStyle("-fx-border-color: #76ff03");
            isValidEmail=true;
        }else {
            txtEmail.setStyle("-fx-border-color: #ff0000");
            isValidEmail=false;
        }
    }

    @FXML
    void addressKeyPressedOnAction(KeyEvent event) {
        addressValidation();
    }

    @FXML
    void addressOnAction(ActionEvent event) {
        if (isValidAddress)
            txtTelNumber.requestFocus();
    }

    @FXML
    void emailKeyPressedOnAction(KeyEvent event) {
        emailValidation();
    }

    @FXML
    void emailOnAction(ActionEvent event) {
        if (isValidEmail) {
            if (isUpdate){
                updateOnAction(event);
            }else {
                addOnAction(event);
            }
        }
    }

    @FXML
    void nameKeyPressedOnAction(KeyEvent event) {
        nameValidation();
    }

    @FXML
    void nameOnAction(ActionEvent event) {
        if (isValidName)
            txtAddress.requestFocus();
    }

    @FXML
    void resetOnAction(ActionEvent event) {
        reset();
    }

    @FXML
    void telNumKeyPressedOnAction(KeyEvent event) {
        telephoneNumberValidation();
    }

    @FXML
    void telNumberOnAction(ActionEvent event) {
        if (isValidTelephoneNumber)
            txtEmail.requestFocus();
    }

    @FXML
    void txtSearchMouseOnClick(MouseEvent event) {

    }

    @FXML
    void updateOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidAddress)
            txtAddress.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else {
            String supplierId=txtId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            try {
                String existsSupplierId=SupplierModel.searchName(name);
                if (existsSupplierId==null || existsSupplierId.equals(supplierId)){
                    if (SupplierModel.updateSupplier(new Supplier(supplierId,name,address,telNumber,email))){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION, "supplier data updated.").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not updated...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "This name is the same as as another supplier's name,supplier id "+existsSupplierId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        SupplierTM supplierTM = tbl.getSelectionModel().getSelectedItem();
        ButtonType result = new Alert(Alert.AlertType.CONFIRMATION, "Deleting a supplier named "+supplierTM.getName()+"\nWhen the supplier is deleted,The supplies is also deleted\nAre You Sure ? ").showAndWait().get();
        if (result == ButtonType.OK){
            try {
                if (SupplierModel.deleteSupplier(supplierTM.getSupplierId())){
                    reset();
                    refreshTable();
                    new Alert(Alert.AlertType.INFORMATION, "Deleting Successfully\n"+supplierTM.getName()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }
}
