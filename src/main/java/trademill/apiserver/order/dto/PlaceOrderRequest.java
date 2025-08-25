package trademill.apiserver.order.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import trademill.apiserver.order.OrderType;

import java.math.BigDecimal;

@Getter @Setter
public class PlaceOrderRequest {
    @NotNull private Long userId;
    @NotBlank private String symbol;
    @NotNull private OrderType orderType;            // MARKET or LIMIT
    @NotNull @DecimalMin("0.00000001") private BigDecimal quantity;
    @DecimalMin("0.0") private BigDecimal price;     // LIMIT일 때만 필요
}
