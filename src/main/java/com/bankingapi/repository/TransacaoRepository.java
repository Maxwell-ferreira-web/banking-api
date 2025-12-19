package com.bankingapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bankingapi.entity.ContaBancaria;
import com.bankingapi.entity.Transacao;
import com.bankingapi.enums.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem = :conta OR t.contaDestino = :conta ORDER BY t.dataTransacao DESC")
    List<Transacao> findByContaOrderByDataTransacaoDesc(@Param("conta") ContaBancaria conta);
    
    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem.id = :contaId OR t.contaDestino.id = :contaId ORDER BY t.dataTransacao DESC")
    List<Transacao> findByContaIdOrderByDataTransacaoDesc(@Param("contaId") Long contaId);
    
    List<Transacao> findByTipo(TipoTransacao tipo);
    
    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem.id = :contaId OR t.contaDestino.id = :contaId")
    List<Transacao> findByContaId(@Param("contaId") Long contaId);
}
