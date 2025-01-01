package com.fluxo_caixa.user_services.adapters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientAdapter implements HttpClientAdapter {
    private final WebClient webClient;

    public WebClientAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public <T> T post(String url, Object request, Class<T> responseType, String token) {
        return webClient.post()
            .uri(url)
            .header("Authorization", "Bearer " + token)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(responseType)
            .block();
    }

    @Override
    public <T> T put(String url, Object request, Class<T> responseType, String token) {
        webClient.put()
            .uri(url)
            .header("Authorization", "Bearer " + token)
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity()
            .block();
        return null;
    }
}
