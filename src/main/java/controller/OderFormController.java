package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CartTM;
import model.Customer;
import model.Item;
import model.OderDetails;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.ItemService;
import service.custom.OrderService;
import util.CrudUtil;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
    private Label lblOderId;

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

    @FXML
    private Label lblTime;

    @FXML
    private Label lblDate;

    @FXML
    private JFXButton btnReward;

    CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
    OrderService orderService= ServiceFactory.getInstance().getServiceType(ServiceType.ODER);

    List<CartTM> cartTMSList = new ArrayList<>();
    Double netTotal= 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOderId();
        setDateAndTime();
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        loadCustomerIds();
        loadItemIds();

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1!=null) {
                setTextCustomerLbl((String) t1);
                btnReward.setText(getCustomerReward()+"");
            }
            if(cmbCustomerID.getValue()!=null){
                itemCler();
                cartTMSList.clear();
                loadTable();
            }
        });
        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1!=null)setItemCustomerLbl((String) t1);
            txtQtyOnHand.clear();
        });
        tblOder.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            System.out.println(t1);
            cmbItemId.setValue(t1.getItemId());
            lblItemName.setText(t1.getItemName());
            //int newStock = 0;
            for (CartTM cartItem : cartTMSList) {
                if (cartItem.getItemId().equals(t1.getItemId())) {
                    // Item exists → combine quantities and recalc total
                    //newStock = cartItem.getQty()- t1.getQtyOnHand();
                    System.out.println("system: "+cartItem.getQty());
                    System.out.println("t1 :"+ t1.getQtyOnHand());
                    break;
                }
            }
            lblStock.setText(t1.getQty()+"");
            lblUnityPrice.setText(t1.getPrice()+"");
            txtQtyOnHand.setText(t1.getQtyOnHand()+"");
        });

    }
    private Integer oderid(){
        try {
            return orderService.oderId()+1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
     private void setOderId(){
         lblOderId.setText(oderid()+"");
     }

    private void setDateAndTime() {Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        lblDate.setText(simpleDateFormat.format(date));


        Timeline timeline = new Timeline(
                new KeyFrame(javafx.util.Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


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
        if (cmbItemId.getValue()==null) {
            new Alert(Alert.AlertType.WARNING, "Select Item").show();
            return;

        }
        if(txtQtyOnHand.getText()==null||txtQtyOnHand.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Qty On Hand Is Empty").show();
            return;
        }
        try {
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            System.out.println("Quantity on hand: " + qtyOnHand);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Quantity must be an integer!");
            alert.showAndWait();
            return;
        }


        Integer qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        Integer oderId =Integer.parseInt(lblOderId.getText());
        String itemId = (String) cmbItemId.getValue();
        String itemName = lblItemName.getText();
        Integer stock = Integer.parseInt(lblStock.getText());
        Double price = Double.parseDouble(lblUnityPrice.getText());
        Integer tblValues = 0;

        if ((qtyOnHand+tblValues) > stock) {
            new Alert(Alert.AlertType.WARNING, "Quantity exceeds stock!").show();
            return;

        }


        // Check if item already exists in cart
        boolean itemExists = false;
        for (CartTM cartItem : cartTMSList) {
            if (cartItem.getItemId().equals(itemId)) {
                if ((qtyOnHand+cartItem.getQtyOnHand()) > stock) {
                    new Alert(Alert.AlertType.WARNING, "Quantity exceeds stock!2222222222").show();
                    return;
                }
                // Item exists → combine quantities and recalc total
                int newQty = cartItem.getQtyOnHand() + qtyOnHand;
                cartItem.setQtyOnHand(newQty);
                cartItem.setTotal(newQty * cartItem.getPrice());
                itemExists = true;
                break;
            }
        }

        // If item not in cart, add new
        if (!itemExists) {
            CartTM cartTM = new CartTM(
                    oderId,
                    itemId,
                    itemName,
                     stock,
                    price,
                    (Integer) qtyOnHand,
                    qtyOnHand * price
            );
            cartTMSList.add(cartTM);
        }

        loadTable(); // refresh TableView
        itemCler();
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
    void btnDeleteOnAction(ActionEvent event) {
        String itemId = tblOder.getSelectionModel().getSelectedItem().getItemId();
        cartTMSList.removeIf(cart -> cart.getItemId().equals(itemId));
        loadTable();
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
    void btnPlaceOrdrOnAction(ActionEvent event) {
        boolean customerAdd ;
        boolean saveCustomerReward = false;

        if(tblOder.getItems().isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Select Items").show();
            return;
        }
       // Integer oderId =Integer.parseInt(lblOderId.getText());

        System.out.println(cartTMSList);
        Double netValue=Double.parseDouble(lblNetTotal.getText());
        for(CartTM cartTM:cartTMSList){
            netTotal+=cartTM.getTotal();}

        String placeOderDate=lblDate.getText();
        String placeOderTime=lblTime.getText();
        System.out.println(placeOderDate+"and"+placeOderTime);
        try {
            customerAdd=orderService.saveOderDetails(
                    new OderDetails(
                           lblOderId.getText(),
                           (cmbCustomerID.getValue()==null)? "Guest":cmbCustomerID.getValue(),
                           cartTMSList,
                           lblDate.getText(),
                           lblTime.getText()
                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(customerAdd){
            Double CustomerReward=(netValue*0.1)+Double.parseDouble(btnReward.getText());
            if(cmbCustomerID.getValue()!=null) {
                try {
                    saveCustomerReward= customerService.SaveCustomerReward(CustomerReward,cmbCustomerID.getValue());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            clear();
            cartTMSList.clear();
            loadTable();


        }
        if(saveCustomerReward){
            new Alert(Alert.AlertType.CONFIRMATION, "Order successfully!").show();
            setOderId();
            System.out.println("Oder Complete ....!");
        }

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
        cmbCustomerID.getSelectionModel().clearSelection();
        itemCler();
    }
    private void itemCler(){
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
        Integer qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        Integer stock = Integer.parseInt(lblStock.getText());
        double price = Double.parseDouble(lblUnityPrice.getText());
        if (qtyOnHand > stock) {
            new Alert(Alert.AlertType.WARNING, "Quantity exceeds stock!").show();
            return;
        }

        // Check if item already exists in cart
        boolean itemExists = false;
        for (CartTM cartItem : cartTMSList) {
            if (cartItem.getItemId().equals(itemId)) {
                int newQty = qtyOnHand;
                cartItem.setQtyOnHand(qtyOnHand);
                cartItem.setTotal(qtyOnHand * cartItem.getPrice());
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
    public Double getCustomerReward(){
        Customer customer =new Customer();
        try {
            customer= customerService.searchById((cmbCustomerID.getValue()) );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer.getReward();
    }

    public void btnRewardOnAction(ActionEvent actionEvent) {
        Double reward =Double.parseDouble(btnReward.getText());
        if(netTotal>reward){
            netTotal-=reward;
            lblNetTotal.setText(netTotal+"");
            btnReward.setText("0.0");
        }else{
            new Alert(Alert.AlertType.WARNING, "The total price must be greater than the reward").show();
            return;
        }

    }
}
