package com.fluxo_caixa.auth_services.adapters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class WebClientAdapter implements HttpClientAdapter {
    private final WebClient webClient;

    public WebClientAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public <T> T get(String url, Class<T> responseType) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Erro ao acessar serviço remoto: " + ex.getMessage(), ex);
        }
    }
}
