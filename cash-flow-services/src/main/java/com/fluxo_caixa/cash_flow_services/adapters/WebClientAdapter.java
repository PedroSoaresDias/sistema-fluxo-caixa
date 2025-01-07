package com.fluxo_caixa.cash_flow_services.adapters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class WebClientAdapter implements HttpClientAdapter {

    private final WebClient webClient;

    private WebClientAdapter(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @Override
    public <T> T get(String url, Class<T> responseType, String token) {
        try {
            return webClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException("Erro ao acessar serviço remoto: " + ex.getMessage(), ex);
        }
    }
}