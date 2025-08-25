package trademill.apiserver.broker.kis.dto;

import lombok.Data;

@Data
public class OrderCashOutput {
    // KIS 주문응답의 주문번호 필드 (문서 기준)
    private String ORD_NO;
}
