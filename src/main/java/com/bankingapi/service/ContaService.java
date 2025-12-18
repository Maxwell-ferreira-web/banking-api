package com.bankingapi.service;

import com.bankingapi.dto.*;
import com.bankingapi.entity.*;
import com.bankingapi.enums.TipoTransacao;
import com.bankingapi.exception.*;
import com.bankingapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    public ContaBancariaResponseDTO criarConta(ContaBancariaRequestDTO request) {
        // Buscar cliente existente ou criar novo
        Cliente cliente = clienteRepository.findByCpf(request.getCpfCliente())
                .orElse(null);
        
        // Se cliente não existe, criar novo
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setNome(request.getNomeCliente());
            cliente.setCpf(request.getCpfCliente());
            cliente.setDataCriacao(LocalDateTime.now());
            cliente = clienteRepository.save(cliente);
        }
        
        // Criar conta
        ContaBancaria conta = new ContaBancaria();
        conta.setNumero(gerarNumeroConta());
        conta.setSaldo(request.getSaldoInicial());
        conta.setAtiva(true);
        conta.setCliente(cliente);
        conta.setDataCriacao(LocalDateTime.now());
        conta = contaBancariaRepository.save(conta);
        
        // Registrar transação inicial se houver saldo
        if (request.getSaldoInicial().compareTo(BigDecimal.ZERO) > 0) {
            registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
                request.getSaldoInicial(), "Depósito inicial");
        }
        
        return convertToResponseDTO(conta);
    }
    
    public List<ContaBancariaResponseDTO> listarContas() {
        return contaBancariaRepository.findByAtivaTrue()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public ContaBancariaResponseDTO buscarConta(Long id) {
        ContaBancaria conta = contaBancariaRepository.findByIdAndAtivaTrue(id)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + id));
        return convertToResponseDTO(conta);
    }
    
    public ContaBancariaResponseDTO depositar(Long contaId, TransacaoRequestDTO request) {
        ContaBancaria conta = buscarContaPorId(contaId);
        
        // Usar método de negócio da entidade
        conta.depositar(request.getValor());
        conta = contaBancariaRepository.save(conta);
        
        registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
            request.getValor(), request.getDescricao());
        
        return convertToResponseDTO(conta);
    }
    
    public ContaBancariaResponseDTO sacar(Long contaId, TransacaoRequestDTO request) {
        ContaBancaria conta = buscarContaPorId(contaId);
        
        // Usar método de negócio da entidade
        conta.sacar(request.getValor());
        conta = contaBancariaRepository.save(conta);
        
        registrarTransacao(conta, null, TipoTransacao.SAQUE, 
            request.getValor(), request.getDescricao());
        
        return convertToResponseDTO(conta);
    }
    
    public TransferenciaResponseDTO transferir(TransferenciaRequestDTO request) {
        ContaBancaria contaOrigem = buscarContaPorId(request.getContaOrigemId());
        ContaBancaria contaDestino = buscarContaPorId(request.getContaDestinoId());
        
        if (request.getContaOrigemId().equals(request.getContaDestinoId())) {
            throw new BusinessException("Conta origem e destino não podem ser iguais");
        }
        
        // Usar métodos de negócio das entidades
        contaOrigem.sacar(request.getValor());
        contaDestino.depositar(request.getValor());
        
        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);
        
        // Registrar UMA transação de transferência
        registrarTransacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA, 
            request.getValor(), request.getDescricao());
        
        TransferenciaResponseDTO response = new TransferenciaResponseDTO();
        response.setContaOrigem(convertToResponseDTO(contaOrigem));
        response.setContaDestino(convertToResponseDTO(contaDestino));
        response.setValor(request.getValor());
        response.setDescricao(request.getDescricao());
        response.setDataTransferencia(LocalDateTime.now());
        
        return response;
    }
    
    public List<TransacaoResponseDTO> listarTransacoes(Long contaId) {
        return transacaoRepository.findByContaIdOrderByDataTransacaoDesc(contaId)
                .stream()
                .map(this::convertToTransacaoResponseDTO)
                .collect(Collectors.toList());
    }
    
    public String exportarContasCSV() {
        List<ContaBancaria> contas = contaBancariaRepository.findByAtivaTrue();
        
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Numero,Saldo,Cliente,CPF,Data Criacao\n");
        
        for (ContaBancaria conta : contas) {
            csv.append(conta.getId()).append(",")
               .append(conta.getNumero()).append(",")
               .append(conta.getSaldo()).append(",")
               .append(conta.getCliente().getNome()).append(",")
               .append(conta.getCliente().getCpf()).append(",")
               .append(conta.getDataCriacao())
               .append("\n");
        }
        
        return csv.toString();
    }
    
    private ContaBancaria buscarContaPorId(Long id) {
        return contaBancariaRepository.findByIdAndAtivaTrue(id)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + id));
    }
    
    private String gerarNumeroConta() {
        Random random = new Random();
        String numero;
        do {
            numero = String.format("%08d", random.nextInt(100000000));
        } while (contaBancariaRepository.findByNumero(numero).isPresent());
        return numero;
    }
    
    private void registrarTransacao(ContaBancaria origem, ContaBancaria destino, 
                                  TipoTransacao tipo, BigDecimal valor, String descricao) {
        Transacao transacao = new Transacao();
        transacao.setContaOrigem(origem);
        transacao.setContaDestino(destino);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDescricao(descricao);
        transacao.setDataTransacao(LocalDateTime.now());
        transacaoRepository.save(transacao);
    }
    
    private ContaBancariaResponseDTO convertToResponseDTO(ContaBancaria conta) {
        ContaBancariaResponseDTO dto = new ContaBancariaResponseDTO();
        dto.setId(conta.getId());
        dto.setNumero(conta.getNumero());
        dto.setSaldo(conta.getSaldo());
        dto.setAtiva(conta.getAtiva());
        dto.setDataCriacao(conta.getDataCriacao());
        
        // Converter cliente para ClienteResponseDTO
        if (conta.getCliente() != null) {
            ClienteResponseDTO clienteDto = new ClienteResponseDTO();
            clienteDto.setId(conta.getCliente().getId());
            clienteDto.setNome(conta.getCliente().getNome());
            clienteDto.setCpf(conta.getCliente().getCpf());
            clienteDto.setDataCriacao(conta.getCliente().getDataCriacao());
            dto.setCliente(clienteDto);
        }
        
        return dto;
    }
    
    private TransacaoResponseDTO convertToTransacaoResponseDTO(Transacao transacao) {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setTipo(transacao.getTipo());
        dto.setValor(transacao.getValor());
        dto.setDescricao(transacao.getDescricao());
        dto.setDataTransacao(transacao.getDataTransacao());
        
        if (transacao.getContaOrigem() != null) {
            dto.setContaOrigemId(transacao.getContaOrigem().getId());
            dto.setNumeroContaOrigem(transacao.getContaOrigem().getNumero());
        }
        
        if (transacao.getContaDestino() != null) {
            dto.setContaDestinoId(transacao.getContaDestino().getId());
            dto.setNumeroContaDestino(transacao.getContaDestino().getNumero());
        }
        
        return dto;
    }
}
