package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Item;
import service.ServiceFactory;
import service.custom.ItemService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Label lblCM;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtUnitPrice;

    ItemService service = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, Item, t1) -> {
            txtName.setText(t1.getName());
            txtId.setText(t1.getId());
            txtQty.setText(t1.getQty()+"");
            txtUnitPrice.setText(t1.getUnitPrice()+"");
        });
        loadTable();
    }

    @FXML
    public void btnCustomerManagement(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/customerFormFXml.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
        Stage currentStage = (Stage) ((JFXButton) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

    }

    @FXML
    void btnAboutOnAction(ActionEvent event) {

    }
    @FXML
    void btnAddOrderOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/AddOrderForm.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
        Stage currentStage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Item item =new Item(
                txtId.getText(),
                txtName.getText(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtUnitPrice.getText())
        );

        try {
            service.add(item);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        refreshUI();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            if (service.deleteById(txtId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Item Deleted!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        refreshUI();
    }

    @FXML
    void btnOderDetailsOnAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/OderDetails.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
        Stage currentStage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Item item =new Item();
        try {
            item =service.searchById(txtId.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtId.setText(item.getId());
        txtName.setText(item.getName());
        txtQty.setText(String.valueOf(item.getQty()));
        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            if (service.updateById(new Item(
                    txtId.getText(),
                    txtName.getText(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText())
            ))){
                new Alert(Alert.AlertType.CONFIRMATION,"Item Updated!").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
    }

    private void loadTable(){
        List<Item> allItem = new ArrayList<>();
        try {
            allItem = service.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tblItem.setItems(FXCollections.observableArrayList(allItem));
    }
    private void clear(){
        txtId.setText("");
        txtName.setText("");
        txtQty.setText("");
        txtUnitPrice.setText("");
    }
     private void refreshUI(){
        loadTable();
        clear();
     }


}
