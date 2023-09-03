package lk.ijse.csms.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SideBarController {

    @FXML
    private AnchorPane anc;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnDashBoard;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnItem;

    @FXML
    private Button btnLoginRecord;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnRepair;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSupplier;

    @FXML
    private Button btnSupplies;

    @FXML
    private Button btnUser;

    @FXML
    private Label date;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserRank;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Pane rightPane;
    private final ArrayList<Button> buttonArrayList = new ArrayList<>();
    private static boolean isAdmin;
    private static String userName;
    public static void setUserProfile(boolean isAdmin, String userName){
        SideBarController.isAdmin = isAdmin;
        SideBarController.userName = userName;
    }

    public void initialize(){
        setProfileDetails();
        setButtonsArray();
        if (isAdmin) {
            btnLoginRecord.setVisible(true);
            btnUser.setVisible(true);
            btnEmployee.setVisible(true);
            btnSupplier.setVisible(true);
            btnSupplies.setVisible(true);
            btnItem.setVisible(true);
        }else {
            btnLoginRecord.setVisible(false);
            btnUser.setVisible(false);
            btnEmployee.setVisible(false);
            btnSupplier.setVisible(false);
            btnSupplies.setVisible(false);
            btnItem.setVisible(false);
        }
        goDashBoardOnAction(new ActionEvent());
    }

    private void setProfileDetails() {
        lblUserName.setText(userName);
        lblUserRank.setText(isAdmin?"Admin":"Cashier");
        date.setText(LocalDate.now().toString());
    }

    private void setButtonsArray() {
        buttonArrayList.add(btnDashBoard);
        buttonArrayList.add(btnCustomer);
        buttonArrayList.add(btnPlaceOrder);
        buttonArrayList.add(btnRepair);
        buttonArrayList.add(btnReport);
        buttonArrayList.add(btnLoginRecord);
        buttonArrayList.add(btnUser);
        buttonArrayList.add(btnEmployee);
        buttonArrayList.add(btnSupplier);
        buttonArrayList.add(btnSupplies);
        buttonArrayList.add(btnItem);
    }
    @FXML
    void exitOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void goCustomerOnAction(ActionEvent event) {
        changeSelectedButton(btnCustomer);
        initUI("CustomerForm.fxml");
    }

    @FXML
    void goDashBoardOnAction(ActionEvent event) {
        changeSelectedButton(btnDashBoard);
        initUI("DashBoardForm.fxml");
    }

    @FXML
    void goEmployerOnAction(ActionEvent event) {
        changeSelectedButton(btnEmployee);
        initUI("EmployeeForm.fxml");
    }

    @FXML
    void goItemOnAction(ActionEvent event) {
        changeSelectedButton(btnItem);
        initUI("ItemForm.fxml");
    }

    @FXML
    void goLoginRecordAction(ActionEvent event) {
        changeSelectedButton(btnLoginRecord);
        initUI("LoginRecord.fxml");
    }

    @FXML
    void goPlaceOrderOnAction(ActionEvent event) {
        changeSelectedButton(btnPlaceOrder);
        initUI("PlaceOrderForm.fxml");
    }

    @FXML
    void goRepairOnAction(ActionEvent event) {
        changeSelectedButton(btnRepair);
        initUI("RepairForm.fxml");
    }

    @FXML
    void goReportsOnAction(ActionEvent event) {
        changeSelectedButton(btnReport);
        initUI("ReportsForm.fxml");
    }

    @FXML
    void goSupplierOnAction(ActionEvent event) {
        changeSelectedButton(btnSupplier);
        initUI("SupplierForm.fxml");
    }

    @FXML
    void goSuppliesOnAction(ActionEvent event) {
        changeSelectedButton(btnSupplies);
        initUI("SuppliesForm.fxml");
    }

    @FXML
    void goUserOnAction(ActionEvent event) {
        changeSelectedButton(btnUser);
        initUI("UserForm.fxml");
    }

    @FXML
    void logOutOnAction(ActionEvent event) {
        try {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml")));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Forms Error !\n"+e.getMessage()).show();
        }
    }

    @FXML
    void userIconClick(MouseEvent event) {

    }

    private void initUI(String location) {
        try {
            anc.getChildren().clear();
            anc.getChildren().add(FXMLLoader.load(getClass().getResource("/view/" + location)));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Forms Error !\n"+e.getMessage()).show();
        }
    }
    void changeSelectedButton(Button selectedButton){
        for (Button button:buttonArrayList) {
            if (button.getId().equals(selectedButton.getId())){
                selectedButton.getStyleClass().removeAll("navigation-button");
                selectedButton.getStyleClass().addAll("selected-navigation-button");
            }else {
                button.getStyleClass().removeAll("selected-navigation-button");
                button.getStyleClass().addAll("navigation-button");
            }
        }
    }
}
