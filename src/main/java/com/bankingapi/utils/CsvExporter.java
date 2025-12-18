package com.bankingapi.utils;

import com.bankingapi.entity.ContaBancaria;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

/**
 * Utilitário para exportação de dados em formato CSV
 */
@Component
public class CsvExporter {
    
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Exporta lista de contas para formato CSV
     */
    public String exportarContas(List<ContaBancaria> contas) {
        StringBuilder csv = new StringBuilder();
        
        // Cabeçalho
        csv.append("ID").append(CSV_SEPARATOR)
           .append("Numero").append(CSV_SEPARATOR)
           .append("Cliente").append(CSV_SEPARATOR)
           .append("CPF").append(CSV_SEPARATOR)
           .append("Saldo").append(CSV_SEPARATOR)
           .append("Ativa").append(CSV_SEPARATOR)
           .append("Data Criacao")
           .append("\n");
        
        // Dados
        String dadosCsv = contas.stream()
            .map(this::contaParaCsv)
            .collect(Collectors.joining("\n"));
        
        csv.append(dadosCsv);
        
        return csv.toString();
    }
    
    /**
     * Converte uma conta para linha CSV
     */
    private String contaParaCsv(ContaBancaria conta) {
        return String.format("%d%s%s%s%s%s%s%s%.2f%s%s%s%s",
                conta.getId(), CSV_SEPARATOR,
                escapeCsv(conta.getNumero()), CSV_SEPARATOR,
                escapeCsv(conta.getCliente().getNome()), CSV_SEPARATOR,
                escapeCsv(conta.getCliente().getCpf()), CSV_SEPARATOR,
                conta.getSaldo(), CSV_SEPARATOR,
                conta.getAtiva() ? "Sim" : "Não", CSV_SEPARATOR,
                conta.getDataCriacao().format(DATE_FORMATTER)
        );
    }
    
    /**
     * Escapa caracteres especiais para CSV
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // Se contém vírgula, aspas ou quebra de linha, envolver com aspas
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Duplicar aspas internas e envolver com aspas
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
