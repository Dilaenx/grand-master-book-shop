package repository.custom;

import model.PurchaseOrder;
import repository.SuperRepository;

import java.sql.SQLException;
import java.util.List;

public interface OderDetailsRepository extends SuperRepository {
    List<PurchaseOrder> alloderDetails() throws SQLException;
}
