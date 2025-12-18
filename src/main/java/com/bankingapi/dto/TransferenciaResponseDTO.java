package com.bankingapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferenciaResponseDTO {
    
    private Long transacaoOrigemId;
    private Long transacaoDestinoId;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime dataTransferencia;
    private ContaBancariaResponseDTO contaOrigem;
    private ContaBancariaResponseDTO contaDestino;
    
    // Construtores
    public TransferenciaResponseDTO() {}
    
    public TransferenciaResponseDTO(Long transacaoOrigemId, Long transacaoDestinoId, 
                                   BigDecimal valor, String descricao, 
                                   LocalDateTime dataTransferencia,
                                   ContaBancariaResponseDTO contaOrigem, 
                                   ContaBancariaResponseDTO contaDestino) {
        this.transacaoOrigemId = transacaoOrigemId;
        this.transacaoDestinoId = transacaoDestinoId;
        this.valor = valor;
        this.descricao = descricao;
        this.dataTransferencia = dataTransferencia;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }
    
    // Getters e Setters
    public Long getTransacaoOrigemId() {
        return transacaoOrigemId;
    }
    
    public void setTransacaoOrigemId(Long transacaoOrigemId) {
        this.transacaoOrigemId = transacaoOrigemId;
    }
    
    public Long getTransacaoDestinoId() {
        return transacaoDestinoId;
    }
    
    public void setTransacaoDestinoId(Long transacaoDestinoId) {
        this.transacaoDestinoId = transacaoDestinoId;
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
    
    public LocalDateTime getDataTransferencia() {
        return dataTransferencia;
    }
    
    public void setDataTransferencia(LocalDateTime dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }
    
    public ContaBancariaResponseDTO getContaOrigem() {
        return contaOrigem;
    }
    
    public void setContaOrigem(ContaBancariaResponseDTO contaOrigem) {
        this.contaOrigem = contaOrigem;
    }
    
    public ContaBancariaResponseDTO getContaDestino() {
        return contaDestino;
    }
    
    public void setContaDestino(ContaBancariaResponseDTO contaDestino) {
        this.contaDestino = contaDestino;
    }
}
