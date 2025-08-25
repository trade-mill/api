package trademill.apiserver.broker.kis;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import trademill.apiserver.broker.BrokerGateway;
import trademill.apiserver.config.KisProperties;
import trademill.apiserver.order.OrderSide;
import trademill.apiserver.broker.kis.dto.KisResponse;
import trademill.apiserver.broker.kis.dto.OrderCashOutput;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("kis")
@RequiredArgsConstructor
public class KisBrokerGateway implements BrokerGateway {

    private static final Logger log = LoggerFactory.getLogger(KisBrokerGateway.class);

    private final WebClient kisWebClient;
    private final KisTokenService tokenService;
    private final KisProperties props;

    @Override
    public String placeMarketBuy(String symbol, BigDecimal quantity) {
        return place(OrderSide.BUY, "MARKET", symbol, quantity, null);
    }

    @Override
    public String placeLimitBuy(String symbol, BigDecimal quantity, BigDecimal price) {
        return place(OrderSide.BUY, "LIMIT", symbol, quantity, price);
    }

    @Override
    public String placeMarketSell(String symbol, BigDecimal quantity) {
        return place(OrderSide.SELL, "MARKET", symbol, quantity, null);
    }

    @Override
    public String placeLimitSell(String symbol, BigDecimal quantity, BigDecimal price) {
        return place(OrderSide.SELL, "LIMIT", symbol, quantity, price);
    }

    private String place(OrderSide side, String orderType, String symbol, BigDecimal qty, BigDecimal price) {
        log.info("[KIS:{} {}] symbol={}, qty={}, price={}", side, orderType, symbol, qty, price);

        // 0) 토큰
        String token = tokenService.getAccessToken();

        // 1) KIS 주문 바디 구성
        //    ORD_DVSN: 00=지정가, 01=시장가
        final String ordDvsn = "LIMIT".equals(orderType) ? "00" : "01";
        final String ordUnpr = "MARKET".equals(orderType) ? "0" : price.toPlainString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("CANO", props.getCano());               // 계좌번호(앞 8자리)
        body.put("ACNT_PRDT_CD", props.getAcntPrdtCd()); // 상품코드(보통 01)
        body.put("PDNO", symbol);                        // 종목코드
        body.put("ORD_DVSN", ordDvsn);
        body.put("ORD_QTY", qty.toPlainString());
        body.put("ORD_UNPR", ordUnpr);

        // 2) 해시키 요청
        String hashKey = requestHashKey(token, body);

        // 3) 주문 요청
        String trId = mapTrId(side, props.isVirtual()); // KIS는 현금주문 TR이 매수/매도로 구분됨
        KisResponse<OrderCashOutput> resp = kisWebClient.post()
                .uri("/uapi/domestic-stock/v1/trading/order-cash")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> {
                    h.add("authorization", "Bearer " + token);
                    h.add("appkey", props.getAppKey());
                    h.add("appsecret", props.getAppSecret());
                    h.add("tr_id", trId);
                    h.add("custtype", "P"); // 개인
                    h.add("hashkey", hashKey);
                })
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ParameterizedTypes.orderCashResponse())
                .block();

        if (resp == null) throw new IllegalStateException("KIS response is null");
        if (!"0".equals(resp.getRt_cd())) {
            throw new IllegalStateException("KIS error: " + resp.getMsg_cd() + " - " + resp.getMsg1());
        }

        String ordNo = resp.getOutput() != null ? resp.getOutput().getORD_NO() : null;
        log.info("[KIS] rt_cd=0, ORD_NO={}", ordNo);
        return ordNo != null ? ordNo : "KIS-NO-ORDER-NO";
    }

    private String requestHashKey(String token, Map<String, Object> body) {
        Map<String, Object> resp = kisWebClient.post()
                .uri("/uapi/hashkey")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> {
                    h.add("authorization", "Bearer " + token);
                    h.add("appkey", props.getAppKey());
                    h.add("appsecret", props.getAppSecret());
                })
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Object hk = resp != null ? resp.get("HASH") : null;
        if (hk == null) throw new IllegalStateException("Failed to get hashkey from KIS");
        return hk.toString();
    }

    /** KIS 현금주문 TR 매핑 (모의/실전, 매수/매도) */
    private String mapTrId(OrderSide side, boolean virtual) {
        boolean buy = side == OrderSide.BUY;
        if (virtual) {
            return buy ? "VTTC0802U" : "VTTC0801U"; // 모의: 매수/매도
        } else {
            return buy ? "TTTC0802U" : "TTTC0801U"; // 실전: 매수/매도
        }
    }

    /** WebClient 제네릭 응답 타입 헬퍼 */
    static class ParameterizedTypes {
        static org.springframework.core.ParameterizedTypeReference<KisResponse<OrderCashOutput>> orderCashResponse() {
            return new org.springframework.core.ParameterizedTypeReference<>() {};
        }
    }
}
