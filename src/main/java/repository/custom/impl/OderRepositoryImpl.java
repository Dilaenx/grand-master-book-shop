package repository.custom.impl;

import com.sun.javafx.css.StyleManager;
import db.DBConnection;
import model.CartTM;
import model.Item;
import model.OderDetails;
import repository.custom.ItemRepository;
import repository.custom.OderDetailsRepository;
import repository.custom.OrderRepository;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OderRepositoryImpl implements OrderRepository {
    ItemRepository
    @Override
    public Integer oderId() throws SQLException {

        Integer lastOderId = 1001;
        ResultSet resultSet = CrudUtil.execute("SELECT oder_id FROM order_details");
        while (resultSet.next()){
            lastOderId = resultSet.getInt("oder_id");
        }
        return lastOderId;
    }
    public boolean savee(OderDetails order) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement psTM = connection.prepareStatement("INSERT INTO oders VALUES(?,?,?)");
        psTM.setObject(1,order.getOderId());
        psTM.setObject(1,order.getCustomerId());
        psTM.setObject(1,order.getPlaceOderDate());
        psTM.setObject(1,order.getPlaceOderTime());
        if(psTM.executeUpdate()>0){
            boolean isOrderDetailsAdd = OderDetailsImpl.getInstance().saveOrderDetails(order.getCartTMList());
            if(isOrderDetailsAdd){
                boolean isUpdateStock = itemRepository.updateStock(order.getCartTMList());
                if(isUpdateStock){
                    return true;
                }
            }
        }

        return false;
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

