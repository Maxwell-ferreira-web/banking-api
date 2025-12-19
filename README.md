---

# 🏦 Banking API

Sistema bancário completo desenvolvido em **Java** com **Spring Boot**, oferecendo funcionalidades essenciais para gestão de contas bancárias e transações financeiras com arquitetura robusta e bem estruturada.

## ✨ Funcionalidades

- ✅ **Criação de contas bancárias** (Corrente e Poupança)
- ✅ **Consulta de saldo e dados da conta**  
- ✅ **Operações de depósito e saque**
- ✅ **Transferências entre contas**
- ✅ **Histórico completo de transações ordenado**
- ✅ **Validações de negócio rigorosas**
- ✅ **Tratamento de exceções personalizado**
- ✅ **Documentação interativa com Swagger**
- ✅ **Banco de dados H2 em memória**

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database**
- **Swagger/OpenAPI 3**
- **Maven**
- **Postman** (para testes)

## 🚀 Como executar o projeto

### 1. **Clone o repositório:**
```bash
git clone https://github.com/Maxwell-ferreira-web/banking-api.git
cd banking-api
```

### 2. **Execute o projeto:**
```bash
./mvnw spring-boot:run
```

### 3. **Acesse as interfaces:**
- **API:** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console

## 📋 Endpoints da API

### **🏦 Contas Bancárias**
| Método | Endpoint | Descrição |                                        
    |------------|--------------|---------------|          
| `POST` | `/api/contas` | Criar nova conta |    
| `GET` | `/api/contas/{id}` | Consultar conta por ID |    
| `GET` | `/api/contas` | Listar todas as contas |

### **💰 Operações Financeiras**
| Método | Endpoint | Descrição |    
|------------|--------------|---------------|  
| `POST` | `/api/contas/{id}/deposito` | Realizar depósito |  
| `POST` | `/api/contas/{id}/saque` | Realizar saque |  
| `POST` | `/api/contas/transferencia` | Transferir entre contas |

### **📊 Histórico**

| Método | Endpoint | Descrição |  
|------------|-------------|----------------|  
| `GET` | `/api/contas/{id}/historico` | Histórico de transações |
```

## 🧪 Testando a API

### **📌 Exemplos de Requisições:**

#### **1. Criar Nova Conta:**
```http
POST /api/contas
Content-Type: application/json

{
  "nomeCliente": "Maxwell Ferreira",
  "cpfCliente": "12345678901",
  "tipoConta": "CORRENTE",
  "saldoInicial": 1000.00
}
```

#### **2. Realizar Depósito:**
```http
POST /api/contas/1/deposito
Content-Type: application/json

{
  "valor": 500.00,
  "descricao": "Depósito em conta"
}
```

#### **3. Realizar Saque:**
```http
POST /api/contas/1/saque
Content-Type: application/json

{
  "valor": 200.00,
  "descricao": "Saque para despesas"
}
```

#### **4. Transferência entre Contas:**
```http
POST /api/contas/transferencia
Content-Type: application/json

{
  "contaOrigemId": 1,
  "contaDestinoId": 2,
  "valor": 300.00,
  "descricao": "Transferência teste"
}
```

#### **5. Consultar Histórico:**
```http
GET /api/contas/1/historico
```

## 🏗️ Estrutura do Projeto

```
src/main/java/com/bankingapi/
├── 📁 config/                    # Configurações
│   ├── CorsConfig.java
│   └── SwaggerConfig.java
├── 📁 controller/                # Controladores REST
│   └── ContaController.java
├── 📁 dto/                       # Data Transfer Objects
│   ├── ClienteResponseDTO.java
│   ├── ContaBancariaRequestDTO.java
│   ├── ContaBancariaResponseDTO.java
│   ├── ContaRequestDTO.java
│   ├── ContaResponseDTO.java
│   ├── NovaContaDTO.java
│   ├── OperacaoDTO.java
│   ├── TransacaoRequestDTO.java
│   ├── TransacaoResponseDTO.java
│   ├── TransferenciaDTO.java
│   ├── TransferenciaRequestDTO.java
│   └── TransferenciaResponseDTO.java
├── 📁 entity/                    # Entidades JPA
│   ├── Cliente.java
│   ├── ContaBancaria.java
│   ├── ContaCorrente.java
│   ├── ContaPoupanca.java
│   └── Transacao.java
├── 📁 enums/                     # Enumerações
│   ├── TipoConta.java
│   └── TipoTransacao.java
├── 📁 exception/                 # Tratamento de exceções
│   ├── BusinessException.java
│   ├── ExceptionHandlerConfig.java
│   └── NotFoundException.java
├── 📁 repository/                # Repositórios JPA
│   ├── ClienteRepository.java
│   ├── ContaBancariaRepository.java
│   └── TransacaoRepository.java
├── 📁 service/                   # Lógica de negócio
│   ├── BancoService.java
│   ├── ContaService.java
│   └── interfaces/
│       ├── IBancoService.java
│       └── IContaService.java
├── 📁 utils/                     # Utilitários
│   └── CsvExporter.java
└── BankingApiApplication.java    # Classe principal
```

## 💡 Validações Implementadas

- ✅ **Saldo suficiente** para saques e transferências
- ✅ **Valores positivos** obrigatórios nas operações
- ✅ **Conta de destino existente** nas transferências
- ✅ **CPF único** por cliente
- ✅ **Dados obrigatórios** validados
- ✅ **Tipos de conta** (CORRENTE/POUPANCA)
- ✅ **Conta ativa** para operações

## 🔒 Segurança & Tratamento de Erros

- **Validação** de entrada de dados
- **Exceções personalizadas** com códigos HTTP apropriados
- **Tratamento global** de exceções
- **Logs detalhados** de transações
- **CORS configurado** para desenvolvimento

### **Códigos de Erro:**
- `400` - Dados inválidos ou saldo insuficiente
- `404` - Conta não encontrada
- `500` - Erro interno do servidor

## 📊 Banco de Dados H2

### **Configuração:**
- **URL:** `jdbc:h2:mem:bankingdb`
- **Usuario:** `sa`
- **Senha:** *(vazia)*
- **Console:** http://localhost:8080/h2-console

### **Tabelas Criadas:**
- `CLIENTES` - Dados dos clientes
- `CONTAS_BANCARIAS` - Informações das contas  
- `TRANSACOES` - Histórico de movimentações

## 📈 Funcionalidades Avançadas

- **Herança JPA** - ContaCorrente e ContaPoupanca
- **Repositórios customizados** com queries específicas
- **DTOs estruturados** para requests e responses
- **Enums** para tipos de conta e transação
- **Histórico ordenado** por data (mais recente primeiro)
- **Relacionamentos JPA** bem definidos

## 👨‍💻 Desenvolvedor

**Maxwell Ferreira**
- GitHub: [@Maxwell-ferreira-web](https://github.com/Maxwell-ferreira-web)
- LinkedIn: [Maxwell Ferreira](https://linkedin.com/in/maxwell-ferreira)

---
