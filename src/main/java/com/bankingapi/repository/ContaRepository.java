package com.bankingapi.repository;

import com.bankingapi.entity.ContaBancaria;
import com.bankingapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Long> {
    
    /**
     * Busca conta pelo número
     */
    Optional<ContaBancaria> findByNumero(String numero);
    
    /**
     * Verifica se existe conta com o número informado
     */
    boolean existsByNumero(String numero);
    
    /**
     * Busca contas por cliente
     */
    List<ContaBancaria> findByClienteOrderByDataCriacaoDesc(Cliente cliente);
    
    /**
     * Busca contas ativas
     */
    List<ContaBancaria> findByAtivaOrderByDataCriacaoDesc(Boolean ativa);
    
    /**
     * Busca contas por CPF do cliente
     */
    @Query("SELECT c FROM ContaBancaria c WHERE c.cliente.cpf = :cpf")
    List<ContaBancaria> findByClienteCpf(@Param("cpf") String cpf);
}
