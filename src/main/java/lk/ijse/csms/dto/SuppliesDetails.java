package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuppliesDetails {
    private String suppliesId;
    private String itemCode;
    private Integer quantity;
    private Double unitPrice;
}