package com.bankingapi.utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bankingapi.entity.ContaBancaria;

@Component
public class CsvExporter {
    
    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public String exportarContas(List<ContaBancaria> contas) {
        StringBuilder csv = new StringBuilder();
        
        csv.append("ID").append(CSV_SEPARATOR)
           .append("Numero").append(CSV_SEPARATOR)
           .append("Cliente").append(CSV_SEPARATOR)
           .append("CPF").append(CSV_SEPARATOR)
           .append("Saldo").append(CSV_SEPARATOR)
           .append("Ativa").append(CSV_SEPARATOR)
           .append("Data Criacao")
           .append("\n");
        
        String dadosCsv = contas.stream()
            .map(this::contaParaCsv)
            .collect(Collectors.joining("\n"));
        
        csv.append(dadosCsv);
        
        return csv.toString();
    }
    
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
    
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
