package service.custom.impl;

import model.OderDetails;
import repository.RepositoryFactory;
import repository.custom.OrderRepository;
import service.ServiceFactory;
import service.custom.OrderService;
import util.RepositoryType;
import util.ServiceType;

import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.ODER);
    @Override
    public Integer oderId() throws SQLException {
       return orderRepository.oderId();
    }

    @Override
    public boolean saveOderDetails(OderDetails oderDetails) throws SQLException {
        System.out.println("order details"+oderDetails);
        return orderRepository.save(oderDetails);

    }

    @Override
    public List<String> getAllIds() throws SQLException {
        return orderRepository.getAllIds();
    }


}
