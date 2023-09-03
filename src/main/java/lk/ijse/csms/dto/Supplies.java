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
public class Supplies {
    private String suppliesId;
    private Date date;
    private Time time;
    private String supplierId;
    private List<SuppliesDetails> suppliesDetailsArrayList;
}