package com.bankingapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
    
    @Pattern(regexp = "CORRENTE|POUPANCA", message = "Tipo deve ser CORRENTE ou POUPANCA")
    private String tipoConta = "CORRENTE";
    
    public NovaContaDTO() {}
    
    public NovaContaDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.saldoInicial = saldoInicial;
    }
    
    public NovaContaDTO(String nomeCliente, String cpfCliente, BigDecimal saldoInicial, String tipoConta) {
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
    
    @Override
    public String toString() {
        return "NovaContaDTO{nomeCliente='" + nomeCliente + "', cpfCliente='" + cpfCliente + 
               "', saldoInicial=" + saldoInicial + ", tipoConta='" + tipoConta + "'}";
    }
}
