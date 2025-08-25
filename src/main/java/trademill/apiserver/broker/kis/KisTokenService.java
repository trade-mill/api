package trademill.apiserver.broker.kis;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import trademill.apiserver.config.KisProperties;

import java.time.Instant;
import java.util.Map;

@Service
@Profile("kis")
@RequiredArgsConstructor
public class KisTokenService {
    private static final Logger log = LoggerFactory.getLogger(KisTokenService.class);

    private final WebClient kisWebClient;
    private final KisProperties props;

    private volatile String cachedToken;
    private volatile Instant expiresAt = Instant.EPOCH;

    public String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
            return cachedToken;
        }
        Map<String, Object> resp = kisWebClient.post()
                .uri("/oauth2/tokenP")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "appkey", props.getAppKey(),
                        "appsecret", props.getAppSecret()
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String token = (String) resp.get("access_token");
        Number exp = (Number) resp.getOrDefault("expires_in", 0);
        this.cachedToken = token;
        this.expiresAt = Instant.now().plusSeconds(exp.longValue());
        log.info("[KIS] access_token issued, expires_in={}s", exp);
        return token;
    }
}
