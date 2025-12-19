package com.bankingapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ContaBancariaRequestDTO {
    
    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;
    
    @NotBlank(message = "CPF do cliente é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpfCliente;
    
    @NotNull(message = "Saldo inicial é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "Saldo inicial deve ser maior ou igual a zero")
    private BigDecimal saldoInicial;
    
    @Pattern(regexp = "CORRENTE|POUPANCA", message = "Tipo deve ser CORRENTE ou POUPANCA")
    private String tipoConta = "CORRENTE";
    
    public ContaBancariaRequestDTO() {}
    
    public ContaBancariaRequestDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.saldoInicial = saldoInicial;
    }
    
    public ContaBancariaRequestDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial, String tipoConta) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.saldoInicial = saldoInicial;
        this.tipoConta = tipoConta;
    }
    
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    
    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
    
    public String getTipoConta() { return tipoConta; }
    public void setTipoConta(String tipoConta) { this.tipoConta = tipoConta; }
}
