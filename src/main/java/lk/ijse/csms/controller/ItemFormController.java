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
import lk.ijse.csms.dto.Item;
import lk.ijse.csms.dto.tm.ItemTM;
import lk.ijse.csms.model.ItemModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemFormController {

    @FXML
    public JFXButton btnDelete;
    @FXML
    private AnchorPane anc;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private TableColumn<ItemTM, String> colAction;

    @FXML
    private TableColumn<ItemTM, String> colDescription;

    @FXML
    private TableColumn<ItemTM, String> colItemCode;

    @FXML
    private TableColumn<ItemTM, Integer> colQtyOnStock;

    @FXML
    private TableColumn<ItemTM, String> colType;

    @FXML
    private TableColumn<ItemTM, Double> colUnitPrice;

    @FXML
    private TableView<ItemTM> tbl;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtItemCode;

    @FXML
    private TextField txtQtyOnStock;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtUnitPrice;

    private final ObservableList<ItemTM> itemObservableList= FXCollections.observableArrayList();
    private Item item;
    private boolean isValidPrice=false;
    private boolean isUpdate=false;

    public void initialize(){
        loadCmb();
        setCellValueFactory();
        reset();
    }

    private void reset() {
        try {
            txtItemCode.setText(ItemModel.getNextItemCode());
            cmbType.getSelectionModel().clearSelection();
            txtDescription.setText("");
            txtUnitPrice.setText("");
            txtQtyOnStock.setText("");

            btnAdd.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            isUpdate = false;
            refreshTable();
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "DataBase Error\n"+e.getMessage());
        }
    }

    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnStock.setCellValueFactory(new PropertyValueFactory<>("qtyOnStock"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("edit"));
    }

    private void loadCmb() {
        String[] type={"Laptop","Desktop","Accessories","Parts"};
        cmbType.getItems().addAll(type);
    }

    private void refreshTable() {
        itemObservableList.clear();
        try {
            for (Item item : ItemModel.getAllItem()){
                JFXButton edit = new JFXButton("📝");
                edit.setStyle("-fx-text-fill: #00E676;" +
                        "-fx-background-color: rgba(255,0,0,0);"+
                        "-fx-font-size: 15px"
                );
                edit.setMaxWidth(250);
                ItemTM itemTM = new ItemTM(item.getItemCode(), item.getItemType(), item.getDescription(), item.getUnitPrice(), item.getQtyOnStock(), edit);
                itemObservableList.add(itemTM);
                edit.setOnMouseClicked(mouseEvent -> {
                    tbl.getSelectionModel().select(itemTM);
                    fillCustomerFields(itemTM);
                });
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "Data Not Loading\n" + e.getMessage()).show();
        }
        tbl.setItems(itemObservableList);
    }

    private void fillCustomerFields(ItemTM itemTM) {
        isUpdate = true;
        btnAdd.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);

        txtItemCode.setText(itemTM.getItemCode());
        cmbType.getSelectionModel().select(itemTM.getItemType());
        txtDescription.setText(itemTM.getDescription());
        txtUnitPrice.setText(String.format("%.2f",itemTM.getUnitPrice()));
        txtQtyOnStock.setText(String.valueOf(itemTM.getQtyOnStock()));
    }

    @FXML
    void addOnAction(ActionEvent event) {
        priceValidation();
        if (cmbType.getSelectionModel().isEmpty())
            cmbType.setStyle("-fx-border-color: #ff0000");
        else if (txtDescription.getText().isEmpty())
            txtDescription.requestFocus();
        else if ((!isValidPrice) || txtUnitPrice.getText().isEmpty())
            txtUnitPrice.requestFocus();
        else if (txtQtyOnStock.getText().isEmpty())
            txtQtyOnStock.requestFocus();
        else {
            String itemCode=txtItemCode.getText();
            String type=cmbType.getSelectionModel().getSelectedItem();
            String description=txtDescription.getText();
            double unitPrice=Double.parseDouble(txtUnitPrice.getText());
            int qtyOnStock=Integer.parseInt(txtQtyOnStock.getText());
            try {
                String existsItemCode = ItemModel.searchDescriptionItem(description);
                if (existsItemCode == null){
                    item = new Item(itemCode,type,description,unitPrice,qtyOnStock);
                    if (ItemModel.addItem(item)){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION, "item data added...!").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not added...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "item exists,item code  "+existsItemCode).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "DataBase Error\n"+e.getMessage()).show();
            }
        }
    }

    @FXML
    void cmbTypeOnAction(ActionEvent event) {
        cmbType.setStyle("-fx-border-color: #76ff03");
        txtDescription.requestFocus();
    }

    @FXML
    void descriptionOnAction(ActionEvent event) {
        if (!txtDescription.getText().isEmpty())
            txtUnitPrice.requestFocus();
    }

    @FXML
    void qtyOnStockOnAction(ActionEvent event) {
        if (isUpdate){
            updateOnAction(event);
        }else {
            addOnAction(event);
        }

    }

    @FXML
    void resetOnAction(ActionEvent event) {
        reset();
    }

    @FXML
    void txtSearchMouseOnClick(MouseEvent event) {

    }

    @FXML
    void unitPriceOnAction(ActionEvent event) {
        if (isValidPrice)
            txtQtyOnStock.requestFocus();
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        priceValidation();
        if (cmbType.getSelectionModel().isEmpty())
            cmbType.setStyle("-fx-border-color: #ff0000");
        else if (txtDescription.getText().isEmpty())
            txtDescription.requestFocus();
        else if ((!isValidPrice) || txtUnitPrice.getText().isEmpty())
            txtUnitPrice.requestFocus();
        else if (txtQtyOnStock.getText().isEmpty())
            txtQtyOnStock.requestFocus();
        else {
            String itemCode=txtItemCode.getText();
            String type=cmbType.getSelectionModel().getSelectedItem();
            String description=txtDescription.getText();
            double unitPrice=Double.parseDouble(txtUnitPrice.getText());
            int qtyOnStock=Integer.parseInt(txtQtyOnStock.getText());
            try {
                String existsItemCode=ItemModel.searchDescriptionItem(description);
                if (existsItemCode==null || existsItemCode.equals(itemCode)){
                    item = new Item(itemCode,type,description,unitPrice,qtyOnStock);
                    if (ItemModel.updateItem(item)){
                        reset();
                        new Alert(Alert.AlertType.INFORMATION, "item data updated...!").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "data not updated...!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.INFORMATION, "item exists,item code "+existsItemCode).show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "DataBase Error\n"+e.getMessage()).show();
            }
        }
    }
    @FXML
    public void txtUnitPriceKeyPressedOnAction(KeyEvent keyEvent) {
        if (!keyEvent.getCharacter().matches("[0-9\\.]"))
            keyEvent.consume();
    }
    @FXML
    public void txtQtyOnStockKeyPressedOnAction(KeyEvent keyEvent) {
        if (!keyEvent.getCharacter().matches("[0-9]"))
            keyEvent.consume();
    }
    private void priceValidation(){
        if (Pattern.compile("^(\\d+)||((\\d+\\.)(\\d){2})$").matcher(txtUnitPrice.getText()).matches()){
            txtUnitPrice.setStyle("-fx-border-color: #76ff03");
            isValidPrice=true;
        }else {
            txtUnitPrice.setStyle("-fx-border-color: #ff0000");
            isValidPrice=false;
        }
    }

    @FXML
    public void deleteOnAction(ActionEvent actionEvent) {
        ItemTM item = tbl.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Deleting a Item code "+item.getItemCode()+"\nWhen the item is deleted,The order is also deleted\nAre You Sure ? ").showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if (ItemModel.deleteItem(item.getItemCode())){
                    reset();
                    new Alert(Alert.AlertType.INFORMATION, "Deleting Successfully\n"+item.getDescription()+" was deleted").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.CONFIRMATION, e.getMessage()).show();
            }
        }
    }
}
