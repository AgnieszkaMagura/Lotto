package pl.lotto.http.numbergenerator;

import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;
import pl.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

public class RandomGeneratorRestTemplateTestConfig extends RandomGeneratorClientConfig {

    public RandomNumberGenerable remoteNumberGeneratorClient(int port, int connectionTimeout, int readTimeout) {
        RandomNumberGeneratorRestTemplateConfigurationProperties properties =
                RandomNumberGeneratorRestTemplateConfigurationProperties.builder()
                        .uri("http://localhost")
                        .port(port)
                        .connectionTimeout(connectionTimeout)
                        .readTimeout(readTimeout)
                        .build();

        RestTemplate restTemplate = restTemplate(errorHandler(), properties);
        return remoteNumberGeneratorClient(restTemplate, properties);
    }
}
