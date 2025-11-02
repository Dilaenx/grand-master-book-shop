package service.custom.impl;

import model.Customer;
import repository.RepositoryFactory;
import repository.custom.CustomerRepository;
import service.custom.CustomerService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    CustomerRepository repository= RepositoryFactory.getInstance().getRepository(RepositoryType.CUSTOMER);
    @Override
    public boolean add(Customer customer) throws SQLException {
        return repository.save(customer);

    }



    @Override
    public List<Customer> getAll() throws SQLException {
        return repository.getAll();

    }

    @Override
    public Customer searchById(String id) throws SQLException {
        return repository.searchById(id);
    }

    @Override
    public boolean deleteById(String text) throws SQLException {
        return repository.deleteById(text);
    }

    @Override
    public boolean updateById(Customer customer) throws SQLException {
        return repository.updateById(customer);
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return repository.getAllIds();
    }

    @Override
    public void SaveCustomerReward(Double customerReward,String CustomerID) throws SQLException {
        repository.SaveCustomerReward(customerReward, CustomerID);
    }
}
