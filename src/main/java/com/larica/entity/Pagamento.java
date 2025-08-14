package com.larica.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "preference_id", length = 100)
    private String preferenceId;

    @Column(name = "init_point", length = 500)
    private String initPoint;

    @Column(name = "sandbox_init_point", length = 500)
    private String sandboxInitPoint;

    @Column(name = "mp_payment_id", length = 100)
    private String mpPaymentId;

    @Column(name = "status", length = 40)
    private String status;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "moeda", length = 10)
    private String moeda;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob
    @Column(name = "last_notification", columnDefinition = "LONGTEXT")
    private String lastNotification;

    public Pagamento() {
    }

    public Pagamento(Long id, Long pedidoId, String preferenceId, String initPoint, String sandboxInitPoint,
                     String mpPaymentId, String status, BigDecimal valorTotal, String moeda,
                     LocalDateTime createdAt, LocalDateTime updatedAt, String lastNotification) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.preferenceId = preferenceId;
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
        this.mpPaymentId = mpPaymentId;
        this.status = status;
        this.valorTotal = valorTotal;
        this.moeda = moeda;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastNotification = lastNotification;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.moeda == null) this.moeda = "BRL";
        if (this.status == null) this.status = "CRIADO";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }

    public String getSandboxInitPoint() {
        return sandboxInitPoint;
    }

    public void setSandboxInitPoint(String sandboxInitPoint) {
        this.sandboxInitPoint = sandboxInitPoint;
    }

    public String getMpPaymentId() {
        return mpPaymentId;
    }

    public void setMpPaymentId(String mpPaymentId) {
        this.mpPaymentId = mpPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(String lastNotification) {
        this.lastNotification = lastNotification;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", preferenceId='" + preferenceId + '\'' +
                ", initPoint='" + initPoint + '\'' +
                ", sandboxInitPoint='" + sandboxInitPoint + '\'' +
                ", mpPaymentId='" + mpPaymentId + '\'' +
                ", status='" + status + '\'' +
                ", valorTotal=" + valorTotal +
                ", moeda='" + moeda + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastNotification='" + lastNotification + '\'' +
                '}';
    }
}