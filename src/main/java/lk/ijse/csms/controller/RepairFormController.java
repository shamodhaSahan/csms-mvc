package lk.ijse.csms.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lk.ijse.csms.dto.Repair;
import lk.ijse.csms.dto.RepairTransactionDetails;
import lk.ijse.csms.dto.tm.RepairTM;
import lk.ijse.csms.dto.tm.ReturnedRepairTM;
import lk.ijse.csms.model.CustomerModel;
import lk.ijse.csms.model.RepairModel;
import lk.ijse.csms.model.RepairTransactionDetailsModel;
import lk.ijse.csms.model.TransactionModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepairFormController {

    @FXML
    public HBox hbState;
    @FXML
    public JFXButton btnDelete;
    @FXML
    private AnchorPane ancRepair;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbCustomerName;

    @FXML
    private ComboBox<String> cmbState;

    @FXML
    private TableColumn<RepairTM, String> colAction;

    @FXML
    private TableColumn<RepairTM, String> colCustomerId;

    @FXML
    private TableColumn<RepairTM, String> colDescription;

    @FXML
    private TableColumn<RepairTM, LocalDate> colReceiveDate;

    @FXML
    private TableColumn<RepairTM, String> colRepairId;

    @FXML
    private TableColumn<RepairTM, LocalDate> colReturnDate;

    @FXML
    private TableColumn<RepairTM, String> colState;
    @FXML
    private TableColumn<ReturnedRepairTM, String> colReturnedCustomerId;

    @FXML
    private TableColumn<ReturnedRepairTM, String> colReturnedDescription;

    @FXML
    private TableColumn<ReturnedRepairTM, Double> colReturnedPrice;

    @FXML
    private TableColumn<ReturnedRepairTM, LocalDate> colReturnedReceiveDate;

    @FXML
    private TableColumn<ReturnedRepairTM, String> colReturnedRepairId;

    @FXML
    private TableColumn<ReturnedRepairTM, String> colReturnedTransactionId;


    @FXML
    private TableColumn<ReturnedRepairTM, LocalDate> colTrueReturnDate;

    @FXML
    private DatePicker dpReceiveDate;

    @FXML
    private DatePicker dpReturnDate;

    @FXML
    private TableView<RepairTM> tblRepair;

    @FXML
    private TableView<ReturnedRepairTM> tblReturnedRepair;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtRepairId;
    private final ObservableList<RepairTM> repairObservableList= FXCollections.observableArrayList();
    private final ObservableList<ReturnedRepairTM> returnRepairTmObservableList= FXCollections.observableArrayList();
    private RepairTM repair=null;
    public void initialize(){
        try {
            setCellFactory();
            loadCmb();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        reset();
    }

    private void refreshTable() throws SQLException, ClassNotFoundException {
        repairObservableList.clear();
        for (Repair repair : RepairModel.getAllNonReturnRepair()){
            JFXButton edit = new JFXButton("📝");
            edit.setStyle("-fx-text-fill: #00E676;" +
                    "-fx-background-color: rgba(255,0,0,0);"+
                    "-fx-font-size: 15px"
            );
            edit.setMaxWidth(250);
            RepairTM repairTM  = new RepairTM(repair.getRepairId(), repair.getReceiveDate(), repair.getReturnDate(), repair.getStatus(), repair.getDescription(), repair.getCustomerId(), edit);
            repairObservableList.add(repairTM);
            edit.setOnMouseClicked(mouseEvent -> {
                tblRepair.getSelectionModel().select(repairTM);
                fillRepairFields(repairTM);
                btnUpdate.setVisible(true);
                btnDelete.setVisible(true);
                btnAdd.setVisible(false);
                hbState.setVisible(true);
            });
        }
        tblRepair.setItems(repairObservableList);
    }

    private void fillRepairFields(RepairTM repairTM) {
        txtRepairId.setText(repairTM.getRepairId());
        cmbCustomerId.setValue(repairTM.getCustomerId());
        dpReceiveDate.setValue(repairTM.getReceiveDate());
        dpReturnDate.setValue(repairTM.getReturnDate());
        txtDescription.setText(repairTM.getDescription());
        cmbState.setValue(repairTM.getState());
    }

    private void setCellFactory() throws SQLException, ClassNotFoundException {
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        colReceiveDate.setCellValueFactory(new PropertyValueFactory<>("receiveDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("edit"));

        colReturnedRepairId.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        colReturnedTransactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colReturnedDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colReturnedCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colReturnedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colReturnedReceiveDate.setCellValueFactory(new PropertyValueFactory<>("receiveDate"));
        colTrueReturnDate.setCellValueFactory(new PropertyValueFactory<>("trueReturnDate"));
        for (Repair repair : RepairModel.getAllReturnedRepair()){
            RepairTransactionDetails rtd = RepairTransactionDetailsModel.searchRepairId(repair.getRepairId());
            LocalDate trueReturnDate = TransactionModel.getDate(rtd.getTransactionId());
            returnRepairTmObservableList.add(new ReturnedRepairTM(
                    repair.getRepairId(),
                    rtd.getTransactionId(),
                    repair.getDescription(),
                    repair.getCustomerId(),
                    rtd.getRepairPrice(),
                    repair.getReceiveDate(),
                    trueReturnDate
            ));
        }
        tblReturnedRepair.setItems(returnRepairTmObservableList);
    }

    private void loadCmb() throws SQLException, ClassNotFoundException {
        String[] state={"repairing","repaired"};
        List<String> customerIdList = CustomerModel.getAllCustomerId();
        List<String>customerNameList = CustomerModel.getAllCustomerName();
        
        cmbState.getItems().addAll(state);
        cmbCustomerId.getItems().addAll(customerIdList);
        cmbCustomerName.getItems().addAll(customerNameList);
    }

    @FXML
    void addOnAction(ActionEvent event) {
        boolean isReady = true; 
        if (cmbCustomerId.getSelectionModel().isEmpty()) {
            isReady = false;
            cmbCustomerId.setStyle("-fx-border-color: #ff0000");
            cmbCustomerName.setStyle("-fx-border-color: #ff0000");
        }
        if (dpReceiveDate.getValue()==null){
            isReady = false;
            dpReceiveDate.setStyle("-fx-border-color: #ff0000");
        }
        if (dpReturnDate.getValue()==null) {
            isReady = false;
            dpReturnDate.setStyle("-fx-border-color: #ff0000");
        }
        if (txtDescription.getText()==null) {
            isReady = false;
            txtDescription.setStyle("-fx-border-color: #ff0000");
        } 
        if (!isReady)return;
        String repairId=txtRepairId.getText();
        LocalDate receiveDate=dpReceiveDate.getValue();
        LocalDate returnDate=dpReturnDate.getValue();
        String state="repairing";
        String description=txtDescription.getText();
        String customerId=cmbCustomerId.getSelectionModel().getSelectedItem();
        try {
            if (RepairModel.addRepair(new Repair(repairId,receiveDate,returnDate,state,description,customerId))){
                reset();
                new Alert(Alert.AlertType.INFORMATION, "Repair data added...!").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"data not added...!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void cmbCustomerIdOnAction(ActionEvent event) {
        try {
            cmbCustomerName.getSelectionModel().select(CustomerModel.searchCustomerName(cmbCustomerId.getSelectionModel().getSelectedItem()));
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }catch (NullPointerException ignored){}
        cmbCustomerId.setStyle("-fx-border-color: #76ff03");
        cmbCustomerName.setStyle("-fx-border-color: #76ff03");
    }

    @FXML
    void cmbCustomerNameOnAction(ActionEvent event) {
        try {
            cmbCustomerId.setValue(CustomerModel.searchCustomerId(cmbCustomerName.getSelectionModel().getSelectedItem()));
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    public void checkCustomerIsSelect(MouseEvent mouseEvent) {
        if (cmbCustomerId.getSelectionModel().isEmpty()){
            cmbCustomerId.setStyle("-fx-border-color: #ff0000");
            cmbCustomerName.setStyle("-fx-border-color: #ff0000");
        }
        if (dpReceiveDate.getValue() != null){
            dpReceiveDate.setStyle("-fx-border-color: #76ff03");
        }
        if (dpReturnDate.getValue() != null){
            dpReturnDate.setStyle("-fx-border-color: #76ff03");
        }
        if (txtDescription.getText() != null){
            txtDescription.setStyle("-fx-border-color: #76ff03");
        }
        if (!cmbState.getSelectionModel().isEmpty()){
            cmbState.setStyle("-fx-border-color: #76ff03");
        }
    }

    @FXML
    void resetOnAction(ActionEvent event) {
        reset();
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        boolean isReady = true;
        if (cmbCustomerId.getSelectionModel().isEmpty()) {
            isReady = false;
            cmbCustomerId.setStyle("-fx-border-color: #ff0000");
            cmbCustomerName.setStyle("-fx-border-color: #ff0000");
        }
        if (dpReceiveDate.getValue()==null){
            isReady = false;
            dpReceiveDate.setStyle("-fx-border-color: #ff0000");
        }
        if (dpReturnDate.getValue()==null) {
            isReady = false;
            dpReturnDate.setStyle("-fx-border-color: #ff0000");
        }
        if (txtDescription.getText()==null) {
            isReady = false;
            txtDescription.setText("-fx-border-color: #ff0000");
        }
        if (cmbState.getSelectionModel().isEmpty()) {
            isReady = false;
            cmbState.setStyle("-fx-border-color: #ff0000");
        }
        if (!isReady)return;
        String repairId=txtRepairId.getText();
        LocalDate receiveDate=dpReceiveDate.getValue();
        LocalDate returnDate=dpReturnDate.getValue();
        String state=cmbState.getSelectionModel().getSelectedItem();
        String description=txtDescription.getText();
        String customerId=cmbCustomerId.getSelectionModel().getSelectedItem();
        try {
            if (RepairModel.updateRepair(new Repair(repairId,receiveDate,returnDate,state,description,customerId))){
                reset();
                new Alert(Alert.AlertType.INFORMATION,"Repair data updated...!").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "data not updated...!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    private void reset(){
        try {
            txtRepairId.setText(RepairModel.getNextRepairId());
            btnDelete.setVisible(false);
            btnUpdate.setVisible(false);
            hbState.setVisible(false);
            btnAdd.setVisible(true);
            cmbCustomerId.getSelectionModel().clearSelection();
            cmbCustomerName.getSelectionModel().clearSelection();
            dpReceiveDate.setValue(null);
            dpReturnDate.setValue(null);
            txtDescription.setText(null);
            cmbState.getSelectionModel().clearSelection();
            refreshTable();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    public void dpReceiveDateOnAction(ActionEvent actionEvent) {
        dpReturnDate.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (dpReceiveDate.getValue()!=null) {
                    setDisable(item.isBefore(dpReceiveDate.getValue()));
                }
            }
        });
    }
    @FXML
    public void dpReturnDateOnAction(ActionEvent actionEvent) {
        dpReceiveDate.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (dpReturnDate.getValue()!=null) {
                    setDisable(item.isAfter(dpReturnDate.getValue()));
                }
            }
        });
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        RepairTM repair = tblRepair.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Deleting confirmation\n"+"Are You Sure ? ").showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if (RepairModel.deleteRepair(repair.getRepairId())){
                    reset();
                    new Alert(Alert.AlertType.INFORMATION, "Deleting Successfully\n"+repair.getRepairId()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }
}
