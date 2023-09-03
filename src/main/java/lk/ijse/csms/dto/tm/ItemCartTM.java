package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemCartTM {
    private String itemCode;
    private String description;
    private int quantity;
    private double unitPrice;
    private double total;
    private JFXButton edit;
}
