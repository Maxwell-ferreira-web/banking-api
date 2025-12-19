package com.bankingapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapi.enums.TipoTransacao;

public class TransacaoResponseDTO {
    
    private Long id;
    private TipoTransacao tipo;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime dataTransacao;
    private Long contaOrigemId;
    private String numeroContaOrigem;
    private Long contaDestinoId;
    private String numeroContaDestino;
    
    public TransacaoResponseDTO() {}
    
    public TransacaoResponseDTO(Long id, TipoTransacao tipo, BigDecimal valor, 
                               String descricao, LocalDateTime dataTransacao) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataTransacao = dataTransacao;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public LocalDateTime getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDateTime dataTransacao) { this.dataTransacao = dataTransacao; }
    
    public Long getContaOrigemId() { return contaOrigemId; }
    public void setContaOrigemId(Long contaOrigemId) { this.contaOrigemId = contaOrigemId; }
    
    public String getNumeroContaOrigem() { return numeroContaOrigem; }
    public void setNumeroContaOrigem(String numeroContaOrigem) { this.numeroContaOrigem = numeroContaOrigem; }
    
    public Long getContaDestinoId() { return contaDestinoId; }
    public void setContaDestinoId(Long contaDestinoId) { this.contaDestinoId = contaDestinoId; }
    
    public String getNumeroContaDestino() { return numeroContaDestino; }
    public void setNumeroContaDestino(String numeroContaDestino) { this.numeroContaDestino = numeroContaDestino; }
}
