package repository.custom.impl;

import model.CartTM;
import model.Item;
import model.OderDetails;
import repository.custom.OrderRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OderRepositoryImpl implements OrderRepository {
    @Override
    public Integer oderId() throws SQLException {

        Integer lastOderId = 1001;
        ResultSet resultSet = CrudUtil.execute("SELECT oder_id FROM order_details");
        while (resultSet.next()){
            lastOderId = resultSet.getInt("oder_id");
        }
        return lastOderId;
    }

    @Override
    public boolean save(OderDetails oderDetails) throws SQLException {
        String oderId =oderDetails.getOderId();
        if(CrudUtil.execute("INSERT INTO order_details VALUES(?,?,?,?)",
                oderId,
                oderDetails.getCustomerId(),
                oderDetails.getPlaceOderDate(),
                oderDetails.getPlaceOderTime())){
            for(CartTM cart:oderDetails.getCartTMList()){
                String itemId =cart.getItemId();
                Integer getQtyOnHand =cart.getQtyOnHand();
                CrudUtil.execute("INSERT INTO cart_tm VALUES(?,?,?)",
                        oderId,
                        itemId,
                        getQtyOnHand
                        );
                CrudUtil.execute("UPDATE item SET qty = qty - ? WHERE id = ?",
                        getQtyOnHand,
                        itemId);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllIds() throws SQLException {
        List <String> allIds=new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM order_details");
        while (resultSet.next()){
            allIds.add(resultSet.getString(1));
        }
        return allIds;
    }
}

