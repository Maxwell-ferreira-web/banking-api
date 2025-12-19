package com.bankingapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapi.enums.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transacoes")
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    @JsonIgnoreProperties({"contas"})
    private ContaBancaria contaOrigem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    @JsonIgnoreProperties({"contas"})
    private ContaBancaria contaDestino;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTransacao tipo;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime dataTransacao;
    
    @Column(length = 500)
    private String descricao;
    
    public Transacao() {
        this.dataTransacao = LocalDateTime.now();
    }
    
    public Transacao(ContaBancaria contaOrigem, ContaBancaria contaDestino, 
                    TipoTransacao tipo, BigDecimal valor, String descricao) {
        this();
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ContaBancaria getContaOrigem() { return contaOrigem; }
    public void setContaOrigem(ContaBancaria contaOrigem) { this.contaOrigem = contaOrigem; }
    
    public ContaBancaria getContaDestino() { return contaDestino; }
    public void setContaDestino(ContaBancaria contaDestino) { this.contaDestino = contaDestino; }
    
    public TipoTransacao getTipo() { return tipo; }
    public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public LocalDateTime getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDateTime dataTransacao) { this.dataTransacao = dataTransacao; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    @Override
    public String toString() {
        return "Transacao{id=" + id + ", tipo=" + tipo + ", valor=" + valor + "}";
    }
}
