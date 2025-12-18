package com.bankingapi.dto;

import java.math.BigDecimal;

public class ContaRequestDTO {
    private String nomeCliente;
    private String cpfCliente;
    private BigDecimal saldoInicial;
    
    // Construtores
    public ContaRequestDTO() {}
    
    public ContaRequestDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.saldoInicial = saldoInicial;
    }
    
    // Getters e Setters
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
    
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
}
