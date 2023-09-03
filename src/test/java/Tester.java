import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.csms.controller.SideBarController;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/4/2023
 * Time :8:16 AM
 * Project name : IntelliJ IDEA
 */

public class Tester extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SideBarController.setUserProfile(true,"tester");
        Parent root = FXMLLoader.load(getClass().getResource("/view/SideBar.fxml"));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
