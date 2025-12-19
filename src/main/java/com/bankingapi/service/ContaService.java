package com.bankingapi.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bankingapi.dto.ClienteResponseDTO;
import com.bankingapi.dto.ContaBancariaRequestDTO;
import com.bankingapi.dto.ContaBancariaResponseDTO;
import com.bankingapi.dto.TransacaoRequestDTO;
import com.bankingapi.dto.TransacaoResponseDTO;
import com.bankingapi.dto.TransferenciaRequestDTO;
import com.bankingapi.dto.TransferenciaResponseDTO;
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
import com.bankingapi.service.interfaces.IContaService;

@Service
@Transactional
public class ContaService implements IContaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private TransacaoRepository transacaoRepository;
    
    public ContaBancariaResponseDTO criarConta(ContaBancariaRequestDTO request) {
        Cliente cliente = clienteRepository.findByCpf(request.getCpfCliente())
                .orElse(null);
        
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setNome(request.getNomeCliente());
            cliente.setCpf(request.getCpfCliente());
            cliente.setDataCriacao(LocalDateTime.now());
            cliente = clienteRepository.save(cliente);
        }
        
        ContaBancaria conta = criarContaEspecifica(request.getTipoConta(), 
                                                 gerarNumeroConta(), 
                                                 cliente, 
                                                 request.getSaldoInicial());
        conta = contaBancariaRepository.save(conta);
        
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
    
    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarHistorico(Long contaId) {
        buscarContaPorId(contaId);
        
        List<Transacao> transacoes = transacaoRepository.findByContaIdOrderByDataTransacaoDesc(contaId);
        
        return transacoes.stream()
                .map(this::convertToTransacaoResponseDTO)
                .collect(Collectors.toList());
    }
    
    public ContaBancariaResponseDTO depositar(Long contaId, TransacaoRequestDTO request) {
        ContaBancaria conta = buscarContaPorId(contaId);
        
        conta.depositar(request.getValor());
        conta = contaBancariaRepository.save(conta);
        
        registrarTransacao(null, conta, TipoTransacao.DEPOSITO, 
            request.getValor(), request.getDescricao());
        
        return convertToResponseDTO(conta);
    }
    
    public ContaBancariaResponseDTO sacar(Long contaId, TransacaoRequestDTO request) {
        ContaBancaria conta = buscarContaPorId(contaId);
        
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
        
        contaOrigem.sacar(request.getValor());
        contaDestino.depositar(request.getValor());
        
        contaBancariaRepository.save(contaOrigem);
        contaBancariaRepository.save(contaDestino);
        
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
                throw new BusinessException("Tipo de conta inválido: " + tipoConta);
        }
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
