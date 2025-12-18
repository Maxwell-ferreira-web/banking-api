package com.bankingapi.dto;

import java.math.BigDecimal;

public class TransferenciaRequestDTO {
    private Long contaOrigemId;
    private Long contaDestinoId;
    private BigDecimal valor;
    private String descricao;
    
    // Construtores
    public TransferenciaRequestDTO() {}
    
    public TransferenciaRequestDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, String descricao) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
        this.descricao = descricao;
    }
    
    // Getters e Setters
    public Long getContaOrigemId() {
        return contaOrigemId;
    }
    
    public void setContaOrigemId(Long contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }
    
    public Long getContaDestinoId() {
        return contaDestinoId;
    }
    
    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
