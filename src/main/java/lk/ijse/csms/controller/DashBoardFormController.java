package lk.ijse.csms.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class DashBoardFormController {

    @FXML
    private AnchorPane anc;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private Label lblTotalCostomer;

    @FXML
    private Label lblTotalIncomeToDay;

    @FXML
    private Label lblTotalRepairsmustbeDelivered;

    @FXML
    private Label lblTotalTransaction;

    @FXML
    private Label lblTotalTransactionToDay;

    @FXML
    private ScrollPane scPane;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

}
