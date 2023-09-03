package lk.ijse.csms.dto;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    private String transactionId;
    private Date date;
    private Time time;
    private String customerId;
    private String type;
    private List<ItemTransactionDetails> itemTransactionDetailsArrayList;
    private List<RepairTransactionDetails> repairTransactionDetailsArrayList;
}