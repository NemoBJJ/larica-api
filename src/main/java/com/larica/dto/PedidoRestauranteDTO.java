package com.larica.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoRestauranteDTO {
    private Long id;
    private LocalDateTime data;
    private String status;
    private String nomeCliente;
    private String telefoneCliente;
    private List<ItemPedidoDTO> itens;
    private Double total;

    // Construtor
    public PedidoRestauranteDTO(Long id, LocalDateTime data, String status, 
                              String nomeCliente, String telefoneCliente, 
                              List<ItemPedidoDTO> itens, Double total) {
        this.id = id;
        this.data = data;
        this.status = status;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
        this.itens = itens;
        this.total = total;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getData() { return data; }
    public String getStatus() { return status; }
    public String getNomeCliente() { return nomeCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public List<ItemPedidoDTO> getItens() { return itens; }
    public Double getTotal() { return total; }
}