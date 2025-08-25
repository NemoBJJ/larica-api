// src/main/java/com/larica/controller/WebhookController.java
package com.larica.controller;

import com.larica.entity.Pagamento;
import com.larica.entity.Pedido;
import com.larica.repository.PagamentoRepository;
import com.larica.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/webhooks/mercadopago")
public class WebhookController {

    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final RestTemplate http;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    public WebhookController(PedidoRepository pedidoRepository,
                             PagamentoRepository pagamentoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.http = new RestTemplate();
    }

    @PostMapping
    public ResponseEntity<Void> receber(@RequestBody(required = false) Map<String, Object> body,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(name = "data.id", required = false) String dataId,
                                        @RequestParam(name = "data_id", required = false) String dataIdLegacy) {
        try {
            String paymentId = dataId != null ? dataId : dataIdLegacy;

            if ("payment".equals(type) && paymentId != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);
                headers.set("Content-Type", "application/json");

                ResponseEntity<Map> response = http.exchange(
                    "https://api.mercadopago.com/v1/payments/" + paymentId,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> paymentData = response.getBody();

                    String status = (String) paymentData.get("status");
                    
                    // 🔍 BUSCA O external_reference EM VÁRIOS LUGARES
                    String externalRef = null;
                    
                    // 1° Tentativa: direto no root
                    if (paymentData.containsKey("external_reference")) {
                        externalRef = String.valueOf(paymentData.get("external_reference"));
                    } 
                    // 2° Tentativa: dentro de 'additional_info'
                    else if (paymentData.containsKey("additional_info")) {
                        Map<String, Object> additionalInfo = (Map<String, Object>) paymentData.get("additional_info");
                        if (additionalInfo != null && additionalInfo.containsKey("external_reference")) {
                            externalRef = String.valueOf(additionalInfo.get("external_reference"));
                        }
                    }
                    // 3° Tentativa: dentro de 'metadata'  
                    else if (paymentData.containsKey("metadata")) {
                        Map<String, Object> metadata = (Map<String, Object>) paymentData.get("metadata");
                        if (metadata != null && metadata.containsKey("external_reference")) {
                            externalRef = String.valueOf(metadata.get("external_reference"));
                        }
                    }
                    // 4° Tentativa: dentro de 'order'
                    else if (paymentData.containsKey("order")) {
                        Map<String, Object> order = (Map<String, Object>) paymentData.get("order");
                        if (order != null && order.containsKey("external_reference")) {
                            externalRef = String.valueOf(order.get("external_reference"));
                        }
                    }

                    // Se não achou o external_reference, loga e sai
                    if (externalRef == null || externalRef.equals("null")) {
                        System.err.println("❌ external_reference NÃO ENCONTRADO no payload: " + paymentData);
                        return ResponseEntity.ok().build();
                    }

                    Long pedidoId = Long.parseLong(externalRef);

                    // Atualiza Pedido
                    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
                    if (pedido != null) {
                        switch (status) {
                            case "approved" -> pedido.setStatus("PAGO");
                            case "pending", "in_process" -> pedido.setStatus("AGUARDANDO_PAGAMENTO");
                            case "cancelled", "rejected", "refunded" -> pedido.setStatus("CANCELADO");
                            default -> pedido.setStatus("PENDENTE");
                        }
                        pedidoRepository.save(pedido);
                    }

                    // Atualiza Pagamento (busca por pedido)
                    Optional<Pagamento> optPagamento = pagamentoRepository.findByPedidoId(pedidoId);
                    if (optPagamento.isPresent()) {
                        Pagamento pagamento = optPagamento.get();
                        pagamento.setStatus(status != null ? status.toUpperCase() : "PENDENTE");
                        pagamento.setMpPaymentId(paymentId);
                        pagamento.setLastNotification(paymentData.toString());
                        pagamentoRepository.save(pagamento);
                    }
                }
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("❌ Erro no webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok().build(); // Sempre retorna 200 para o MP
        }
    }
}