# 🏦 Banking API

Sistema bancário completo desenvolvido em **Java** com **Spring Boot**, oferecendo funcionalidades essenciais para gestão de contas bancárias e transações financeiras.

## ✨ Funcionalidades

- ✅ **Criação de contas bancárias**
- ✅ **Consulta de saldo e dados da conta**  
- ✅ **Operações de depósito e saque**
- ✅ **Transferências entre contas**
- ✅ **Histórico completo de transações**
- ✅ **Validações de negócio e segurança**
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

## 📋 Endpoints da API

### **Contas Bancárias**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/contas` | Criar nova conta |
| `GET` | `/api/contas/{numero}` | Consultar conta |
| `GET` | `/api/contas` | Listar todas as contas |

### **Operações Financeiras**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/contas/{numero}/deposito` | Realizar depósito |
| `POST` | `/api/contas/{numero}/saque` | Realizar saque |
| `POST` | `/api/contas/transferencia` | Transferir entre contas |

### **Histórico**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/contas/{numero}/transacoes` | Histórico de transações |

## 🧪 Testando a API

### **Usando Postman:**
1. Importe a collection: `Banking-API.postman_collection.json`
2. Execute os requests na seguinte ordem:
   - Criar contas
   - Realizar operações
   - Consultar histórico

### **Exemplo de requisição:**
```json
POST /api/contas
{
  "titular": "Maxwell Ferreira",
  "cpf": "12345678901",
  "saldoInicial": 1000.00
}

## 🏗️ Estrutura do Projeto

src/
├── main/java/com/bankingapi/
│   ├── controller/     # Controladores REST
│   ├── service/        # Lógica de negócio
│   ├── entity/         # Entidades JPA
│   ├── repository/     # Repositórios
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Tratamento de exceções
│   └── config/         # Configurações
└── resources/
    └── application.properties

## 💡 Validações Implementadas

- ✅ **Saldo suficiente** para saques e transferências
- ✅ **Valores positivos** nas operações
- ✅ **Conta de destino existente** nas transferências
- ✅ **CPF único** por conta
- ✅ **Dados obrigatórios** validados

## 🔒 Segurança

- Validação de entrada de dados
- Tratamento de exceções personalizado
- Logs de transações
- Controle de CORS configurado

## 📊 Banco de Dados

### **Configuração H2:**
- **URL:** `jdbc:h2:mem:bankingdb`
- **Usuario:** `sa`
- **Senha:** *(vazia)*

### **Tabelas criadas automaticamente:**
- `CLIENTE` - Dados dos clientes
- `CONTA_BANCARIA` - Informações das contas  
- `TRANSACAO` - Histórico de movimentações

## 👨‍💻 Desenvolvedor

**Maxwell Ferreira**
- GitHub: [@Maxwell-ferreira-web](https://github.com/Maxwell-ferreira-web)
- LinkedIn: [Maxwell Ferreira](https://linkedin.com/in/maxwell-ferreira)

---
