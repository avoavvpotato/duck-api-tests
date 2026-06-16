package autotests;

import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class EndpointConfig {
    private final String duckServiceUrl = "http://localhost:2222";

    @Bean("duckService")
    public HttpClient duckService() {
        return new HttpClientBuilder()
                .requestUrl(duckServiceUrl)
                .build();
    }

    @Bean("testDb")
    public SingleConnectionDataSource db() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost:9092/mem:ducks");
        dataSource.setUsername("dev");
        dataSource.setPassword("dev");
        return dataSource;
    }
}
