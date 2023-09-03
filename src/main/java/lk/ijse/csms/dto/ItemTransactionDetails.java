package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemTransactionDetails {
    private String transactionId;
    private String itemCode;
    private Integer quantity;
    private Double unitPrice;
}