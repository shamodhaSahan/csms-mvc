package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SuppliesCartTM {
    private String itemCode;
    private String description;
    private String type;
    private int quantity;
    private double unitPrice;
    private int oldQuantity;
    private int newQuantity;
    private double total;
    private JFXButton delete;
}
