package repository.custom;

import model.CartTM;
import model.Item;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;

public interface ItemRepository extends CrudRepository<Item,String> {
    boolean updateStock(List<CartTM> cartTMList) throws SQLException;
}
