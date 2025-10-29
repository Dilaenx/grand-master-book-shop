package repository.custom.impl;

import model.Customer;
import repository.custom.CustomerRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public boolean save(Customer customer) throws SQLException {
         return CrudUtil.execute("Insert into customer VALUES(?,?,?,?,?)",
                customer.getId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getReward());

    }

    @Override
    public boolean update(Customer customer) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Customer searchById(String id) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE id=?", id);
        resultSet.next();
        return new Customer(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getString(4),
                resultSet.getDouble(5)
        );
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer");
        while(resultSet.next())
            customerList.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            ));

        return customerList;
    }

    @Override
    public Integer getCount() {
        return 0;
    }

    @Override
    public boolean deleteById(String id) throws SQLException {
        return CrudUtil.execute("DELETE FROM customer WHERE id = ?",id);
    }

    @Override
    public boolean updateById(Customer customer) throws SQLException {
        return CrudUtil.execute(
                "UPDATE customer SET name = ?, phoneNumber = ?, address = ? WHERE id = ?",
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getId());
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        List<String> ids = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM customer");
        while (resultSet.next()){
            ids.add(resultSet.getString("id"));
        }
        return ids;
    }

    @Override
    public boolean SaveCustomerReward(Double customerReward,String CustomerID) throws SQLException {
        return CrudUtil.execute("UPDATE customer SET reward = ? WHERE id = ?",
                customerReward,
                CustomerID
        );
    }
}
