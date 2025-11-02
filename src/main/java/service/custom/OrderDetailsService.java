package service.custom;

import model.PurchaseOrder;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailsService extends SuperService {
    List<PurchaseOrder> allOderDetails() throws SQLException;
}
