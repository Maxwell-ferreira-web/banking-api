package com.bankingapi.service;

import com.bankingapi.dto.*;
import com.bankingapi.entity.*;
import com.bankingapi.enums.TipoTransacao;
import com.bankingapi.exception.*;
import com.bankingapi.repository.*;
import com.bankingapi.utils.CsvExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço principal para operações bancárias
 */
@Service
@Transactional
public class BancoService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository; // CORRIGIDO
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    @Autowired
    private CsvExporter csvExporter;
    
    /**
     * Lista todas as contas cadastradas
     */
    @Transactional(readOnly = true)
    public List<ContaBancaria> listarContas() {
        return contaBancariaRepository.findAll(); // CORRIGIDO
    }
    
    /**
     * Busca conta por ID
     */
    @Transactional(readOnly = true)
    public ContaBancaria buscarConta(Long id) {
        return contaBancariaRepository.findById(id) // CORRIGIDO
                .orElseThrow(() -> new NotFoundException("Conta", id));
    }
    
    /**
     * Cria nova conta bancária
     */
    public ContaBancaria criarConta(NovaContaDTO dto) {
        // Validar CPF
        validarCpf(dto.getCpfCliente());
        
        // Buscar ou criar cliente
        Cliente cliente = clienteRepository.findByCpf(dto.getCpfCliente())
                .orElse(new Cliente(dto.getNomeCliente(), dto.getCpfCliente()));
        
        // Salvar cliente se é novo
        if (cliente.getId() == null) {
            cliente = clienteRepository.save(cliente);
        }
        
        // Gerar número único da conta
        String numeroConta = gerarNumeroConta();
        
        // Criar conta
        ContaBancaria conta = new ContaBancaria(numeroConta, cliente, dto.getSaldoInicial());
        conta = contaBancariaRepository.save(conta); // CORRIGIDO
        
        // Registrar transação de depósito inicial se saldo > 0
        if (dto.getSaldoInicial().compareTo(BigDecimal.ZERO) > 0) {
            registrarTransacao(null, conta, TipoTransacao.DEPOSITO, // CORRIGIDO
                             dto.getSaldoInicial(), "Depósito inicial");
        }
        
        return conta;
    }
    
    /**
     * Realiza depósito em conta
     */
    public void depositar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        
        // Realizar depósito
        conta.depositar(dto.getValor());
        contaBancariaRepository.save(conta); 
        
        // Registrar transação
        registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
                         dto.getValor(), dto.getDescricao());
    }
    
    /**
     * Realiza saque de conta
     */
    public void sacar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        
        // Realizar saque
        conta.sacar(dto.getValor());
        contaBancariaRepository.save(conta); 
        
        // Registrar transação
        registrarTransacao(conta, null, TipoTransacao.SAQUE, 
                         dto.getValor(), dto.getDescricao());
    }
    
    /**
     * Realiza transferência entre contas
     */
    public void transferir(TransferenciaDTO dto) {
        // Validar se contas são diferentes
        if (dto.getContaOrigemId().equals(dto.getContaDestinoId())) {
            throw new BusinessException("SAME_ACCOUNT", 
                "Conta origem e destino não podem ser iguais");
        }
        
        // Buscar contas
        ContaBancaria contaOrigem = buscarConta(dto.getContaOrigemId());
        ContaBancaria contaDestino = buscarConta(dto.getContaDestinoId());
        
        // Realizar transferência
        contaOrigem.sacar(dto.getValor());
        contaDestino.depositar(dto.getValor());
        
        // Salvar contas
        contaBancariaRepository.save(contaOrigem); 
        contaBancariaRepository.save(contaDestino); 
        
        // Registrar transação
        registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA, 
                         dto.getValor(), dto.getDescricao());
    }
    
    /**
     * Exporta dados das contas para CSV
     */
    @Transactional(readOnly = true)
    public String exportar() {
        List<ContaBancaria> contas = contaBancariaRepository.findAll(); 
        return csvExporter.exportarContas(contas);
    }
    
    /**
     * Busca histórico de transações de uma conta
     */
    @Transactional(readOnly = true)
    public List<Transacao> buscarHistorico(Long contaId) {
        ContaBancaria conta = buscarConta(contaId);
        return transacaoRepository.findByContaOrderByDataTransacaoDesc(conta); 
    }
    
    // --- MÉTODOS PRIVADOS ---
    
    /**
     * Registra uma transação
     */
    private void registrarTransacao(ContaBancaria origem, ContaBancaria destino, 
                                  TipoTransacao tipo, BigDecimal valor, String descricao) { 
        Transacao transacao = new Transacao(origem, destino, tipo, valor, descricao);
        transacaoRepository.save(transacao);
    }
    
    /**
     * Gera número único para conta
     */
    private String gerarNumeroConta() {
        String numero;
        do {
            // Gera número baseado no timestamp + random
            long timestamp = System.currentTimeMillis();
            numero = String.format("%010d", timestamp % 10000000000L);
        } while (contaBancariaRepository.existsByNumero(numero)); 
        
        return numero;
    }
    
    /**
     * Valida CPF (validação básica)
     */
    private void validarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new BusinessException("INVALID_CPF", "CPF deve conter exatamente 11 dígitos numéricos");
        }
        
        // Verificar se não é CPF com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            throw new BusinessException("INVALID_CPF", "CPF inválido");
        }
    }
}
