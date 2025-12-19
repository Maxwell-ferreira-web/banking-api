package com.bankingapi.service.interfaces;

import com.bankingapi.dto.*;
import java.util.List;

public interface IContaService {
    
    ContaBancariaResponseDTO criarConta(ContaBancariaRequestDTO request);
    List<ContaBancariaResponseDTO> listarContas();
    ContaBancariaResponseDTO buscarConta(Long id);
    ContaBancariaResponseDTO depositar(Long contaId, TransacaoRequestDTO request);
    ContaBancariaResponseDTO sacar(Long contaId, TransacaoRequestDTO request);
    TransferenciaResponseDTO transferir(TransferenciaRequestDTO request);
    List<TransacaoResponseDTO> listarTransacoes(Long contaId);
    String exportarContasCSV();
}
