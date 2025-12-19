package com.bankingapi.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankingapi.dto.NovaContaDTO;
import com.bankingapi.dto.OperacaoDTO;
import com.bankingapi.dto.TransferenciaDTO;
import com.bankingapi.entity.Cliente;
import com.bankingapi.entity.ContaBancaria;
import com.bankingapi.entity.ContaCorrente;
import com.bankingapi.entity.ContaPoupanca;
import com.bankingapi.entity.Transacao;
import com.bankingapi.enums.TipoTransacao;
import com.bankingapi.exception.BusinessException;
import com.bankingapi.exception.NotFoundException;
import com.bankingapi.repository.ClienteRepository;
import com.bankingapi.repository.ContaBancariaRepository;
import com.bankingapi.repository.TransacaoRepository;
import com.bankingapi.service.interfaces.IBancoService;
import com.bankingapi.utils.CsvExporter;

@Service
@Transactional
public class BancoService implements IBancoService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    @Autowired
    private CsvExporter csvExporter;
    
    @Override
    @Transactional(readOnly = true)
    public List<ContaBancaria> listarContas() {
        return contaBancariaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ContaBancaria buscarConta(Long id) {
        return contaBancariaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada", id));
    }
    
    @Override
    public ContaBancaria criarConta(NovaContaDTO dto) {
        validarCpf(dto.getCpfCliente());
        
        Cliente cliente = clienteRepository.findByCpf(dto.getCpfCliente())
                .orElse(new Cliente(dto.getNomeCliente(), dto.getCpfCliente()));
        
        if (cliente.getId() == null) {
            cliente = clienteRepository.save(cliente);
        }
        
        String numeroConta = gerarNumeroConta();
        
        ContaBancaria conta = criarContaEspecifica(dto.getTipoConta(), numeroConta, cliente, dto.getSaldoInicial());
        conta = contaBancariaRepository.save(conta);
        
        if (dto.getSaldoInicial().compareTo(BigDecimal.ZERO) > 0) {
            registrarTransacao(null, conta, TipoTransacao.DEPOSITO,
                             dto.getSaldoInicial(), "Depósito inicial");
        }
        
        return conta;
    }
    
    @Override
    public void depositar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        conta.depositar(dto.getValor());
        contaBancariaRepository.save(conta);
        registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
                         dto.getValor(), dto.getDescricao());
    }
    
    @Override
    public void sacar(Long contaId, OperacaoDTO dto) {
        ContaBancaria conta = buscarConta(contaId);
        conta.sacar(dto.getValor());
        contaBancariaRepository.save(conta);
        registrarTransacao(conta, null, TipoTransacao.SAQUE, 
                         dto.getValor(), dto.getDescricao());
    }
    
    @Override
    public void transferir(TransferenciaDTO dto) {
        if (dto.getContaOrigemId().equals(dto.getContaDestinoId())) {
            throw new BusinessException("SAME_ACCOUNT", 
                "Conta origem e destino não podem ser iguais");
        }
        
        ContaBancaria contaOrigem = buscarConta(dto.getContaOrigemId());
        ContaBancaria contaDestino = buscarConta(dto.getContaDestinoId());
        
        if (!contaOrigem.podeTransferir(dto.getValor())) {
            throw new BusinessException("TRANSFER_NOT_ALLOWED", 
                "Transferência não permitida para esta conta");
        }
        
        contaOrigem.sacar(dto.getValor());
        contaDestino.depositar(dto.getValor());
        
        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);
        
        registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA, 
                         dto.getValor(), dto.getDescricao());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Transacao> buscarHistorico(Long contaId) {
        ContaBancaria conta = buscarConta(contaId);
        return transacaoRepository.findByContaOrderByDataTransacaoDesc(conta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String exportar() {
        List<ContaBancaria> contas = contaBancariaRepository.findAll();
        return csvExporter.exportarContas(contas);
    }
    
    @Override
    public void alterarLimiteCredito(Long contaId, BigDecimal novoLimite) {
        ContaBancaria conta = buscarConta(contaId);
        
        if (conta instanceof ContaCorrente) {
            ContaCorrente contaCorrente = (ContaCorrente) conta;
            contaCorrente.setLimiteCredito(novoLimite);
            contaBancariaRepository.save(contaCorrente);
        } else {
            throw new BusinessException("LIMIT_NOT_SUPPORTED", 
                "Limite de crédito disponível apenas para Conta Corrente");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal consultarSaldoComLimite(Long contaId) {
        ContaBancaria conta = buscarConta(contaId);
        return conta.getSaldo().add(conta.getLimiteCredito());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularRendimento(Long contaId) {
        ContaBancaria conta = buscarConta(contaId);
        
        if (conta instanceof ContaPoupanca) {
            ContaPoupanca contaPoupanca = (ContaPoupanca) conta;
            return contaPoupanca.calcularRendimento();
        } else {
            throw new BusinessException("INCOME_NOT_SUPPORTED", 
                "Rendimento disponível apenas para Conta Poupança");
        }
    }
    
    private ContaBancaria criarContaEspecifica(String tipoConta, String numero, 
                                             Cliente cliente, BigDecimal saldoInicial) {
        if (tipoConta == null) {
            tipoConta = "CORRENTE";
        }
        
        switch (tipoConta.toUpperCase()) {
            case "CORRENTE":
                return new ContaCorrente(numero, cliente, saldoInicial);
            case "POUPANCA":
                return new ContaPoupanca(numero, cliente, saldoInicial);
            default:
                throw new BusinessException("INVALID_ACCOUNT_TYPE", 
                    "Tipo de conta inválido: " + tipoConta + ". Use: CORRENTE ou POUPANCA");
        }
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
