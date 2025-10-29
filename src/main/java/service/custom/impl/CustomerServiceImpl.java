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
        System.out.println("ser :repository.getAll()");
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
    public boolean SaveCustomerReward(Double customerReward,String CustomerID) throws SQLException {
        return repository.SaveCustomerReward(customerReward, CustomerID);
    }
}
