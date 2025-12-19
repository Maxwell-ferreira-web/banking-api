package com.bankingapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OperacaoDTO {
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @DecimalMax(value = "999999.99", message = "Valor máximo permitido é R$ 999.999,99")
    private BigDecimal valor;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    // Constructors
    public OperacaoDTO() {}
    
    public OperacaoDTO(BigDecimal valor) {
        this.valor = valor;
    }
    
    public OperacaoDTO(BigDecimal valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }
    
    // Getters and Setters
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    @Override
    public String toString() {
        return "OperacaoDTO{valor=" + valor + ", descricao='" + descricao + "'}";
    }
}
