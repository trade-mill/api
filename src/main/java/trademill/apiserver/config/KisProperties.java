package trademill.apiserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kis")
public class KisProperties {
    private String baseUrl;
    private String appKey;
    private String appSecret;
    private String cano;
    private String acntPrdtCd;
    private TrId trId = new TrId();

    public static class TrId {
        private String buy;
        private String sell;
        // getters/setters
        public String getBuy() { return buy; }
        public void setBuy(String buy) { this.buy = buy; }
        public String getSell() { return sell; }
        public void setSell(String sell) { this.sell = sell; }
    }

    // getters/setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getAppKey() { return appKey; }
    public void setAppKey(String appKey) { this.appKey = appKey; }
    public String getAppSecret() { return appSecret; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
    public String getCano() { return cano; }
    public void setCano(String cano) { this.cano = cano; }
    public String getAcntPrdtCd() { return acntPrdtCd; }
    public void setAcntPrdtCd(String acntPrdtCd) { this.acntPrdtCd = acntPrdtCd; }
    public TrId getTrId() { return trId; }
    public void setTrId(TrId trId) { this.trId = trId; }
}
