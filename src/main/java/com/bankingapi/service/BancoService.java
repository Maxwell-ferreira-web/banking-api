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
    
    @Transactional(readOnly = true)
    public List<ContaBancaria> listarContas() {
        return contaBancariaRepository.findAll(); // CORRIGIDO
    }
    
    @Transactional(readOnly = true)
    public ContaBancaria buscarConta(Long id) {
        return contaBancariaRepository.findById(id) // CORRIGIDO
                .orElseThrow(() -> new NotFoundException("Conta", id));
    }
    
    public ContaBancaria criarConta(NovaContaDTO dto) {
        validarCpf(dto.getCpfCliente());
        
        Cliente cliente = clienteRepository.findByCpf(dto.getCpfCliente())
                .orElse(new Cliente(dto.getNomeCliente(), dto.getCpfCliente()));
        
        if (cliente.getId() == null) {
            cliente = clienteRepository.save(cliente);
        }
        
        String numeroConta = gerarNumeroConta();
        
        ContaBancaria conta = new ContaBancaria(numeroConta, cliente, dto.getSaldoInicial());
        conta = contaBancariaRepository.save(conta); // CORRIGIDO
        
        if (dto.getSaldoInicial().compareTo(BigDecimal.ZERO) > 0) {
            registrarTransacao(null, conta, TipoTransacao.DEPOSITO, // CORRIGIDO
                             dto.getSaldoInicial(), "Depósito inicial");
        }
        
        return conta;
    }
    
    public void depositar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        
        conta.depositar(dto.getValor());
        contaBancariaRepository.save(conta); 
        
        registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
                         dto.getValor(), dto.getDescricao());
    }
    
    public void sacar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        
        conta.sacar(dto.getValor());
        contaBancariaRepository.save(conta); 
        
        registrarTransacao(conta, null, TipoTransacao.SAQUE, 
                         dto.getValor(), dto.getDescricao());
    }
    
    public void transferir(TransferenciaDTO dto) {
        if (dto.getContaOrigemId().equals(dto.getContaDestinoId())) {
            throw new BusinessException("SAME_ACCOUNT", 
                "Conta origem e destino não podem ser iguais");
        }
        
        ContaBancaria contaOrigem = buscarConta(dto.getContaOrigemId());
        ContaBancaria contaDestino = buscarConta(dto.getContaDestinoId());
        
        contaOrigem.sacar(dto.getValor());
        contaDestino.depositar(dto.getValor());
        
        contaBancariaRepository.save(contaOrigem); 
        contaBancariaRepository.save(contaDestino); 
        
        registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA, 
                         dto.getValor(), dto.getDescricao());
    }
    
    @Transactional(readOnly = true)
    public String exportar() {
        List<ContaBancaria> contas = contaBancariaRepository.findAll(); 
        return csvExporter.exportarContas(contas);
    }
    
    @Transactional(readOnly = true)
    public List<Transacao> buscarHistorico(Long contaId) {
        ContaBancaria conta = buscarConta(contaId);
        return transacaoRepository.findByContaOrderByDataTransacaoDesc(conta); 
    }
    
    private void registrarTransacao(ContaBancaria origem, ContaBancaria destino, 
                                  TipoTransacao tipo, BigDecimal valor, String descricao) { 
        Transacao transacao = new Transacao(origem, destino, tipo, valor, descricao);
        transacaoRepository.save(transacao);
    }
    
    private String gerarNumeroConta() {
        String numero;
        do {
            long timestamp = System.currentTimeMillis();
            numero = String.format("%010d", timestamp % 10000000000L);
        } while (contaBancariaRepository.existsByNumero(numero)); 
        
        return numero;
    }
    
    private void validarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new BusinessException("INVALID_CPF", "CPF deve conter exatamente 11 dígitos numéricos");
        }
        
        if (cpf.matches("(\\d)\\1{10}")) {
            throw new BusinessException("INVALID_CPF", "CPF inválido");
        }
    }
}
