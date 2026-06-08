package autotests;

import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {
    private final String duckServiceUrl = "http://localhost:2222";

    @Bean("duckService")
    public HttpClient duckService() {
        return new HttpClientBuilder()
                .requestUrl(duckServiceUrl)
                .build();
    }
}
