package trademill.apiserver.order;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import trademill.apiserver.order.dto.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){ this.service = service; }

    @PostMapping("/buy")
    public OrderResponse buy(@Valid @RequestBody PlaceOrderRequest req){
        return OrderResponse.from(service.placeBuy(req));
    }

    @PostMapping("/sell")
    public OrderResponse sell(@Valid @RequestBody PlaceOrderRequest req){
        return OrderResponse.from(service.placeSell(req));
    }

    @GetMapping
    public Page<OrderResponse> list(@RequestParam Long userId,
                                    @RequestParam(defaultValue="0") int page,
                                    @RequestParam(defaultValue="20") int size){
        return service.list(userId, PageRequest.of(page, size, Sort.by("id").descending()))
                .map(OrderResponse::from);
    }
}
