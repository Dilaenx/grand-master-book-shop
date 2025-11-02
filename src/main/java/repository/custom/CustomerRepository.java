package repository.custom;

import model.Customer;
import repository.CrudRepository;

import java.sql.SQLException;

public interface CustomerRepository extends CrudRepository<Customer,String>{
    void SaveCustomerReward(Double customerReward,String CustomerID) throws SQLException;
}
