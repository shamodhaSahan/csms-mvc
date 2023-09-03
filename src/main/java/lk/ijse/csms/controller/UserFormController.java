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
import lk.ijse.csms.dto.User;
import lk.ijse.csms.dto.tm.UserTM;
import lk.ijse.csms.model.UserModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserFormController {
    @FXML
    public PasswordField psPassword;

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
    private TableColumn<UserTM, String> colAction;

    @FXML
    private TableColumn<UserTM, String> colEmail;

    @FXML
    private TableColumn<UserTM, String> colId;

    @FXML
    private TableColumn<UserTM, String> colName;

    @FXML
    private TableColumn<UserTM, String> colNic;

    @FXML
    private TableColumn<UserTM, String> colRank;

    @FXML
    private TableColumn<UserTM, String> colTelNumber;

    @FXML
    private TableView<UserTM> tbl;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTelNumber;

    private final ObservableList<UserTM> userObservableList = FXCollections.observableArrayList();
    private boolean isValidName=false;
    private boolean isValidPassword=false;
    private boolean isValidNic=false;
    private boolean isValidTelephoneNumber=false;
    private boolean isValidEmail=false;
    private boolean isUpdate=false;
    private boolean isPasswordHide = true;
    public void initialize(){
        loadCmb();
        setCellFactory();
        reset();
    }

    private void loadCmb() {
        String[]rank={"Admin","Cashier"};
        cmbRank.getItems().addAll(rank);
    }

    private void reset() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.setDisable(false);
        try {
            txtId.setText(UserModel.getNextUserId());
            txtName.clear();
            psPassword.clear();
            txtNic.clear();
            txtTelNumber.clear();
            txtEmail.clear();
            cmbRank.getSelectionModel().clearSelection();
            refreshTable();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void refreshTable() {
        userObservableList.clear();
        try {
            for (User user : UserModel.getAllUser()){
                JFXButton edit = new JFXButton("📝");
                edit.setStyle("-fx-text-fill: #00E676;" +
                        "-fx-background-color: rgba(255,0,0,0);"+
                        "-fx-font-size: 15px"
                );
                edit.setMaxWidth(250);
                UserTM userTM = new UserTM(user.getUserId(), user.getUserName(), user.getUserPassword(), user.getNic(), user.getTelephoneNumber(), user.getEmail(), user.getRank(), edit);
                userObservableList.add(userTM);
                edit.setOnMouseClicked(mouseEvent -> {
                    tbl.getSelectionModel().select(userTM);
                    txtId.setText(userTM.getUserId());
                    txtName.setText(userTM.getUserName());
                    psPassword.setText(userTM.getUserPassword());
                    txtNic.setText(userTM.getNic());
                    txtTelNumber.setText(userTM.getTelephoneNumber());
                    txtEmail.setText(userTM.getEmail());
                    cmbRank.getSelectionModel().select(userTM.getRank());
                    btnUpdate.setDisable(false);
                    btnDelete.setDisable(false);
                    btnAdd.setDisable(true);
                    isUpdate = true;
                    if (userTM.getUserId().equals("U001") || userTM.getUserId().equals("U002")){
                        btnDelete.setDisable(true);
                    }
                });
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        tbl.setItems(userObservableList);
    }

    private void setCellFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colTelNumber.setCellValueFactory(new PropertyValueFactory<>("telephoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("edit"));
    }

    @FXML
    void addOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidPassword)
            psPassword.requestFocus();
        else if (!isValidNic)
            txtNic.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else if (cmbRank.getSelectionModel().isEmpty()) {
            cmbRank.setStyle("-fx-border-color: #ff0000");
            cmbRank.requestFocus();
        } else {
            String userId=txtId.getText();
            String name=txtName.getText();
            String password=psPassword.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            String rank=cmbRank.getValue();
            try {
                String existsUserId= UserModel.existsUser(nic);
                if (existsUserId==null){
                    if (UserModel.addUser(new User(userId,name,password,nic,telNumber,email,rank))){
                        reset();
                        txtName.requestFocus();
                        new Alert(Alert.AlertType.INFORMATION, "user data added...!").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not added...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "exists\nThis nic is the same as as another user's nic,user id "+existsUserId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        validateAll();
        if (!isValidName)
            txtName.requestFocus();
        else if (!isValidPassword)
            psPassword.requestFocus();
        else if (!isValidNic)
            txtNic.requestFocus();
        else if (!isValidTelephoneNumber)
            txtTelNumber.requestFocus();
        else if (!isValidEmail)
            txtEmail.requestFocus();
        else if (cmbRank.getSelectionModel().isEmpty()) {
            cmbRank.setStyle("-fx-border-color: #ff0000");
            cmbRank.requestFocus();
        }else{
            String userId=txtId.getText();
            String name=txtName.getText();
            String password=psPassword.getText();
            String nic=txtNic.getText();
            String telNumber=txtTelNumber.getText();
            String email=txtEmail.getText();
            String rank=cmbRank.getValue();
            try {
                String existsUserId= UserModel.existsUser(nic);
                if (existsUserId==null || existsUserId.equals(userId)){
                    if (UserModel.updateUser(new User(userId,name,password,nic,telNumber,email,rank))){
                        reset();
                        txtName.requestFocus();
                        isUpdate=false;
                        new Alert(Alert.AlertType.INFORMATION, "user data updated...!").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not updated...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "exists\nThis nic is the same as as another user's nic,user id "+existsUserId).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();            }
        }
    }

    @FXML
    void resetOnAction(ActionEvent event) {
        reset();
    }

    @FXML
    void nameOnAction(ActionEvent event) {
        if (isValidName)
            psPassword.requestFocus();
    }

    @FXML
    void passwordOnAction(ActionEvent event) {
        if (isValidPassword)
            txtNic.requestFocus();
    }

    @FXML
    void nicOnAction(ActionEvent event) {
        if (isValidNic)
            txtTelNumber.requestFocus();
    }

    @FXML
    void telNumberOnAction(ActionEvent event) {
        if (isValidTelephoneNumber)
            txtEmail.requestFocus();
    }

    @FXML
    void emailOnAction(ActionEvent event) {
        if (isValidEmail)
            cmbRank.requestFocus();
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
    void passwordKeyPressedOnAction(KeyEvent event) {
        passwordValidation();
    }

    @FXML
    void nicKeyPressedOnAction(KeyEvent event) {
        nicValidation();
    }

    @FXML
    void telNumKeyPressedOnAction(KeyEvent event) {
        telephoneNumberValidation();
    }

    @FXML
    void emailKeyPressedOnAction(KeyEvent event) {
        emailValidation();
    }

    private void nameValidation(){
        try {
            boolean isExitsUserName=UserModel.searchExistsUserName(txtId.getText(),txtName.getText());
            if (Pattern.compile("^[a-zA-Z\\s]{3,50}$").matcher(txtName.getText()).matches() && !isExitsUserName){
                txtName.setStyle("-fx-border-color: #76ff03");
                isValidName=true;
            }else {
                txtName.setStyle("-fx-border-color: #ff0000");
                isValidName=false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }
    private void passwordValidation(){
        if (psPassword.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
            psPassword.setStyle("-fx-border-color: #76ff03");
            isValidPassword=true;
        }else {
            psPassword.setStyle("-fx-border-color: #ff0000");
            isValidPassword=false;
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
        passwordValidation();
        nicValidation();
        telephoneNumberValidation();
        emailValidation();
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        UserTM userTM = tbl.getSelectionModel().getSelectedItem();
        ButtonType result = new Alert(Alert.AlertType.CONFIRMATION, "Deleting a user named "+userTM.getUserName()+"\nAre You Sure ? ").showAndWait().get();
        if (result==ButtonType.OK){
            try {
                if (UserModel.deleteUser(userTM.getUserId())){
                    reset();
                    new Alert(Alert.AlertType.INFORMATION, "Deleting Successfully\n"+userTM.getUserName()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }
}
