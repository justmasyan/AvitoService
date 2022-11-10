package avito.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class Payment {
    private int user_id;
    private int service_id;
    private int order_id;
    private BigDecimal amount;

}
