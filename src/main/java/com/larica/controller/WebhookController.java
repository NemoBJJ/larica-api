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
                HttpHeaders h = new HttpHeaders();
                h.setBearerAuth(accessToken);

                ResponseEntity<Map> resp = http.exchange(
                        "https://api.mercadopago.com/v1/payments/" + paymentId,
                        HttpMethod.GET,
                        new HttpEntity<>(h),
                        Map.class
                );

                if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                    Map<String, Object> p = resp.getBody();

                    String status = (String) p.get("status"); // approved, pending, cancelled, rejected...
                    String externalRef = String.valueOf(p.get("external_reference"));
                    String prefId = (String) p.get("order") != null ? null : null; // opcional, nem sempre vem aqui
                    Long pedidoId = Long.valueOf(externalRef);

                    // Atualiza Pedido
                    Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
                    if (pedido != null) {
                        switch (status) {
                            case "approved" -> pedido.setStatus("PAGO");
                            case "pending" -> pedido.setStatus("AGUARDANDO_PAGAMENTO");
                            case "cancelled", "rejected" -> pedido.setStatus("CANCELADO");
                            default -> { /* mantém */ }
                        }
                        pedidoRepository.save(pedido);
                    }

                    // Atualiza Pagamento (busca por pedido)
                    Optional<Pagamento> optPag = pagamentoRepository.findByPedidoId(pedidoId);
                    if (optPag.isPresent()) {
                        Pagamento pagamento = optPag.get();
                        pagamento.setStatus(status != null ? status.toUpperCase() : null);
                        pagamento.setMpPaymentId(String.valueOf(p.get("id")));
                        pagamento.setLastNotification(p.toString());
                        // preferenceId normalmente você tem salvo desde a criação;
                        // se quiser reforçar por segurança, poderia tentar extrair do payload.
                        pagamentoRepository.save(pagamento);
                    }
                }
            }

            // SEMPRE responder 200
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // logaria o erro aqui, mas para o MP não ficar reintentando sem fim, retornamos 200
            return ResponseEntity.ok().build();
        }
    }
}
