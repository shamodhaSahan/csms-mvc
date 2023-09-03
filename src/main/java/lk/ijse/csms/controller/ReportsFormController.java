package lk.ijse.csms.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lk.ijse.csms.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;

public class ReportsFormController {

    @FXML
    void customerReport(ActionEvent event) {
        InputStream inputStream = this.getClass().getResourceAsStream("/report/CustomerReport.jrxml");
        runJasperReport(inputStream);
    }

    @FXML
    void incomeOnAction(ActionEvent event) {
        InputStream inputStream = this.getClass().getResourceAsStream("/report/income.jrxml");
        runJasperReport(inputStream);
    }

    private void runJasperReport(InputStream inputStream) {
        try {
            JasperReport compileReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport,null, DBConnection.getInstance().getConnection());
            //JasperPrintManager.printReport(jasperPrint,true);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void thisMonthOnAction(ActionEvent actionEvent) {
        InputStream inputStream = this.getClass().getResourceAsStream("/report/monthlyIncome.jrxml");
        runJasperReport(inputStream);
    }
}

