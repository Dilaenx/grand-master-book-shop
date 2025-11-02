package service.custom;

import model.Customer;
import service.CrudService;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService extends CrudService<Customer,String> {

    void SaveCustomerReward(Double customerReward,String customerId) throws SQLException;
}
