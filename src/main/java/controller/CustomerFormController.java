package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static util.ServiceType.CUSTOMER;

public class CustomerFormController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnClear1;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableView<Customer> tblCustomerList;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPhoneNumber;

    @FXML
    private Label lblCM;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private TableColumn colPoints;


    CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        tblCustomerList.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, t1) -> {
            txtName.setText(t1.getName());
            txtId.setText(t1.getId());
            txtPhoneNumber.setText(t1.getPhoneNumber() + "");
            txtAddress.setText(t1.getAddress());


        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) throws SQLException {
        Customer customer = new Customer(
                txtId.getText(),
                txtName.getText(),
                Integer.parseInt(txtPhoneNumber.getText()),
                txtAddress.getText(),
                5.0
        );

        if (customerService.add(customer)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Customer Added!").show();
        }

        loadTable();
        Clear();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        Clear();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            if (customerService.deleteById(txtId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Deleted!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
        Clear();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            customerService.updateById(
                    new Customer(
                            txtId.getText(),
                            txtName.getText(),
                            Integer.parseInt(txtPhoneNumber.getText()),
                            txtAddress.getText(),
                            0.0

                    )
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent actionEvent) {
        Customer customer = null;
        try {
            customer = customerService.searchById(txtId.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtPhoneNumber.setText(customer.getPhoneNumber().toString());
        loadTable();

    }

    private void loadTable() {
        List<Customer> getAll;
        try {
            getAll = customerService.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(getAll);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
       colPoints.setCellValueFactory(new PropertyValueFactory<>("reward"));

        tblCustomerList.setItems(FXCollections.observableArrayList(getAll));


    }

    private void Clear() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtPhoneNumber.setText("");
    }


    public void btnItemManagementOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/ItemForm.fxml"));
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

    public void btnAddOrderOnAction(ActionEvent actionEvent) {
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

    public void btnOderDetailsOnAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewFxml/OderDetails.fxml"));
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
