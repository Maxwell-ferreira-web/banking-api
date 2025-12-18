package com.bankingapi.repository;

import com.bankingapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca cliente por CPF
     */
    Optional<Cliente> findByCpf(String cpf);
    
    /**
     * Verifica se existe cliente com o CPF informado
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca cliente por nome (case insensitive)
     */
    @Query("SELECT c FROM Cliente c WHERE UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
    Optional<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
}
