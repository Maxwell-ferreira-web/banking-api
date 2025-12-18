package com.bankingapi.controller;

import com.bankingapi.dto.ContaBancariaRequestDTO;
import com.bankingapi.dto.ContaBancariaResponseDTO;
import com.bankingapi.dto.TransacaoRequestDTO;
import com.bankingapi.dto.TransferenciaRequestDTO;
import com.bankingapi.dto.TransferenciaResponseDTO;
import com.bankingapi.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
@Tag(name = "Contas Bancárias", description = "Operações relacionadas ao gerenciamento de contas bancárias")
@CrossOrigin(origins = "*")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Operation(
        summary = "Listar todas as contas",
        description = "Retorna uma lista de todas as contas bancárias cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<ContaBancariaResponseDTO>> listarContas() {
        List<ContaBancariaResponseDTO> contas = contaService.listarContas();
        return ResponseEntity.ok(contas);
    }

    @Operation(
        summary = "Buscar conta por ID",
        description = "Retorna os detalhes de uma conta específica pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conta encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancariaResponseDTO> buscarConta(
            @Parameter(description = "ID da conta bancária") @PathVariable Long id) {
        ContaBancariaResponseDTO conta = contaService.buscarConta(id);
        return ResponseEntity.ok(conta);
    }

    @Operation(
        summary = "Criar nova conta",
        description = "Cria uma nova conta bancária com os dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ContaBancariaResponseDTO> criarConta(
            @Parameter(description = "Dados para criação da conta") 
            @Valid @RequestBody ContaBancariaRequestDTO contaRequest) {
        ContaBancariaResponseDTO novaConta = contaService.criarConta(contaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @Operation(
        summary = "Realizar depósito",
        description = "Realiza um depósito na conta especificada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido para depósito"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/{id}/deposito")
    public ResponseEntity<ContaBancariaResponseDTO> depositar(
            @Parameter(description = "ID da conta para depósito") @PathVariable Long id,
            @Parameter(description = "Dados da transação de depósito") 
            @Valid @RequestBody TransacaoRequestDTO transacaoRequest) {
        ContaBancariaResponseDTO contaAtualizada = contaService.depositar(id, transacaoRequest);
        return ResponseEntity.ok(contaAtualizada);
    }

    @Operation(
        summary = "Realizar saque",
        description = "Realiza um saque na conta especificada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido para saque ou saldo insuficiente"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/{id}/saque")
    public ResponseEntity<ContaBancariaResponseDTO> sacar(
            @Parameter(description = "ID da conta para saque") @PathVariable Long id,
            @Parameter(description = "Dados da transação de saque") 
            @Valid @RequestBody TransacaoRequestDTO transacaoRequest) {
        ContaBancariaResponseDTO contaAtualizada = contaService.sacar(id, transacaoRequest);
        return ResponseEntity.ok(contaAtualizada);
    }

    @Operation(
        summary = "Realizar transferência",
        description = "Realiza uma transferência entre duas contas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo insuficiente"),
        @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/transferencia")
    public ResponseEntity<TransferenciaResponseDTO> transferir(
            @Parameter(description = "Dados da transferência") 
            @Valid @RequestBody TransferenciaRequestDTO transferenciaRequest) {
        TransferenciaResponseDTO resultado = contaService.transferir(transferenciaRequest);
        return ResponseEntity.ok(resultado);
    }

    @Operation(
        summary = "Exportar contas em CSV",
        description = "Exporta todas as contas cadastradas em formato CSV"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "CSV gerado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/exportar")
    public ResponseEntity<String> exportarCSV() {
        String csv = contaService.exportarContasCSV();
        return ResponseEntity.ok(csv);
    }

    @GetMapping("/ping")
    @Operation(summary = "Teste de conectividade", description = "Endpoint para testar se a API está funcionando")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Banking API está funcionando! 🚀");
    }
}
