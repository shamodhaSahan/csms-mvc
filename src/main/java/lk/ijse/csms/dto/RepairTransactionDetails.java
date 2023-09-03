package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepairTransactionDetails {
    private String transactionId;
    private String repairId;
    private Double repairPrice;
}
