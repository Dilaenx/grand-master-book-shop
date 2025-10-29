package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CartTM;
import model.Customer;
import model.Item;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.ItemService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OderFormController implements Initializable {

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
    private ComboBox<String> cmbCustomerID;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Label lblCM;

    @FXML
    private Label lblCM1;

    @FXML
    private Label lblCM11;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblCustomerPhoneNumber;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblUnityPrice;

    @FXML
    private Label lblNetTotal;

    @FXML
    private TableView<CartTM> tblOder;

    @FXML
    private JFXTextField txtQtyOnHand;

    CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

    List<CartTM> cartTMSList = new ArrayList<>();
    Double netTotal= 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadCustomerIds();
        loadItemIds();

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1!=null)setTextCustomerLbl((String) t1);
        });
        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1!=null)setItemCustomerLbl((String) t1);
        });
        tblOder.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            System.out.println(t1);
            cmbItemId.setValue(t1.getItemId());
            lblItemName.setText(t1.getItemName());
            lblStock.setText(t1.getQty()+"");
            lblUnityPrice.setText(t1.getPrice()+"");
            txtQtyOnHand.setText(t1.getQtyOnHand()+"");
        });

    }
    private void setTextCustomerLbl(String id){
        Customer customer;
        try {
            customer = customerService.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        lblCustomerName.setText(customer.getName());
        lblCustomerPhoneNumber.setText(customer.getPhoneNumber()+"");
    }
    private void setItemCustomerLbl(String id){
        Item item = new Item();
        Integer useStock =0;

        try {
            item = itemService.searchById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        for (CartTM cartItem : cartTMSList) {
            if (cartItem.getItemId().equals(id)) {
                useStock = Integer.valueOf((cartItem.getQty()));
                break;
            }
        }



        lblItemName.setText(item.getName());
        lblStock.setText(item.getQty()-useStock+"");
        lblUnityPrice.setText(item.getUnitPrice()+"");
    }
    @FXML
    void btnAddOrderOnAction(ActionEvent event) {

    }
    private void loadTable(){
        netTotal=0.0;
        for(CartTM item :cartTMSList){
            netTotal+= item.getTotal();
        }
        tblOder.getItems().clear();
        tblOder.setItems(FXCollections.observableArrayList(cartTMSList));


        System.out.println(netTotal);
        lblNetTotal.setText(netTotal+"");

    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String itemId = (String) cmbItemId.getValue();
        String itemName = lblItemName.getText();
        Integer qtyToAdd = Integer.parseInt(txtQtyOnHand.getText());
        Integer stock = Integer.parseInt(lblStock.getText());
        Double price = Double.parseDouble(lblUnityPrice.getText());
        if (qtyToAdd > stock) {
            new Alert(Alert.AlertType.WARNING, "Quantity exceeds stock!").show();
            return;
        }


        // Check if item already exists in cart
        boolean itemExists = false;
        for (CartTM cartItem : cartTMSList) {
            if (cartItem.getItemId().equals(itemId)) {
                // Item exists → combine quantities and recalc total
                int newQty = cartItem.getQty() + qtyToAdd;
                cartItem.setQty(newQty);
                cartItem.setTotal(newQty * cartItem.getPrice());
                itemExists = true;
                break;
            }
        }

        // If item not in cart, add new
        if (!itemExists) {
            CartTM cartTM = new CartTM(
                    itemId,
                    itemName,
                     stock,
                    price,
                    (double)qtyToAdd,
                    qtyToAdd * price
            );
            cartTMSList.add(cartTM);
        }

        loadTable(); // refresh TableView
        clear();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        lblCustomerPhoneNumber.setText("Phone Number");
        cmbCustomerID.getSelectionModel().clearSelection();
        lblCustomerName.setText("Customer");
        clear();
    }

    @FXML
    void btnCustomerManagement(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String itemId = tblOder.getSelectionModel().getSelectedItem().getItemId();
        cartTMSList.removeIf(cart -> cart.getItemId().equals(itemId));
        loadTable();
    }

    @FXML
    void btnItemManagementForm(ActionEvent event) {

    }

    @FXML
    void btnOderDetailsOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrdrOnAction(ActionEvent event) {


    }
    private void loadCustomerIds(){

        try {
            cmbCustomerID.setItems(FXCollections.observableArrayList(customerService.getAllIds()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private void loadItemIds(){

        try {
            cmbItemId.setItems(FXCollections.observableArrayList(itemService.getAllIds()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void clear(){
        cmbItemId.getSelectionModel().clearSelection();
        lblItemName.setText("Item");
        lblStock.setText("Stock");
        lblUnityPrice.setText("Unity Price");
        txtQtyOnHand.clear();
    }


    public void btnOrderDetailsOnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String itemId = (String) cmbItemId.getValue();
        String itemName = lblItemName.getText();
        int qtyToAdd = Integer.parseInt(txtQtyOnHand.getText());
        int stock = Integer.parseInt(lblStock.getText());
        double price = Double.parseDouble(lblUnityPrice.getText());
        if (qtyToAdd > stock) {
            new Alert(Alert.AlertType.WARNING, "Quantity exceeds stock!").show();
            return;
        }

        // Check if item already exists in cart
        boolean itemExists = false;
        for (CartTM cartItem : cartTMSList) {
            if (cartItem.getItemId().equals(itemId)) {
                int newQty = qtyToAdd;
                cartItem.setQty(newQty);
                cartItem.setTotal(newQty * cartItem.getPrice());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            return;
        }
        loadTable(); // refresh TableView
        clear();
    }
}
