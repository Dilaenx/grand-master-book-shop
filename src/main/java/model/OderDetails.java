package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OderDetails {
    private String oderId;
    private String customerId;
    private List<CartTM> cartTMList;
    private String placeOderDate;
    private String placeOderTime;
}
