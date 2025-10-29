package repository.custom.impl;

import model.PurchaseOrder;
import repository.custom.OderDetailsRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OderDetailsRepositoryImpl implements OderDetailsRepository {
    List<PurchaseOrder> purchaseOrderList =new ArrayList<>();
    @Override
    public List<PurchaseOrder> alloderDetails() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT od.oder_id AS orderId, c.id AS customerId, i.id AS itemId, c.name AS customerName, i.name AS itemName, ct.qty AS qty, (ct.qty * i.unitPrice) AS total FROM order_details od JOIN customer c ON od.customer_id = c.id JOIN cart_tm ct ON od.oder_id = ct.order_id JOIN item i ON ct.item_id = i.id ORDER BY od.oder_id");
        while (resultSet.next()){
            purchaseOrderList.add(
                    new PurchaseOrder(
                            resultSet.getString("orderId"),
                            resultSet.getString("customerId"),
                            resultSet.getString("itemId"),
                            resultSet.getString("customerName"),
                            resultSet.getString("itemName"),
                            Integer.parseInt(resultSet.getString("qty")),
                            Double.parseDouble(resultSet.getString("total"))
                    )
            );
        }
        System.out.println(purchaseOrderList);
        return purchaseOrderList;
    }

}
