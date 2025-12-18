package com.bankingapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "contas_bancarias")
public class ContaBancaria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String numero;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"contas"})
    private Cliente cliente;
    
    @NotNull(message = "Saldo não pode ser nulo")
    @DecimalMin(value = "0.00", message = "Saldo não pode ser negativo")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "ativa", nullable = false)
    private Boolean ativa;
    
    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transacao> transacoesOrigem = new ArrayList<>();
    
    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transacao> transacoesDestino = new ArrayList<>();
    
    public ContaBancaria() {
        this.dataCriacao = LocalDateTime.now();
        this.saldo = BigDecimal.ZERO;
        this.ativa = true;
    }
    
    public ContaBancaria(String numero, Cliente cliente, BigDecimal saldoInicial) {
        this();
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
    }
    
    // Business Methods
    public void depositar(BigDecimal valor) {
        validarValorPositivo(valor);
        validarContaAtiva();
        this.saldo = this.saldo.add(valor);
    }
    
    public void sacar(BigDecimal valor) {
        validarValorPositivo(valor);
        validarContaAtiva();
        validarSaldoSuficiente(valor);
        this.saldo = this.saldo.subtract(valor);
    }
    
    private void validarValorPositivo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }
    
    private void validarContaAtiva() {
        if (!this.ativa) {
            throw new IllegalStateException("Conta não está ativa");
        }
    }
    
    private void validarSaldoSuficiente(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente. Saldo atual: " + this.saldo);
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
    
    public List<Transacao> getTransacoesOrigem() { return transacoesOrigem; }
    public void setTransacoesOrigem(List<Transacao> transacoesOrigem) { this.transacoesOrigem = transacoesOrigem; }
    
    public List<Transacao> getTransacoesDestino() { return transacoesDestino; }
    public void setTransacoesDestino(List<Transacao> transacoesDestino) { this.transacoesDestino = transacoesDestino; }
    
    @Override
    public String toString() {
        return "ContaBancaria{id=" + id + ", numero='" + numero + "', saldo=" + saldo + "}";
    }
}
