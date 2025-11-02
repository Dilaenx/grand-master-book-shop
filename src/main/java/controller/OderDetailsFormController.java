package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.PurchaseOrder;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.ItemService;
import service.custom.OrderDetailsService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OderDetailsFormController implements Initializable {

    @FXML
    private JFXButton btnSearch;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbItemid;

    @FXML
    private ComboBox<String> cmbOrderId;

    @FXML
    private TableColumn colCustomerName;

    @FXML
    private TableColumn colCustomerId;

    @FXML
    private TableColumn colItemName;

    @FXML
    private TableColumn colItemQty;

    @FXML
    private TableColumn colOderId;

    @FXML
    private TableColumn colTotal;

    @FXML
    private Label lblCM;

    @FXML
    private Label lblAmont;

    @FXML
    private TableView<PurchaseOrder> tblOder;

    List<PurchaseOrder> purchaseOrderList =new ArrayList<>();
    OrderDetailsService service  = ServiceFactory.getInstance().getServiceType(ServiceType.ODERDETAILS);
    CustomerService custoemerService =ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
    OrderService orderService=ServiceFactory.getInstance().getServiceType(ServiceType.ODER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadTable();
        loadCustomerIds();
        loadItemIds();
        loadOrderIds();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            List<PurchaseOrder> customer = new ArrayList<>();
            for(PurchaseOrder purchaseOrder:purchaseOrderList){
                if(purchaseOrder.getCustomerId().equals(t1)){
                    customer.add(purchaseOrder);
                }
            }
            cmbOrderId.getSelectionModel().clearSelection();
            cmbItemid.getSelectionModel().clearSelection();
            tblOder.setItems(FXCollections.observableArrayList(customer));
            loadAmont(customer);

        });
        cmbItemid.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            List<PurchaseOrder> item = new ArrayList<>();
            for(PurchaseOrder purchaseOrder:purchaseOrderList){
                if(purchaseOrder.getItemId().equals(t1)){
                    item.add(purchaseOrder);

                }
            }
            cmbCustomerId.getSelectionModel().clearSelection();
            cmbOrderId.getSelectionModel().clearSelection();
            tblOder.setItems(FXCollections.observableArrayList(item));
            loadAmont(item);

        });
        cmbOrderId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            List<PurchaseOrder> order = new ArrayList<>();
            for(PurchaseOrder purchaseOrder:purchaseOrderList){
                if(purchaseOrder.getOrderId().equals(t1)){
                    order.add(purchaseOrder);

                }
            }
            cmbCustomerId.getSelectionModel().clearSelection();
            cmbItemid.getSelectionModel().clearSelection();
            tblOder.setItems(FXCollections.observableArrayList(order));
            loadAmont(order);
        });
    }

    private void loadOrderIds() {
        List <String> orderIds;
        try {
            orderIds=orderService.getAllIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cmbOrderId.setItems(FXCollections.observableArrayList(orderIds));
    }

    private void loadItemIds() {
        List<String> itemIds;
        try {
            itemIds=itemService.getAllIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cmbItemid.setItems(FXCollections.observableArrayList(itemIds));
    }

    private void loadCustomerIds() {
        List<String> itemIds;
        try {
           itemIds=custoemerService.getAllIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cmbCustomerId.setItems(FXCollections.observableArrayList(itemIds));
    }



    private void loadTable(){
        purchaseOrderList.clear();
        try {
            purchaseOrderList=service.allOderDetails();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tblOder.setItems(FXCollections.observableArrayList(purchaseOrderList));
        loadAmont(purchaseOrderList);
    }

    private void loadAmont(List <PurchaseOrder> purchaseOrderList) {
        Double amont = 0.0;
        for(PurchaseOrder purchaseOrder :purchaseOrderList){
            amont+=purchaseOrder.getTotal();
        }
        System.out.println(amont);
        lblAmont.setText(amont+"");
    }

    @FXML
    void btnAddOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnCustomerManagement(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/customerFormFXml.fxml"));
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
    void btnItemManagementForm(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/ItemForm.fxml"));
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
    void btnOderDetailsOnAction(ActionEvent event) {

    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        cmbOrderId.getSelectionModel().clearSelection();
        cmbItemid.getSelectionModel().clearSelection();
        cmbCustomerId.getSelectionModel().clearSelection();
        loadTable();
    }



    public void btnCreateOrderOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/AddOrderForm.fxml"));
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
}
