package com.bankingapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class NovaContaDTO {
    
    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nomeCliente;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos numéricos")
    private String cpfCliente;
    
    @NotNull(message = "Saldo inicial é obrigatório")
    @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo")
    private BigDecimal saldoInicial;
    
    // Constructors
    public NovaContaDTO() {}
    
    public NovaContaDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.saldoInicial = saldoInicial;
    }
    
    // Getters and Setters
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    
    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    @Override
    public String toString() {
        return "NovaContaDTO{nomeCliente='" + nomeCliente + "', cpfCliente='" + cpfCliente + 
               "', saldoInicial=" + saldoInicial + "}";
    }
}
