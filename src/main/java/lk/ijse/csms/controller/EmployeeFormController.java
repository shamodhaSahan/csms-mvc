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
import lk.ijse.csms.dto.Employee;
import lk.ijse.csms.dto.tm.EmployeeTM;
import lk.ijse.csms.model.CustomerModel;
import lk.ijse.csms.model.EmployeeModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmployeeFormController {

    @FXML
    public JFXButton btnDelete;
    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbRank;

    @FXML
    private TableColumn<EmployeeTM, JFXButton> colAction;

    @FXML
    private TableColumn<EmployeeTM, String> colAddress;

    @FXML
    private TableColumn<EmployeeTM, String> colEmployeeId;

    @FXML
    private TableColumn<EmployeeTM, String> colName;

    @FXML
    private TableColumn<EmployeeTM, String> colNic;

    @FXML
    private TableColumn<EmployeeTM, String> colRank;

    @FXML
    private TableColumn<EmployeeTM, String> colTelNumber;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTelNumber;

    ObservableList<EmployeeTM> employeeObservableList = FXCollections.observableArrayList();
    Employee employee=null;
    boolean isValidName=false;
    boolean isValidAddress=false;
    boolean isValidNic=false;
    boolean isValidTelephoneNumber=false;
    boolean isUpdate=false;
    public void initialize(){
        setCellValueFactory();
        loadCmb();
        reset();
    }

    private void reset(){
        try {
            txtEmployeeId.setText(EmployeeModel.getNextEmployeeId());
            txtName.setText("");
            txtAddress.setText("");
            txtNic.setText("");
            txtTelNumber.setText("");
            cmbRank.getSelectionModel().clearSelection();
            
            btnAdd.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            isUpdate = false;
            refreshTable();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void loadCmb() {
        String[] ranks={"Manager","Repairer","Technical Supporters","Cashier","Minor Employees"};
        cmbRank.getItems().addAll(ranks);
    }

    private void refreshTable() {
        employeeObservableList.clear();
        try {
            for (Employee employee : EmployeeModel.getAllEmployee()){
                JFXButton edit = new JFXButton("📝");
                edit.setStyle("-fx-text-fill: #00E676;" +
                        "-fx-background-color: rgba(255,0,0,0);"+
                        "-fx-font-size: 15px"
                );
                edit.setMaxWidth(250);
                
                EmployeeTM employeeTM = new EmployeeTM(employee.getEmployeeId(), employee.getName(), employee.getAddress(), employee.getNic(), employee.getTelephoneNumber(), employee.getRank(), edit);
                employeeObservableList.add(employeeTM);

                edit.setOnMouseClicked(mouseEvent -> {
                    tblEmployee.getSelectionModel().select(employeeTM);
                    fillCustomerFields(employeeTM);
                });
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
        tblEmployee.setItems(employeeObservableList);
    }

    private void fillCustomerFields(EmployeeTM employeeTM) {
        isUpdate = true;
        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

        txtEmployeeId.setText(employeeTM.getEmployeeId());
        txtName.setText(employeeTM.getName());
        txtAddress.setText(employeeTM.getAddress());
        txtNic.setText(employeeTM.getNic());
        txtTelNumber.setText(employeeTM.getTelephoneNumber());
        cmbRank.getSelectionModel().select(employeeTM.getRank());
    }

    private void setCellValueFactory() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colTelNumber.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
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


    @FXML
    void addOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidAddress)
            txtAddress.requestFocus();
        else if (!isValidNic)
            txtNic.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (cmbRank.getSelectionModel().isEmpty())
            cmbRank.setStyle("-fx-border-color: #ff0000");
        else {
            String employeeId=txtEmployeeId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String rank=cmbRank.getSelectionModel().getSelectedItem();
            try {
                String existsCusId=EmployeeModel.existsEmployee(nic);
                if (existsCusId==null){
                    employee = new Employee(employeeId,name,address,nic,telNumber,rank);
                    if (EmployeeModel.addEmployee(employee)){
                        reset();
                        txtName.requestFocus();
                        new Alert(Alert.AlertType.INFORMATION, "Employee data added.").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not added...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION,"This nic is the same as as another employee's nic,customer id "+existsCusId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e+"").show();
            }
        }
    }

    @FXML
    void addressKeyPressedOnAction(KeyEvent event) {
        addressValidation();
    }

    @FXML
    void addressOnAction(ActionEvent event) {
        if (isValidAddress)
            txtNic.requestFocus();
    }

    @FXML
    void cmbRankOnAction(ActionEvent event) {
        cmbRank.setStyle("-fx-border-color: #76ff03");
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
    void nicKeyPressedOnAction(KeyEvent event) {
        nicValidation();
    }

    @FXML
    void nicOnAction(ActionEvent event) {
        if (isValidNic)
            txtTelNumber.requestFocus();
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
    void txtSearchMouseOnClick(MouseEvent event) {

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
        else if (cmbRank.getSelectionModel().isEmpty())
            cmbRank.setStyle("-fx-border-color: #ff0000");
        else {
            String employeeId=txtEmployeeId.getText();
            String name=txtName.getText();
            String address=txtAddress.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String rank=cmbRank.getSelectionModel().getSelectedItem();
            try {
                String existsEmployeeId=EmployeeModel.existsEmployee(nic);
                if (existsEmployeeId==null || existsEmployeeId.equals(employeeId)){
                    employee = new Employee(employeeId,name,address,nic,telNumber,rank);
                    if (EmployeeModel.updateEmployee(employee)){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION, "Employee data updated.").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not updated...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "This nic is the same as as another employee's nic,Employee id "+existsEmployeeId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e+"").show();
            }
        }
    }

    private void validateAll() {
        nameValidation();
        addressValidation();
        nicValidation();
        telephoneNumberValidation();
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        EmployeeTM employeeTM=tblEmployee.getSelectionModel().getSelectedItem();
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Deleting a employee named " + employeeTM.getName() + "\nAre You Sure ? ").showAndWait();
        if (buttonType.get()==ButtonType.OK){
            try {
                if (EmployeeModel.deleteEmployee(employeeTM.getEmployeeId())){
                    reset();
                    new Alert(Alert.AlertType.INFORMATION, employeeTM.getName()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
            }
        }
    }
}
