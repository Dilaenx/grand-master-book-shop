package service.custom;

import model.OderDetails;
import service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface OrderService extends SuperService {
    Integer oderId() throws SQLException;

    boolean saveOderDetails(OderDetails oderDetails) throws SQLException;

    List<String> getAllIds() throws SQLException;
}
