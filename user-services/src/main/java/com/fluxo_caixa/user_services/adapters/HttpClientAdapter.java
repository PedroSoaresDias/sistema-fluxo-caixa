package com.fluxo_caixa.user_services.adapters;

public interface HttpClientAdapter {
    <T> T post(String url, Object request, Class<T> responseType, String token);
    <T> T put(String url, Object request, Class<T> responseType, String token);
}
