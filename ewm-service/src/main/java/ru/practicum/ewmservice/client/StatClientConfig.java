package ru.practicum.ewmservice.client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statclient.client.StatClient;

@Configuration
public class StatClientConfig {

    @Value("${stats-service.url}")
    private String serverUrl;

    @Bean
    StatClient endpointHitClient() {
        return new StatClient(serverUrl, new RestTemplateBuilder());
    }
}
