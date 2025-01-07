package com.fluxo_caixa.cash_flow_services.adapters;

public interface HttpClientAdapter {
    <T> T get(String url, Class<T> responseType, String token);
}