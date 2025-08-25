package trademill.apiserver.order.dto;

import lombok.Data;
import trademill.apiserver.order.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String symbol;
    private OrderSide side;
    private OrderType orderType;
    private BigDecimal quantity;
    private BigDecimal price;
    private OrderStatus status;
    private String brokerOrderId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static OrderResponse from(Order o) {
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setUserId(o.getUserId());
        r.setSymbol(o.getSymbol());
        r.setSide(o.getSide());
        r.setOrderType(o.getOrderType());
        r.setQuantity(o.getQuantity());
        r.setPrice(o.getPrice());
        r.setStatus(o.getStatus());
        r.setBrokerOrderId(o.getBrokerOrderId());
        r.setCreatedAt(o.getCreatedAt());
        r.setUpdatedAt(o.getUpdatedAt());
        return r;
    }
}
