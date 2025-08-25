package com.larica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuração padrão para sua API
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:3000", 
                    "https://larica.neemindev.com"  // SEU FRONTEND
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        
        // ✅ CONFIGURAÇÃO ESPECÍFICA PARA WEBHOOKS (MP não envia Origin header)
        registry.addMapping("/webhooks/mercadopago/**")
                .allowedOrigins("*") // Libera TODAS as origens
                .allowedMethods("POST")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}