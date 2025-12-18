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


## 🚀 Como executar o projeto

### 1. **Clone o repositório:**
```bash
git clone https://github.com/Maxwell-ferreira-web/banking-api.git
cd banking-api

