package lk.ijse.csms.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.csms.dto.LoginRecord;
import lk.ijse.csms.dto.User;
import lk.ijse.csms.dto.tm.LoginTM;
import lk.ijse.csms.model.LoginRecordModel;
import lk.ijse.csms.model.UserModel;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginRecordFormController {

    @FXML
    private AnchorPane anc;

    @FXML
    private TableColumn<LoginTM, String> colDateTime;

    @FXML
    private TableColumn<LoginTM, String> colLoginId;

    @FXML
    private TableColumn<LoginTM, String> colRank;

    @FXML
    private TableColumn<LoginTM, String> colTelNum;

    @FXML
    private TableColumn<LoginTM, String> colUserId;

    @FXML
    private TableColumn<LoginTM, String> colUserName;

    @FXML
    private TableView<LoginTM> tblLogin;
    public void initialize(){
        ObservableList<LoginTM> loginTMS= FXCollections.observableArrayList();
        colLoginId.setCellValueFactory((new PropertyValueFactory<>("loginId")));
        colUserId.setCellValueFactory((new PropertyValueFactory<>("userId")));
        colUserName.setCellValueFactory((new PropertyValueFactory<>("userName")));
        colDateTime.setCellValueFactory((new PropertyValueFactory<>("dateTime")));
        colTelNum.setCellValueFactory((new PropertyValueFactory<>("telephoneNumber")));
        colRank.setCellValueFactory((new PropertyValueFactory<>("rank")));
        try {
            for (LoginRecord loginRecord : LoginRecordModel.getAllLogin()) {
                User user = UserModel.searchUserId(loginRecord.getUserId());
                String dateTime = loginRecord.getDate()+" / "+loginRecord.getTime();
                loginTMS.add(new LoginTM(
                        loginRecord.getLoginId(),
                        loginRecord.getUserId(),
                        user.getUserName(),
                        dateTime,
                        user.getTelephoneNumber(),
                        user.getRank()
                ));
            }
            tblLogin.setItems(loginTMS);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
        }
    }
}
