package com.bankingapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class ContaBancariaRequestDTO {
    
    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;
    
    @NotBlank(message = "CPF do cliente é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpfCliente;
    
    @NotNull(message = "Saldo inicial é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "Saldo inicial deve ser maior ou igual a zero")
    private BigDecimal saldoInicial;
    
    // Construtores
    public ContaBancariaRequestDTO() {}
    
    public ContaBancariaRequestDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial) {
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
