package com.bankingapi.service.interfaces;

import com.bankingapi.dto.*;
import com.bankingapi.entity.*;
import java.math.BigDecimal;
import java.util.List;

public interface IBancoService {
    
    List<ContaBancaria> listarContas();
    ContaBancaria buscarConta(Long id);
    ContaBancaria criarConta(NovaContaDTO dto);
    
    void depositar(Long contaId, OperacaoDTO dto);
    void sacar(Long contaId, OperacaoDTO dto);
    void transferir(TransferenciaDTO dto);
    
    List<Transacao> buscarHistorico(Long contaId);
    String exportar();
    
    void alterarLimiteCredito(Long contaId, BigDecimal novoLimite);
    BigDecimal consultarSaldoComLimite(Long contaId);
    BigDecimal calcularRendimento(Long contaId);
}
