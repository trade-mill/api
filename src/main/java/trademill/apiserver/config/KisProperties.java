package trademill.apiserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties(prefix = "kis")
public class KisProperties {
    private String baseUrl;
    private String appKey;
    private String appSecret;
    private String accountNo; // 예: 50151652-01

    public String getCano() {
        if (accountNo == null) return null;
        int idx = accountNo.indexOf('-');
        return idx > 0 ? accountNo.substring(0, idx) : accountNo;
    }

    public String getAcntPrdtCd() {
        if (accountNo == null) return null;
        int idx = accountNo.indexOf('-');
        return idx > 0 ? accountNo.substring(idx + 1) : "01";
    }

    public boolean isVirtual() {
        // 모의투자 여부 (base-url 에 vts 포함 시 true)
        return baseUrl != null && baseUrl.contains("openapivts");
    }
}
