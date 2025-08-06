package com.larica.dto;

import java.time.LocalDateTime;

public class PedidoCriadoDTO {
    private Long id;
    private String status;
    private LocalDateTime data;

    public PedidoCriadoDTO(Long id, String status, LocalDateTime data) {
        this.id = id;
        this.status = status;
        this.data = data;
    }

    public Long getId() { return id; }
    public String getStatus() { return status; }
    public LocalDateTime getData() { return data; }
}
