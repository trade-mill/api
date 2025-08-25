package trademill.apiserver.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String symbol;

    @Enumerated(EnumType.STRING) private OrderSide side;
    @Enumerated(EnumType.STRING) private OrderType orderType;

    private BigDecimal quantity;
    private BigDecimal price; // MARKET이면 null 가능

    @Enumerated(EnumType.STRING) private OrderStatus status;
    private String brokerOrderId;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
        if (status == null) status = OrderStatus.PENDING;
    }
    @PreUpdate
    void onUpdate() { updatedAt = OffsetDateTime.now(); }
}
