package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    private String id;
    private String name;
    private Integer phone_number;
    private String address;
    private Double reward;
}
