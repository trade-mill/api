package trademill.apiserver.broker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Profile({"local","default"})
@Component

// FakeBrokerGateway : 실제 증권사 호출 대신 로컬 개발/테스트용으로 응답을 꾸며주는 구현체(어댑터)
public class FakeBrokerGateway implements BrokerGateway {
    private String id(){ return "SIM-" + UUID.randomUUID(); }

    public String placeMarketBuy(String s, BigDecimal q){ return id(); }
    public String placeMarketSell(String s, BigDecimal q){ return id(); }
    public String placeLimitBuy(String s, BigDecimal q, BigDecimal p){ return id(); }
    public String placeLimitSell(String s, BigDecimal q, BigDecimal p){ return id(); }
}
