```markdown
# 📸 Projeto Álbum

API desenvolvida com **Spring Boot** para o gerenciamento de álbuns e fotografias. O projeto utiliza **MySQL** para dados relacionais e **MinIO** (S3 Compatible) para o armazenamento dos arquivos físicos de imagem.

## 🛠️ Stack Tecnológica

* **Backend:** Java 17, Spring Boot 3
* **Banco de Dados:** MySQL 8.0 (Porta 3307)
* **Migrações:** Flyway
* **Storage:** MinIO
* **Documentação:** Swagger UI / OpenAPI 3

---

## 🚀 Como Executar o Projeto

### 1. Clonar o Repositório
Abra o seu terminal e utilize um dos comandos abaixo:

**Via HTTPS:**
```bash
git clone [https://github.com/XOPAXD/album-back-end.git](https://github.com/XOPAXD/album-back-end.git)
cd album-back-end

```

**Via SSH:**

```bash
git clone git@github.com:XOPAXD/album-back-end.git
cd album-back-end

```

### 2. Inicialização Completa (Windows)

Para facilitar o setup, utilize o script de automação `init.bat` localizado na raiz do projeto. Ele configura a infraestrutura, limpa o banco e sobe a aplicação.

**Pré-requisitos:**

* **Docker Desktop** instalado e em execução.
* **Maven** configurado no PATH do sistema.

**Basta executar:**

```batch
./init.bat

```

**O que o script `init.bat` realiza:**

1. **Docker:** Sobe os containers do MySQL e MinIO via Docker Compose.
2. **Aguardar:** Realiza uma pausa de 30 segundos para garantir que o MySQL inicializou completamente o sistema de arquivos.
3. **Clean:** Reseta o banco de dados para garantir um estado limpo (`flyway:clean`).
4. **Migrate:** Aplica as migrações SQL localizadas em `src/main/resources/db/migration`.
5. **Run:** Inicia o servidor Spring Boot na porta **8081**.

---

## 📖 Documentação da API (Swagger)

A aplicação utiliza o context-path `/projeto-album`. Com o servidor rodando, acesse:

* **Swagger UI:** [http://localhost:8081/projeto-album/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8081/projeto-album/swagger-ui/index.html)
* **OpenAPI Specs:** [http://localhost:8081/projeto-album/v3/api-docs](https://www.google.com/search?q=http://localhost:8081/projeto-album/v3/api-docs)

---

## ⚙️ Configurações de Infraestrutura

### Banco de Dados (MySQL)

* **Host:** `localhost:3307`
* **Database:** `album`
* **Usuário/Senha:** `root` / `root`
* **Estratégia JPA:** `ddl-auto=none` (O controle é feito exclusivamente pelo Flyway).

### Armazenamento (MinIO)

* **Endpoint API:** `http://127.0.0.1:9000`
* **Console Web:** [http://127.0.0.1:9001](https://www.google.com/search?q=http://127.0.0.1:9001)
* **Credenciais:** `minioadmin` / `minioadmin`

---

## 🔍 Logs e Desenvolvimento

Para facilitar o debug, o projeto está configurado para exibir:

* Logs de requisições Web em nível `DEBUG`.
* Queries SQL do Hibernate formatadas no console.

```properties
logging.level.org.springframework.web=DEBUG
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```

---

## ⚠️ Troubleshooting (Resolução de Problemas)

* **Erro 'Table flyway_schema_history doesn't exist':** Este erro ocorre se a migração tentar rodar antes do container MySQL estar pronto para escrita. O script `init.bat` resolve isso com o tempo de espera de 30s.
* **Flyway Clean:** Certifique-se de que no seu `application.properties` a linha `spring.flyway.clean-disabled=false` esteja presente para permitir o reset via script.
* **Falha no Batch:** O script `init.bat` não fecha a janela automaticamente em caso de erro, permitindo que você leia a mensagem no console.

---

```

**Deseja que eu gere também um arquivo `.gitignore` otimizado para que as pastas do Maven, Docker e da sua IDE não "sujem" o seu repositório Git?**

```
