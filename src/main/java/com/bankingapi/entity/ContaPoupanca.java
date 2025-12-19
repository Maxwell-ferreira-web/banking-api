package com.bankingapi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends ContaBancaria {
    
    @Column(name = "taxa_rendimento", precision = 5, scale = 4)
    private BigDecimal taxaRendimento;
    
    @Column(name = "ultimo_rendimento")
    private LocalDateTime ultimoRendimento;
    
    @Column(name = "aniversario_conta")
    private Integer aniversarioConta; 
    
    public ContaPoupanca() {
        super();
        this.taxaRendimento = new BigDecimal("0.0070"); // 0.70% ao mês
        this.aniversarioConta = LocalDateTime.now().getDayOfMonth();
        this.ultimoRendimento = LocalDateTime.now();
    }
    
    public ContaPoupanca(String numero, Cliente cliente, BigDecimal saldoInicial) {
        super(numero, cliente, saldoInicial);
        this.taxaRendimento = new BigDecimal("0.0070");
        this.aniversarioConta = LocalDateTime.now().getDayOfMonth();
        this.ultimoRendimento = LocalDateTime.now();
    }
    
    public ContaPoupanca(String numero, Cliente cliente, BigDecimal saldoInicial, 
                        Integer aniversarioConta) {
        super(numero, cliente, saldoInicial);
        this.taxaRendimento = new BigDecimal("0.0070");
        this.aniversarioConta = aniversarioConta != null ? aniversarioConta : 
                               LocalDateTime.now().getDayOfMonth();
        this.ultimoRendimento = LocalDateTime.now();
    }
    
    @Override
    public BigDecimal calcularTaxa() {
        return BigDecimal.ZERO; 
    }
    
    @Override
    public BigDecimal getLimiteCredito() {
        return BigDecimal.ZERO; 
    }
    
    @Override
    public String getTipoConta() {
        return "CONTA POUPANÇA";
    }
    
    @Override
    public boolean podeTransferir(BigDecimal valor) {
        return getAtiva() && 
               valor != null && 
               valor.compareTo(BigDecimal.ZERO) > 0 &&
               getSaldo().compareTo(valor) >= 0; 
    }
    
    @Override
    public void sacar(BigDecimal valor) {
        if (!podeTransferir(valor)) {
            throw new IllegalArgumentException(
                "Saque negado. Poupança não permite saldo negativo. Saldo disponível: " + getSaldo());
        }
        super.sacar(valor); 
    }
    
    public BigDecimal calcularRendimento() {
        return getSaldo().multiply(this.taxaRendimento);
    }
    
    public void aplicarRendimento() {
        if (getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rendimento = calcularRendimento();
            setSaldo(getSaldo().add(rendimento));
            this.ultimoRendimento = LocalDateTime.now();
        }
    }
    
    public boolean isAniversarioConta() {
        return LocalDateTime.now().getDayOfMonth() == this.aniversarioConta;
    }
    
    public boolean podeReceberRendimento() {
        return isAniversarioConta() && 
               (this.ultimoRendimento == null || 
                !this.ultimoRendimento.toLocalDate().equals(LocalDateTime.now().toLocalDate()));
    }
    
    public BigDecimal getRendimentoAcumulado(int meses) {
        BigDecimal saldoAtual = getSaldo();
        BigDecimal rendimentoTotal = BigDecimal.ZERO;
        
        for (int i = 0; i < meses; i++) {
            BigDecimal rendimentoMes = saldoAtual.multiply(this.taxaRendimento);
            rendimentoTotal = rendimentoTotal.add(rendimentoMes);
            saldoAtual = saldoAtual.add(rendimentoMes);
        }
        
        return rendimentoTotal;
    }
    
    public BigDecimal getTaxaRendimento() { 
        return taxaRendimento; 
    }
    
    public void setTaxaRendimento(BigDecimal taxaRendimento) { 
        if (taxaRendimento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de rendimento não pode ser negativa");
        }
        this.taxaRendimento = taxaRendimento; 
    }
    
    public LocalDateTime getUltimoRendimento() { 
        return ultimoRendimento; 
    }
    
    public void setUltimoRendimento(LocalDateTime ultimoRendimento) { 
        this.ultimoRendimento = ultimoRendimento; 
    }
    
    public Integer getAniversarioConta() { 
        return aniversarioConta; 
    }
    
    public void setAniversarioConta(Integer aniversarioConta) {
        if (aniversarioConta < 1 || aniversarioConta > 28) {
            throw new IllegalArgumentException("Aniversário deve estar entre 1 e 28");
        }
        this.aniversarioConta = aniversarioConta; 
    }
    
    @Override
    public String toString() {
        return "ContaPoupanca{" +
                "id=" + getId() +
                ", numero='" + getNumero() + '\'' +
                ", saldo=" + getSaldo() +
                ", taxaRendimento=" + taxaRendimento +
                ", aniversario=" + aniversarioConta +
                ", cliente=" + getCliente().getNome() +
                '}';
    }
}
