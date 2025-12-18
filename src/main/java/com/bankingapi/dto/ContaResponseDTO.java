package com.bankingapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContaResponseDTO {
    private Long id;
    private String numero;
    private BigDecimal saldo;
    private Boolean ativa;
    private String nomeCliente;
    private String cpfCliente;
    private LocalDateTime dataCriacao;
    
    // Construtores
    public ContaResponseDTO() {}
    
    public ContaResponseDTO(Long id, String numero, BigDecimal saldo, Boolean ativa, 
                           String nomeCliente, String cpfCliente, LocalDateTime dataCriacao) {
        this.id = id;
        this.numero = numero;
        this.saldo = saldo;
        this.ativa = ativa;
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.dataCriacao = dataCriacao;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    public Boolean getAtiva() {
        return ativa;
    }
    
    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
    
    public String getNomeCliente() {
        return nomeCliente;
    }
    
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    public String getCpfCliente() {
        return cpfCliente;
    }
    
    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
