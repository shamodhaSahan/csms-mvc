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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.csms.dto.Customer;
import lk.ijse.csms.dto.tm.CustomerTM;
import lk.ijse.csms.model.CustomerModel;

import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerFormController {

    @FXML
    public JFXButton btnDelete;
    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<CustomerTM, JFXButton> colAction;

    @FXML
    private TableColumn<CustomerTM, String> colAddress;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerId;

    @FXML
    private TableColumn<CustomerTM, String> colEmail;

    @FXML
    private TableColumn<CustomerTM, String> colName;

    @FXML
    private TableColumn<CustomerTM, String> colNic;

    @FXML
    private TableColumn<CustomerTM, String> colTelNumber;

    @FXML
    private TableView<CustomerTM>tblCustomer;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCusId;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtTelNumber;
    ObservableList<CustomerTM> customerObservableList = FXCollections.observableArrayList();
    Customer customer=null;
    boolean isValidName=false;
    boolean isValidAddress=false;
    boolean isValidNic=false;
    boolean isValidTelephoneNumber=false;
    boolean isValidEmail=false;
    boolean isUpdate=false;
    public void initialize(){
        setCellValueFactory();
        reset();
    }

    private void reset(){
        try {
            txtCusId.setText(CustomerModel.getNextCustomerId());
            txtName.setText("");
            txtAddress.setText("");
            txtNic.setText("");
            txtTelNumber.setText("");
            txtEmail.setText("");

            btnAdd.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            isUpdate = false;
            refreshTable();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    @FXML
    public void txtSearchMouseOnClick(MouseEvent mouseEvent) {

    }

    @FXML
    void resetOnAction(ActionEvent event) {
        reset();
    }
    @FXML
    public void addOnAction(ActionEvent actionEvent) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidAddress)
            txtAddress.requestFocus();
        else if (!isValidNic)
            txtNic.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else {
            String cusId=txtCusId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            try {
                String existsCusId= CustomerModel.existsCustomer(nic);
                if (existsCusId==null){
                    customer = new Customer(cusId,name,address,nic,telNumber,email);
                    if (CustomerModel.addCustomer(customer)){
                        reset();
                        txtName.requestFocus();
                        new Alert(Alert.AlertType.INFORMATION,"customer data added").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"data not added...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING,"This nic is the same as as another customer's nic,customer id "+existsCusId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.INFORMATION,"DataBase Error\n" + e.getMessage()).show();
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidAddress)
            txtAddress.requestFocus();
        else if (!isValidNic)
            txtNic.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else {
            String cusId=txtCusId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            try {
                String existsCusId=CustomerModel.existsCustomer(nic);
                if (existsCusId==null || existsCusId.equals(cusId)){
                    customer = new Customer(cusId,name,address,nic,telNumber,email);
                    if (CustomerModel.updateCustomer(customer)){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION,"customer data updated.").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"data not updated...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "This nic is the same as as another customer's nic,customer id "+existsCusId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

    @FXML
    void telNumberOnAction(ActionEvent event) {
        if (isValidTelephoneNumber)
            txtEmail.requestFocus();
    }

    @FXML
    public void nameOnAction(ActionEvent actionEvent) {
        if (isValidName)
            txtAddress.requestFocus();
    }

    @FXML
    public void addressOnAction(ActionEvent actionEvent) {
        if (isValidAddress)
            txtNic.requestFocus();
    }

    @FXML
    public void nicOnAction(ActionEvent actionEvent) {
        if (isValidNic)
            txtTelNumber.requestFocus();
    }
    @FXML
    public void emailOnAction(ActionEvent actionEvent) {
        if (isValidEmail) {
            if (isUpdate){
                updateOnAction(actionEvent);
            }else {
                addOnAction(actionEvent);
            }
        }
    }
    @FXML
    void telNumKeyPressedOnAction(KeyEvent event) {
        telephoneNumberValidation();
    }
    @FXML
    public void nameKeyPressedOnAction(KeyEvent keyEvent) {
        nameValidation();
    }
    @FXML
    public void addressKeyPressedOnAction(KeyEvent keyEvent) {
        addressValidation();
    }
    @FXML
    public void nicKeyPressedOnAction(KeyEvent keyEvent) {
        nicValidation();
    }
    @FXML
    public void emailKeyPressedOnAction(KeyEvent keyEvent) {
        emailValidation();
    }

    private void refreshTable() {
        customerObservableList.clear();
        try {
            for (Customer customer :  CustomerModel.getAllCustomer()){
                JFXButton edit = new JFXButton("📝");
                edit.setStyle("-fx-text-fill: #00E676;" +
                        "-fx-background-color: rgba(255,0,0,0);"+
                        "-fx-font-size: 15px"
                );
                edit.setMaxWidth(250);

                CustomerTM customerTM = new CustomerTM(customer.getCustomerId(), customer.getName(), customer.getAddress(), customer.getNic(), customer.getTelephoneNumber(), customer.getEmail(), edit);
                customerObservableList.add(customerTM);

                edit.setOnMouseClicked(mouseEvent -> {
                    tblCustomer.getSelectionModel().select(customerTM);
                    fillCustomerFields(customerTM);
                });
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e + ",Data Not Loading !");
        }
        tblCustomer.setItems(customerObservableList);
    }

    private void fillCustomerFields(CustomerTM customerTM) {
        isUpdate = true;
        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

        txtCusId.setText(customerTM.getCustomerId());
        txtName.setText(customerTM.getName());
        txtAddress.setText(customerTM.getAddress());
        txtNic.setText(customerTM.getNic());
        txtTelNumber.setText(customerTM.getTelephoneNumber());
        txtEmail.setText(customerTM.getEmail());
    }

    private void setCellValueFactory() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colTelNumber.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("edit"));
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
    private void nicValidation(){
        if (Pattern.compile("^[0-9]{9}[vVxX]||[0-9]{12}$").matcher(txtNic.getText()).matches()){
            txtNic.setStyle("-fx-border-color: #76ff03");
            isValidNic=true;
        }else {
            txtNic.setStyle("-fx-border-color: #ff0000");
            isValidNic=false;
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

    private void validateAll() {
        nameValidation();
        addressValidation();
        nicValidation();
        telephoneNumberValidation();
        emailValidation();
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Deleting a customer named " + customerTM.getName() + "\nWhen the customer is deleted,The order is also deleted\nAre You Sure ? ").showAndWait();
        if (buttonType.get()==ButtonType.OK){
            try {
                if (CustomerModel.deleteCustomer(customerTM.getCustomerId())){
                    reset();
                    new Alert(Alert.AlertType.INFORMATION, "Deleting Successfully\n"+customerTM.getName()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
}
