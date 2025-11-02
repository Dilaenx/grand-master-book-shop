package service.custom.impl;

import model.PurchaseOrder;
import repository.RepositoryFactory;
import repository.custom.OderDetailsRepository;
import service.custom.OrderDetailsService;
import util.RepositoryType;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {

    OderDetailsRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryType.ODERDETAILS);
    @Override
    public List<PurchaseOrder> allOderDetails() throws SQLException {
        return repository.alloderDetails();
    }
}
