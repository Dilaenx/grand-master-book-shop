package repository.custom.impl;

import model.CartTM;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OderDetailsImpl {
    private static OderDetailsImpl instance;
    private OderDetailsImpl(){}
    public boolean saveOderDetails(List<OderDetailsImpl> oderDetailsList){
        return true;
    }
    public boolean saveOrderDetails(List<CartTM> cartTMSList) throws SQLException {
        for(CartTM cartTM:cartTMSList){
            boolean isSave =saveOrderDetails(cartTM);
            if(!isSave)return false;
        }
        return true;
    }
    public boolean saveOrderDetails(CartTM cartTM) throws SQLException {
        return CrudUtil.execute("INSERT INTO cart_tm VALUES(?,?,?)",
                cartTM.getOderId(),
                cartTM.getItemId(),
                cartTM.getQtyOnHand()
        );
    }
    public static OderDetailsImpl getInstance(){
        return instance==null?instance=new OderDetailsImpl():instance;
    }
}
