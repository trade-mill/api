package trademill.apiserver.order;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trademill.apiserver.broker.BrokerGateway;
import trademill.apiserver.order.dto.PlaceOrderRequest;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final BrokerGateway broker;

    public OrderService(OrderRepository orders, BrokerGateway broker) {
        this.orders = orders;
        this.broker = broker;
    }

    @Transactional
    public Order placeBuy(PlaceOrderRequest req){
        Order o = baseOf(req, OrderSide.BUY);
        String id = switch (req.getOrderType()){
            case MARKET -> broker.placeMarketBuy(req.getSymbol(), req.getQuantity());
            case LIMIT  -> broker.placeLimitBuy(req.getSymbol(), req.getQuantity(), req.getPrice());
        };
        o.setBrokerOrderId(id); o.setStatus(OrderStatus.ACCEPTED);
        return orders.save(o);
    }

    @Transactional
    public Order placeSell(PlaceOrderRequest req){
        Order o = baseOf(req, OrderSide.SELL);
        String id = switch (req.getOrderType()){
            case MARKET -> broker.placeMarketSell(req.getSymbol(), req.getQuantity());
            case LIMIT  -> broker.placeLimitSell(req.getSymbol(), req.getQuantity(), req.getPrice());
        };
        o.setBrokerOrderId(id); o.setStatus(OrderStatus.ACCEPTED);
        return orders.save(o);
    }

    public Page<Order> list(Long userId, Pageable pageable){
        return orders.findByUserId(userId, pageable);
    }

    private Order baseOf(PlaceOrderRequest req, OrderSide side){
        Order o = new Order();
        o.setUserId(req.getUserId());
        o.setSymbol(req.getSymbol());
        o.setSide(side);
        o.setOrderType(req.getOrderType());
        o.setQuantity(req.getQuantity());
        o.setPrice(req.getPrice());
        o.setStatus(OrderStatus.PENDING);
        return o;
    }
}
