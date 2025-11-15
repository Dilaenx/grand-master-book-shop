package repository.custom.impl;

import com.sun.javafx.css.StyleManager;
import db.DBConnection;
import model.CartTM;
import model.Item;
import model.OderDetails;
import repository.RepositoryFactory;
import repository.custom.ItemRepository;
import repository.custom.OderDetailsRepository;
import repository.custom.OrderRepository;
import util.CrudUtil;
import util.RepositoryType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OderRepositoryImpl implements OrderRepository {
    ItemRepository itemRepository= RepositoryFactory.getInstance().getRepository(RepositoryType.ITEM);
    @Override
    public Integer oderId() throws SQLException {

        Integer lastOderId = 1001;
        ResultSet resultSet = CrudUtil.execute("SELECT oder_id FROM order_details");
        while (resultSet.next()){
            lastOderId = resultSet.getInt("oder_id");
        }
        return lastOderId;
    }
    public boolean save(OderDetails order) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try{
            PreparedStatement psTM = connection.prepareStatement("INSERT INTO order_details VALUES(?,?,?,?)");
            psTM.setObject(1,order.getOderId());
            psTM.setObject(2,order.getCustomerId());
            psTM.setObject(3,order.getPlaceOderDate());
            psTM.setObject(4,order.getPlaceOderTime());
            if(psTM.executeUpdate()>0){
                boolean isOrderDetailsAdd = OderDetailsImpl.getInstance().saveOrderDetails(order.getCartTMList());
                if(isOrderDetailsAdd){
                    boolean isUpdateStock = itemRepository.updateStock(order.getCartTMList());
                    if(isUpdateStock){
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
    //@Override
    public boolean savee(OderDetails oderDetails) throws SQLException {
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

