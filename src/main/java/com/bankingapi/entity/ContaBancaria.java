package com.bankingapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "contas_bancarias")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
public abstract class ContaBancaria {
    
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
    
    public abstract BigDecimal calcularTaxa();
    public abstract BigDecimal getLimiteCredito();
    public abstract String getTipoConta();
    
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
    
    public boolean podeTransferir(BigDecimal valor) {
        return this.ativa && 
               valor != null && 
               valor.compareTo(BigDecimal.ZERO) > 0 &&
               (this.saldo.add(getLimiteCredito())).compareTo(valor) >= 0;
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
        BigDecimal limiteTotal = this.saldo.add(getLimiteCredito());
        if (limiteTotal.compareTo(valor) < 0) {
            throw new IllegalArgumentException(
                "Saldo insuficiente. Saldo disponível: " + limiteTotal);
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
        return "ContaBancaria{id=" + id + ", numero='" + numero + "', tipo='" + getTipoConta() + "', saldo=" + saldo + "}";
    }
}
