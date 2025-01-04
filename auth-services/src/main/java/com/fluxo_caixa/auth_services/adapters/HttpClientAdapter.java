package com.fluxo_caixa.auth_services.adapters;

public interface HttpClientAdapter {
    <T> T get(String url, Class<T> responseType);
}
