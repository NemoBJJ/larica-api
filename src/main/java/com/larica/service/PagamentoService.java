package com.larica.service;

import com.larica.dto.CheckoutPreferenceResponse;
import com.larica.entity.ItemPedido;
import com.larica.entity.Pagamento;
import com.larica.entity.Pedido;
import com.larica.repository.PagamentoRepository;
import com.larica.repository.PedidoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PagamentoService {

    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final RestTemplate http;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${app.base-url}")
    private String baseUrl;

    public PagamentoService(PedidoRepository pedidoRepository,
                            PagamentoRepository pagamentoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.http = buildRestTemplate(); // mantém RestTemplate, agora com timeouts
    }

    private RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(10_000);
        f.setReadTimeout(10_000);
        return new RestTemplate(f);
    }

    /** Prova de credenciais: chama /users/me no MP */
    public Map<String, Object> ping() {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);
        ResponseEntity<Map> resp = http.exchange(
                "https://api.mercadopago.com/users/me",
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Falha no ping ao Mercado Pago");
        }
        Map<String, Object> body = resp.getBody();
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id", body.get("id"));
        out.put("nickname", body.get("nickname"));
        out.put("email", body.get("email"));
        out.put("site_id", body.get("site_id"));
        return out;
    }

    /** Sincroniza status direto no MP usando payment_id (sem webhook) */
    public void atualizarPorPaymentId(String paymentId) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        ResponseEntity<Map> resp = http.exchange(
                "https://api.mercadopago.com/v1/payments/" + paymentId,
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Não consegui consultar pagamento " + paymentId);
        }

        Map<String, Object> p = resp.getBody();
        String status = (String) p.get("status"); // approved, pending, rejected...
        String externalRef = String.valueOf(p.get("external_reference"));
        Long pedidoId = null;
        try { pedidoId = Long.valueOf(externalRef); } catch (Exception ignore) {}

        if (pedidoId != null) {
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

            // Atualiza Pagamento
            pagamentoRepository.findByPedidoId(pedidoId).ifPresent(pag -> {
                pag.setStatus(status != null ? status.toUpperCase() : null);
                pag.setMpPaymentId(String.valueOf(p.get("id")));
                pag.setLastNotification(p.toString());
                pagamentoRepository.save(pag);
            });
        }
    }

    /** Busca tentativas/erros do pagamento por external_reference (pedidoId numérico) */
    public Map<String, Object> buscarTentativasPorPedido(Long pedidoId) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.mercadopago.com/v1/payments/search")
                .queryParam("sort", "date_created")
                .queryParam("criteria", "desc")
                .queryParam("external_reference", pedidoId.toString())
                .build()
                .toUriString();

        ResponseEntity<Map> resp = http.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Falha ao consultar tentativas de pagamento no Mercado Pago");
        }
        return resp.getBody(); // "results": [ {status, status_detail, ...}, ... ]
    }

    /** Busca tentativas/erros por external_reference STRING (ex.: "teste-r1") */
    public Map<String, Object> buscarTentativasPorRef(String externalRef) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.mercadopago.com/v1/payments/search")
                .queryParam("sort", "date_created")
                .queryParam("criteria", "desc")
                .queryParam("external_reference", externalRef)
                .build()
                .toUriString();

        ResponseEntity<Map> resp = http.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Falha ao consultar tentativas por external_reference");
        }
        return resp.getBody();
    }

    /** Busca merchant_order pela preferenceId (lista pagamentos vinculados) */
    public Map<String, Object> buscarMerchantOrderPorPreferenceId(String preferenceId) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.mercadopago.com/merchant_orders")
                .queryParam("preference_id", preferenceId)
                .build()
                .toUriString();

        ResponseEntity<Map> resp = http.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Falha ao consultar merchant_orders por preferenceId");
        }
        return resp.getBody(); // "elements": [ { payments: [...] } ]
    }

    /** Lista pagamentos por janela de data (ISO 8601 com timezone) */
    public Map<String, Object> buscarPagamentosPorData(String beginIso, String endIso, Integer limit) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        UriComponentsBuilder b = UriComponentsBuilder
                .fromHttpUrl("https://api.mercadopago.com/v1/payments/search")
                .queryParam("sort", "date_created")
                .queryParam("criteria", "desc")
                .queryParam("range", "date_created")
                .queryParam("begin_date", beginIso)
                .queryParam("end_date", endIso);

        if (limit != null) b.queryParam("limit", limit);

        String url = b.build().toUriString();

        ResponseEntity<Map> resp = http.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(h),
                Map.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("Falha ao consultar pagamentos por data");
        }
        return resp.getBody();
    }

    /** =============== Cobra R$ 5,00 real (sem pedido) =============== */
    public CheckoutPreferenceResponse criarPreferenceR1() {
        String base = normalizeBase(baseUrl);
        Map<String, Object> pref = new LinkedHashMap<>();

        // Item único de R$ 5,00
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("title", "Teste R$5,00");
        item.put("quantity", 1);
        item.put("unit_price", 5.00); // 5 reais
        item.put("currency_id", "BRL");
        pref.put("items", List.of(item));

        // External ref livre
        pref.put("external_reference", "teste-r1");

        // Prefill de pagador para evitar colar sessão antiga
        Map<String, Object> payer = new LinkedHashMap<>();
        payer.put("email", "pagador+" + System.currentTimeMillis() + "@mailinator.com");
        pref.put("payer", payer);

        // Back urls
        Map<String, String> backUrls = new LinkedHashMap<>();
        backUrls.put("success", base + "/pagamentos/retorno/sucesso");
        backUrls.put("failure", base + "/pagamentos/retorno/falha");
        backUrls.put("pending", base + "/pagamentos/retorno/pendente");
        pref.put("back_urls", backUrls);

        if (backUrls.get("success").startsWith("https://")) {
            pref.put("auto_return", "approved");
        }

        pref.put("notification_url", base + "/webhooks/mercadopago");

        Map<String, Object> body = criarPreferenciaNoMP(pref);

        String initPoint = (String) body.get("init_point");
        String sandboxInitPoint = (String) body.get("sandbox_init_point");
        String preferenceId = (String) body.get("id");

        // NÃO aceitamos sandbox aqui
        if (initPoint == null || initPoint.contains("sandbox.")) {
            throw new RuntimeException("MP retornou sandbox_init_point. Verifique se o token é APP_USR- (produção).");
        }

        return new CheckoutPreferenceResponse(initPoint, sandboxInitPoint, preferenceId);
    }

    /** =============== EXISTENTE: cria preferência por pedido =============== */
    @Transactional
    public CheckoutPreferenceResponse criarPreference(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

        pagamentoRepository.findByPedidoId(pedidoId).ifPresent(p -> {
            throw new RuntimeException("Pedido já possui pagamento associado");
        });

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new RuntimeException("Pedido não possui itens para pagamento");
        }

        String base = normalizeBase(baseUrl);

        // Monta itens e total
        List<Map<String, Object>> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {
            BigDecimal precoUnitario = item.getProduto().getPreco();
            BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);

            Map<String, Object> it = new LinkedHashMap<>();
            it.put("title", item.getProduto().getNome());
            it.put("quantity", item.getQuantidade());
            it.put("unit_price", precoUnitario.doubleValue());
            it.put("currency_id", "BRL");
            items.add(it);
        }

        // Preferência
        Map<String, Object> pref = new LinkedHashMap<>();
        pref.put("items", items);
        pref.put("external_reference", pedidoId.toString());

        Map<String, String> backUrls = new LinkedHashMap<>();
        backUrls.put("success", base + "/pagamentos/retorno/sucesso");
        backUrls.put("failure", base + "/pagamentos/retorno/falha");
        backUrls.put("pending", base + "/pagamentos/retorno/pendente");
        pref.put("back_urls", backUrls);

        if (backUrls.get("success").startsWith("https://")) {
            pref.put("auto_return", "approved");
        }

        pref.put("notification_url", base + "/webhooks/mercadopago");

        Map<String, Object> body = criarPreferenciaNoMP(pref);

        String initPoint = (String) body.get("init_point");
        String sandboxInitPoint = (String) body.get("sandbox_init_point");
        String preferenceId = (String) body.get("id");

        if (initPoint == null || initPoint.contains("sandbox.")) {
            throw new RuntimeException("MP retornou sandbox_init_point. Verifique se o token é APP_USR- (produção).");
        }

        // Persiste pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(pedidoId);
        pagamento.setPreferenceId(preferenceId);
        pagamento.setInitPoint(initPoint);
        pagamento.setSandboxInitPoint(sandboxInitPoint);
        pagamento.setStatus("PENDENTE");
        pagamento.setValorTotal(total);
        pagamento.setMoeda("BRL");
        pagamentoRepository.save(pagamento);

        return new CheckoutPreferenceResponse(initPoint, sandboxInitPoint, preferenceId);
    }

    // ----------------- helpers -----------------

    private String normalizeBase(String b) {
        if (b == null) return "";
        return b.endsWith("/") ? b.substring(0, b.length() - 1) : b;
    }

    private Map<String, Object> criarPreferenciaNoMP(Map<String, Object> pref) {
        try {
            try { System.out.println("MP preference JSON -> " + mapper.writeValueAsString(pref)); } catch (Exception ignore) {}

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            ResponseEntity<Map> response = http.postForEntity(
                    "https://api.mercadopago.com/checkout/preferences",
                    new HttpEntity<>(pref, headers),
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (!response.getStatusCode().is2xxSuccessful() || body == null) {
                throw new RuntimeException("Falha ao criar preferência no Mercado Pago");
            }
            return body;
        } catch (HttpClientErrorException e) {
            String detalhe = e.getResponseBodyAsString();
            throw new RuntimeException("Erro MP (" + e.getStatusCode().value() + "): " + detalhe, e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com Mercado Pago: " + e.getMessage(), e);
        }
    }
}
