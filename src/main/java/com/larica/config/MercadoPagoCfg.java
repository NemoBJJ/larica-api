package com.larica.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoCfg {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("Access token do Mercado Pago não configurado!");
        }
        // Garante PRODUÇÃO
        if (accessToken.startsWith("TEST-")) {
            throw new IllegalStateException("Token de TESTE carregado. Use o de PRODUÇÃO (prefixo APP_USR-).");
        }
        System.out.println("Configurando Mercado Pago com token: " + accessToken.substring(0, 6) + "...");
        MercadoPagoConfig.setAccessToken(accessToken);

        // REMOVIDO: estes métodos não existem na 2.5.0
        // MercadoPagoConfig.setConnectionTimeoutMillis(10000);
        // MercadoPagoConfig.setSocketTimeoutMillis(10000);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
