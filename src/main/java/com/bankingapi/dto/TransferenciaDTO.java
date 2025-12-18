package com.bankingapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransferenciaDTO {
    
    @NotNull(message = "ID da conta origem é obrigatório")
    @Positive(message = "ID da conta origem deve ser positivo")
    private Long contaOrigemId;
    
    @NotNull(message = "ID da conta destino é obrigatório")
    @Positive(message = "ID da conta destino deve ser positivo")
    private Long contaDestinoId;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @DecimalMax(value = "999999.99", message = "Valor máximo permitido é R$ 999.999,99")
    private BigDecimal valor;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    // Constructors
    public TransferenciaDTO() {}
    
    public TransferenciaDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
    }
    
    public TransferenciaDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, String descricao) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
        this.descricao = descricao;
    }
    
    // Getters and Setters
    public Long getContaOrigemId() { return contaOrigemId; }
    public void setContaOrigemId(Long contaOrigemId) { this.contaOrigemId = contaOrigemId; }
    
    public Long getContaDestinoId() { return contaDestinoId; }
    public void setContaDestinoId(Long contaDestinoId) { this.contaDestinoId = contaDestinoId; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    @Override
    public String toString() {
        return "TransferenciaDTO{contaOrigemId=" + contaOrigemId + 
               ", contaDestinoId=" + contaDestinoId + ", valor=" + valor + 
               ", descricao='" + descricao + "'}";
    }
}
