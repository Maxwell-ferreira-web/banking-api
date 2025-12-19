package com.bankingapi.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends ContaBancaria {
    
    @Column(name = "limite_credito", precision = 19, scale = 2)
    private BigDecimal limiteCredito;
    
    @Column(name = "taxa_manutencao", precision = 19, scale = 2)
    private BigDecimal taxaManutencao;
    
    public ContaCorrente() {
        super();
        this.limiteCredito = new BigDecimal("1000.00"); 
        this.taxaManutencao = new BigDecimal("15.00");   
    }
    
    public ContaCorrente(String numero, Cliente cliente, BigDecimal saldoInicial) {
        super(numero, cliente, saldoInicial);
        this.limiteCredito = new BigDecimal("1000.00");
        this.taxaManutencao = new BigDecimal("15.00");
    }
    
    public ContaCorrente(String numero, Cliente cliente, BigDecimal saldoInicial, 
                        BigDecimal limiteCredito) {
        super(numero, cliente, saldoInicial);
        this.limiteCredito = limiteCredito != null ? limiteCredito : new BigDecimal("1000.00");
        this.taxaManutencao = new BigDecimal("15.00");
    }
    
    @Override
    public BigDecimal calcularTaxa() {
        return this.taxaManutencao;
    }
    
    @Override
    public BigDecimal getLimiteCredito() {
        return this.limiteCredito;
    }
    
    @Override
    public String getTipoConta() {
        return "CONTA CORRENTE";
    }
    
    @Override
    public boolean podeTransferir(BigDecimal valor) {
        return getAtiva() && 
               valor != null && 
               valor.compareTo(BigDecimal.ZERO) > 0 &&
               (getSaldo().add(this.limiteCredito)).compareTo(valor) >= 0;
    }
    
    public void alterarLimiteCredito(BigDecimal novoLimite) {
        if (novoLimite == null || novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limite de crédito deve ser positivo ou zero");
        }
        this.limiteCredito = novoLimite;
    }
    
    public BigDecimal getSaldoComLimite() {
        return getSaldo().add(this.limiteCredito);
    }
    
    public boolean isUsandoLimite() {
        return getSaldo().compareTo(BigDecimal.ZERO) < 0;
    }
    
    public BigDecimal getValorUsadoDoLimite() {
        if (isUsandoLimite()) {
            return getSaldo().abs(); 
        }
        return BigDecimal.ZERO;
    }
    
    public void setLimiteCredito(BigDecimal limiteCredito) { 
        this.limiteCredito = limiteCredito; 
    }
    
    public BigDecimal getTaxaManutencao() { 
        return taxaManutencao; 
    }
    
    public void setTaxaManutencao(BigDecimal taxaManutencao) { 
        this.taxaManutencao = taxaManutencao; 
    }
    
    @Override
    public String toString() {
        return "ContaCorrente{" +
                "id=" + getId() +
                ", numero='" + getNumero() + '\'' +
                ", saldo=" + getSaldo() +
                ", limiteCredito=" + limiteCredito +
                ", saldoComLimite=" + getSaldoComLimite() +
                ", cliente=" + (getCliente() != null ? getCliente().getNome() : "null") +
                '}';
    }
}
