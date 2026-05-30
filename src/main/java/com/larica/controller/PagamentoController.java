package com.larica.controller;

import com.larica.dto.CheckoutPreferenceResponse;
import com.larica.entity.Pagamento;
import com.larica.entity.Pedido;
import com.larica.repository.PagamentoRepository;
import com.larica.repository.PedidoRepository;
import com.larica.service.PagamentoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final RestTemplate http;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    public PagamentoController(PagamentoService pagamentoService,
                               PagamentoRepository pagamentoRepository,
                               PedidoRepository pedidoRepository) {
        this.pagamentoService = pagamentoService;
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.http = new RestTemplate();
    }

    @GetMapping("/mp/ping")
    public ResponseEntity<?> ping() {
        try {
            return ResponseEntity.ok(pagamentoService.ping());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "erro", e.getMessage()));
        }
    }

    @PostMapping("/teste-r1")
    public ResponseEntity<?> criarPreferenceR1() {
        try {
            CheckoutPreferenceResponse resp = pagamentoService.criarPreferenceR1();
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/mercadopago/preference/{pedidoId}")
    public ResponseEntity<?> criarPreference(@PathVariable Long pedidoId) {
        try {
            CheckoutPreferenceResponse response = pagamentoService.criarPreference(pedidoId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/mercadopago/search")
    public ResponseEntity<?> buscarTentativas(@RequestParam Long pedidoId) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarTentativasPorPedido(pedidoId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/mercadopago/search/ref")
    public ResponseEntity<?> buscarPorRef(@RequestParam String ref) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarTentativasPorRef(ref));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/mercadopago/merchant-order")
    public ResponseEntity<?> merchantOrderPorPreference(@RequestParam String preferenceId) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarMerchantOrderPorPreferenceId(preferenceId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/mercadopago/payments/today")
    public ResponseEntity<?> pagamentosHoje(@RequestParam(required = false) Integer limit) {
        try {
            ZoneId zone = ZoneId.of("America/Sao_Paulo");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            ZonedDateTime now = ZonedDateTime.now(zone);
            ZonedDateTime start = now.toLocalDate().atStartOfDay(zone);
            ZonedDateTime end = start.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);

            String beginIso = start.format(fmt);
            String endIso = end.format(fmt);

            int lim = (limit != null && limit > 0) ? limit : 50;
            return ResponseEntity.ok(pagamentoService.buscarPagamentosPorData(beginIso, endIso, lim));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/mp/sync")
    public ResponseEntity<?> sync(@RequestParam("payment_id") String paymentId) {
        try {
            pagamentoService.atualizarPorPaymentId(paymentId);
            return ResponseEntity.ok(Map.of("ok", true, "payment_id", paymentId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "erro", e.getMessage()));
        }
    }

    // 🔥 NOVO ENDPOINT - Sincronizar pedido pelo ID
    @PostMapping("/sync/pedido/{pedidoId}")
    public ResponseEntity<?> syncPorPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
            if (pedido == null) {
                return ResponseEntity.status(404).body(Map.of("erro", "Pedido não encontrado"));
            }

            String url = "https://api.mercadopago.com/v1/payments/search?external_reference=" + pedidoId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            ResponseEntity<Map> response = http.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
            );
            
            if (response.getBody() != null) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
                
                if (results != null && !results.isEmpty()) {
                    Map<String, Object> payment = results.get(0);
                    String status = (String) payment.get("status");
                    
                    if ("approved".equals(status)) {
                        pedido.setStatus("PAGO");
                        pedidoRepository.save(pedido);
                        return ResponseEntity.ok(Map.of(
                            "ok", true,
                            "status", "PAGO",
                            "message", "Pagamento confirmado!"
                        ));
                    } else {
                        return ResponseEntity.ok(Map.of(
                            "ok", false,
                            "status", status,
                            "message", "Pagamento ainda não aprovado. Status: " + status
                        ));
                    }
                } else {
                    return ResponseEntity.ok(Map.of(
                        "ok", false,
                        "status", "NAO_ENCONTRADO",
                        "message", "Nenhum pagamento encontrado para este pedido"
                    ));
                }
            }
            
            return ResponseEntity.ok(Map.of("ok", false, "message", "Erro ao consultar Mercado Pago"));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/retorno/sucesso")
    public ResponseEntity<?> sucesso(@RequestParam(required = false, name = "payment_id") String paymentId,
                                     @RequestParam(required = false, name = "preference_id") String preferenceId,
                                     @RequestParam(required = false, name = "status") String status) {
        if (paymentId != null) {
            try { pagamentoService.atualizarPorPaymentId(paymentId); } catch (Exception ignore) {}
        }
        return ResponseEntity.ok(Map.of("ok", true, "payment_id", paymentId, "preference_id", preferenceId, "status", status));
    }

    @GetMapping("/retorno/falha")
    public String falha() { return "Pagamento falhou"; }

    @GetMapping("/retorno/pendente")
    public String pendente() { return "Pagamento pendente"; }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> getById(@PathVariable Long id) {
        return pagamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pagamento> getByPedido(@PathVariable Long pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/preference/{preferenceId}")
    public ResponseEntity<Pagamento> getByPreference(@PathVariable String preferenceId) {
        return pagamentoRepository.findByPreferenceId(preferenceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listAll() {
        return ResponseEntity.ok(pagamentoRepository.findAll());
    }
}