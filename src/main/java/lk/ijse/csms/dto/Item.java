package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    private String itemCode;
    private String itemType;
    private String description;
    private Double unitPrice;
    private Integer qtyOnStock;
}