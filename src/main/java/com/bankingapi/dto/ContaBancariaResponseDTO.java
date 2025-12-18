package com.bankingapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContaBancariaResponseDTO {
    
    private Long id;
    private String numero;
    private BigDecimal saldo;
    private boolean ativa;
    private LocalDateTime dataCriacao;
    private ClienteResponseDTO cliente;
    
    // Construtores
    public ContaBancariaResponseDTO() {}
    
    public ContaBancariaResponseDTO(Long id, String numero, BigDecimal saldo, 
                                   boolean ativa, LocalDateTime dataCriacao, 
                                   ClienteResponseDTO cliente) {
        this.id = id;
        this.numero = numero;
        this.saldo = saldo;
        this.ativa = ativa;
        this.dataCriacao = dataCriacao;
        this.cliente = cliente;
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
    
    public boolean isAtiva() {
        return ativa;
    }
    
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public ClienteResponseDTO getCliente() {
        return cliente;
    }
    
    public void setCliente(ClienteResponseDTO cliente) {
        this.cliente = cliente;
    }
}
