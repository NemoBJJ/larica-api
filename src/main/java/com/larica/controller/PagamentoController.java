package com.larica.controller;

import com.larica.dto.CheckoutPreferenceResponse;
import com.larica.entity.Pagamento;
import com.larica.repository.PagamentoRepository;
import com.larica.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoController(PagamentoService pagamentoService,
                               PagamentoRepository pagamentoRepository) {
        this.pagamentoService = pagamentoService;
        this.pagamentoRepository = pagamentoRepository;
    }

    // Prova imediata: /users/me
    @GetMapping("/mp/ping")
    public ResponseEntity<?> ping() {
        try {
            return ResponseEntity.ok(pagamentoService.ping());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "erro", e.getMessage()));
        }
    }

    // Cobra R$5,00 real e retorna init_point
    @PostMapping("/teste-r1")
    public ResponseEntity<?> criarPreferenceR1() {
        try {
            CheckoutPreferenceResponse resp = pagamentoService.criarPreferenceR1();
            return ResponseEntity.ok(resp);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Cria preferência por pedido (soma itens do banco)
    @PostMapping("/mercadopago/preference/{pedidoId}")
    public ResponseEntity<?> criarPreference(@PathVariable Long pedidoId) {
        try {
            CheckoutPreferenceResponse response = pagamentoService.criarPreference(pedidoId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Consulta tentativas/erros por pedidoId numérico
    @GetMapping("/mercadopago/search")
    public ResponseEntity<?> buscarTentativas(@RequestParam Long pedidoId) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarTentativasPorPedido(pedidoId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Consulta tentativas/erros por external_reference string (ex.: "teste-r1")
    @GetMapping("/mercadopago/search/ref")
    public ResponseEntity<?> buscarPorRef(@RequestParam String ref) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarTentativasPorRef(ref));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Consulta merchant_order por preferenceId (mostra payments vinculados)
    @GetMapping("/mercadopago/merchant-order")
    public ResponseEntity<?> merchantOrderPorPreference(@RequestParam String preferenceId) {
        try {
            return ResponseEntity.ok(pagamentoService.buscarMerchantOrderPorPreferenceId(preferenceId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Pagamentos de HOJE (timezone São Paulo) no formato que o MP exige
    @GetMapping("/mercadopago/payments/today")
    public ResponseEntity<?> pagamentosHoje(@RequestParam(required = false) Integer limit) {
        try {
            ZoneId zone = ZoneId.of("America/Sao_Paulo");
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            ZonedDateTime now = ZonedDateTime.now(zone);
            ZonedDateTime start = now.toLocalDate().atStartOfDay(zone);
            ZonedDateTime end = start.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);

            String beginIso = start.format(fmt); // ex: 2025-08-13T00:00:00.000-03:00
            String endIso   = end.format(fmt);   // ex: 2025-08-13T23:59:59.999-03:00

            int lim = (limit != null && limit > 0) ? limit : 50;
            return ResponseEntity.ok(pagamentoService.buscarPagamentosPorData(beginIso, endIso, lim));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Força sincronização sem webhook
    @PostMapping("/mp/sync")
    public ResponseEntity<?> sync(@RequestParam("payment_id") String paymentId) {
        try {
            pagamentoService.atualizarPorPaymentId(paymentId);
            return ResponseEntity.ok(Map.of("ok", true, "payment_id", paymentId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "erro", e.getMessage()));
        }
    }

    // Retornos simples (dev)
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

    // Consultas
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
