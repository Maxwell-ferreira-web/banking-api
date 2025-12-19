package com.bankingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bankingapi.entity.Cliente;
import com.bankingapi.entity.ContaBancaria;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Long> {
    
    Optional<ContaBancaria> findByNumero(String numero);
    
    boolean existsByNumero(String numero);
    
    List<ContaBancaria> findByClienteOrderByDataCriacaoDesc(Cliente cliente);
    
    List<ContaBancaria> findByAtivaOrderByDataCriacaoDesc(Boolean ativa);
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.cliente.cpf = :cpf")
    List<ContaBancaria> findByClienteCpf(@Param("cpf") String cpf);
}
