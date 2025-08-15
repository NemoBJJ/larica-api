package com.larica.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.maps.api.key}")
    private String apiKey;

    public GeoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Coordenadas obterCoordenadasPorEndereco(String endereco) {
        try {
            String enderecoFormatado = endereco.replace(" ", "+");
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + enderecoFormatado + "&key=" + apiKey;

            String respostaJson = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(respostaJson);

            JsonNode results = root.path("results");
            if (!results.isArray() || results.isEmpty()) {
                throw new RuntimeException("Endereço não encontrado pelo Google Maps.");
            }

            JsonNode locationNode = results.get(0).path("geometry").path("location");
            double lat = locationNode.path("lat").asDouble();
            double lng = locationNode.path("lng").asDouble();

            System.out.printf("📍 Coordenadas obtidas para '%s': lat=%.6f, lng=%.6f%n", endereco, lat, lng);

            return new Coordenadas(lat, lng);

        } catch (Exception e) {
            System.err.println("❌ Erro ao obter coordenadas: " + e.getMessage());
            throw new RuntimeException("Erro ao obter coordenadas: " + e.getMessage());
        }
    }

    public static class Coordenadas {
        private double latitude;
        private double longitude;

        public Coordenadas(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "Latitude: " + latitude + ", Longitude: " + longitude;
        }
    }
}
