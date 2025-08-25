package trademill.apiserver.broker;

import java.math.BigDecimal;


// BrokerGateway : '증권사와 어떻게 대화할지' 계약서(포트) 역할
public interface BrokerGateway {
    String placeMarketBuy(String symbol, BigDecimal quantity);
    String placeMarketSell(String symbol, BigDecimal quantity);
    String placeLimitBuy(String symbol, BigDecimal quantity, BigDecimal price);
    String placeLimitSell(String symbol, BigDecimal quantity, BigDecimal price);
}
