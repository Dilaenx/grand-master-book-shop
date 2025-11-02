package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
    private String orderId;
    private String customerId;
    private String itemId;
    private String customerName;
    private String itemName;
    private Integer qty;
    private Double total;
}
