package trademill.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import trademill.apiserver.config.KisProperties;


@EnableConfigurationProperties(KisProperties.class)
@SpringBootApplication
public class TrademillApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrademillApiApplication.class, args);
	}

}
