package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemTM {
    private String itemCode;
    private String itemType;
    private String description;
    private Double unitPrice;
    private Integer qtyOnStock;
    private JFXButton edit;
}
