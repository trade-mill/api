package trademill.apiserver.broker.kis.dto;

import lombok.Data;

@Data
public class KisResponse<T> {
    private String rt_cd;   // "0" 성공, "1" 실패
    private String msg_cd;
    private String msg1;
    private T output;
}
