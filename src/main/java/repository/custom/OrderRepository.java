package repository.custom;

import model.OderDetails;
import repository.SuperRepository;

import java.sql.SQLException;
import java.util.List;

public interface OrderRepository extends SuperRepository {
    Integer oderId() throws SQLException;

    boolean save(OderDetails oderDetails) throws SQLException;

    List<String> getAllIds() throws SQLException;
}
