package com.bankingapi.dto;

import java.time.LocalDateTime;

public class ClienteResponseDTO {
    
    private Long id;
    private String nome;
    private String cpf;
    private LocalDateTime dataCriacao;
    
    // Construtores
    public ClienteResponseDTO() {}
    
    public ClienteResponseDTO(Long id, String nome, String cpf, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataCriacao = dataCriacao;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
