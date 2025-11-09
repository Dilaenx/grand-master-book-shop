package repository.custom.impl;

import model.CartTM;
import model.Item;
import repository.custom.ItemRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepositoryImpl implements ItemRepository {
    @Override
    public boolean save(Item item) throws SQLException {
       return CrudUtil.execute("INSERT INTO item (id, name, qty, unitPrice) VALUES (?, ?, ?, ?);",
               item.getId(),
               item.getName(),
               item.getQty(),
               item.getUnitPrice());
    }

    @Override
    public boolean update(Item item) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Item searchById(String id) throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item WHERE id = ?",id);
        resultSet.next();
        return new Item(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getDouble(4)
        );

        }

    @Override
    public List<Item> getAll() throws SQLException {
        List<Item> itemList =new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item");
        while (resultSet.next()){
            itemList.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }
        return itemList;
    }

    @Override
    public Integer getCount() {
        return 0;
    }

    @Override
    public boolean deleteById(String id) throws SQLException {
        return CrudUtil.execute("DELETE FROM item WHERE id = ?",id);
    }

    @Override
    public boolean updateById(Item item) throws SQLException {
        return CrudUtil.execute(
                "UPDATE item SET name = ?, qty = ?, unitPrice = ? WHERE id = ?",
                item.getName(),
                item.getQty(),
                item.getUnitPrice(),
                item.getId());


    }

    @Override
    public List<String> getAllIds() throws SQLException {
        List<String> ids = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM item");
        while (resultSet.next()){
            ids.add(resultSet.getString("id"));
        }
        return ids;
    }

    @Override
    public boolean updateStock(List<CartTM> cartTMList) throws SQLException {
        for(CartTM cartTM: cartTMList){
            boolean isUpdatestock= updateStock(cartTM);
            if(isUpdatestock)return false;
        }
        return true;
    }
    public boolean updateStock(CartTM cartTM) throws SQLException {
        return CrudUtil.execute("UPDATE ITEM SET qtyOnHand = qtyOnHand -? WHERE Code =?",
                cartTM.getQtyOnHand(),
                cartTM.getItemId());
    }
}
