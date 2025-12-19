package com.bankingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bankingapi.entity.ContaBancaria;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    
    List<ContaBancaria> findByAtivaTrue();
    
    Optional<ContaBancaria> findByIdAndAtivaTrue(Long id);
    
    Optional<ContaBancaria> findByNumero(String numero);
    
    @Query("SELECT c FROM ContaBancaria c WHERE c.ativa = true ORDER BY c.dataCriacao DESC")
    List<ContaBancaria> findAllActiveOrderByDataCriacao();
    
    boolean existsByNumero(String numero);
}
