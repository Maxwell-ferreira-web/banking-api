package com.bankingapi.dto;

import java.math.BigDecimal;

public class TransacaoRequestDTO {
    private BigDecimal valor;
    private String descricao;
    
    // Construtores
    public TransacaoRequestDTO() {}
    
    public TransacaoRequestDTO(BigDecimal valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }
    
    // Getters e Setters
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
