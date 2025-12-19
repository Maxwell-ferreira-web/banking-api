package com.bankingapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bankingapi.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCpf(String cpf);
    
    boolean existsByCpf(String cpf);
    
    @Query("SELECT c FROM Cliente c WHERE UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
    Optional<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
}
