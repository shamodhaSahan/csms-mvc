package lk.ijse.csms.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.csms.dto.LoginRecord;
import lk.ijse.csms.dto.User;
import lk.ijse.csms.model.LoginRecordModel;
import lk.ijse.csms.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LoginFormController {
    @FXML
    private Label lblMassage;

    @FXML
    private Label lblRank;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private PasswordField psPassword;

    @FXML
    private TextField txtUsername;

    private boolean isAdmin = false;
    private User dbUser;

    public void initialize(){
        try {
            if (UserModel.getAllUser().size() < 1){
                UserModel.addUser(new User("U001","admin","1234",null,null,null,"ADMIN"));
                UserModel.addUser(new User("U002","admin","4321",null,null,null,"ADMIN"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void clickHereOnAction(MouseEvent event) {
        if (isAdmin){
            isAdmin = false;
            lblRank.setText("Cashier");
            lblMassage.setText("If you are admin  ? ");
        }else{
            isAdmin = true;
            lblRank.setText("Admin");
            lblMassage.setText("If you are cashier  ? ");
        }
    }

    @FXML
    void exitOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void logInOnAction(ActionEvent event) {
        try {
            if (!(txtUsername.getText().isEmpty() || psPassword.getText().isEmpty())) {
                String userName = txtUsername.getText();
                String password = psPassword.getText();
                String rank = isAdmin ? "Admin" : "Cashier";
                SideBarController.setUserProfile(isAdmin, userName);
                if (isAdmin) {
                    if (verification(userName, password, rank)) {
                        login(dbUser);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Your user name,password wrong or your not admin ...!").show();
                    }
                } else {
                    if (verification(userName, password, rank)) {
                        login(dbUser);
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Your user name,password wrong or your not user ...!").show();
                    }
                }
            }
            if (txtUsername.getText().isEmpty())
                txtUsername.setStyle("-fx-border-color: #ff0000");
            if (psPassword.getText().isEmpty())
                psPassword.setStyle("-fx-border-color: #ff0000");
        }catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    @FXML
    void onClickField(MouseEvent mouseEvent) {
        txtUsername.setStyle("-fx-border-color: #76ff03");
        psPassword.setStyle("-fx-border-color: #76ff03");
    }

    private boolean verification(String userName, String password, String rank) throws SQLException, ClassNotFoundException {
        dbUser = UserModel.searchUserName(userName);
        if (dbUser != null && password.equals(dbUser.getUserPassword()) && rank.equalsIgnoreCase(dbUser.getRank()))
            return true;
        return false;
    }
    private void login(User user) {
        try {
            if(LoginRecordModel.saveLogin(new LoginRecord(LoginRecordModel.getNextLoginId(), LocalDate.now(), LocalTime.now(), user.getUserId()))) {
                loginForm.getChildren().clear();
                loginForm.getChildren().add(FXMLLoader.load(getClass().getResource("/view/SideBar.fxml")));
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Forms Error !\n"+e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error !\n"+e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found Error !\n"+e.getMessage()).show();
        }
    }
}
