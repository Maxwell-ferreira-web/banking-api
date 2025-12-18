package com.bankingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Aplicação principal da Banking API
 */
@SpringBootApplication
@EnableTransactionManagement
public class BankingApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BankingApiApplication.class, args);
        
        System.out.println("\n" +
            "╔══════════════════════════════════════╗\n" +
            "║         BANKING API INICIADA         ║\n" +
            "║                                      ║\n" +
            "║  🌐 API: http://localhost:8080       ║\n" +
            "║  📊 H2: http://localhost:8080/h2     ║\n" +
            "║  🧪 Test: /api/contas/ping           ║\n" +
            "╚══════════════════════════════════════╝\n"
        );
    }
}
