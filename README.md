# DocManager CDC

Este projeto implementa um sistema de Change Data Capture (CDC) com Debezium, Apache Kafka e Kafka Connect, com o fim de fornecer um ambiente prático para o estudo desta tecnologia.

O objetivo é capturar alterações em um banco de dados PostgreSQL em tempo real, processá-las e alimentar o tópico kafka 'docview.public.documento', que poderá ser usado por outros sistemas.

## Arquitetura

O fluxo de dados do sistema é o seguinte:

1. **PostgreSQL**: O banco de dados primário onde os dados dos documentos são armazenados.
2. **Debezium (Kafka Connect)**: Monitora o log de transações (WAL) do PostgreSQL. Qualquer inserção, atualização ou exclusão nas tabelas configuradas é capturada como um evento.
3. **Apache Kafka**: Os eventos de alteração de dados gerados pelo Debezium são publicados em tópicos do Kafka. Cada tabela monitorada possui seu próprio tópico.
4. **Doc Processor**: Um serviço desenvolvido em Quarkus que consome os eventos dos tópicos do Kafka. Ele processa, enriquece e transforma os dados brutos em um modelo de visualização (view model) adequado para busca.

## Tecnologias Utilizadas

- **Backend**: Java 17, Quarkus
- **Banco de Dados**: PostgreSQL
- **Mensageria**: Apache Kafka
- **CDC**: Debezium
- **Containerização**: Docker, Docker Compose

## Pré-requisitos

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java 17+ (para desenvolvimento local do `doc-processor`)
- Maven 3.8+ (para desenvolvimento local do `doc-processor`)

## Como Executar

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/gervasiodbjr/DocManagerCDC
   cd DocManagerCDC
   ```
2. **Inicie a infraestrutura com Docker Compose:**

   Navegue até o diretório `docker` e execute o comando:

   ```bash
   cd docker
   docker-compose up -d
   ```

   Este comando irá iniciar todos os serviços necessários: PostgreSQL, Zookeeper, Kafka e Kafka Connect.
3. **Configure o Conector Debezium:**

   Após os contêineres estarem em execução, execute o script para configurar o conector do Debezium para o PostgreSQL.

   ```bash
   ./configs/create-connector.sh
   ```

   Ou execute o comando `curl` presente no script manualmente. Isso instruirá o Kafka Connect a começar a monitorar a tabela `documento` (e outras, se configurado) no banco de dados `doc-manager`.
4. **Execute o Serviço `doc-processor`:**

   O serviço `doc-processor` está comentado no arquivo `docker-compose.yml` para facilitar o desenvolvimento local. Para executá-lo:

   - **Via IDE (Recomendado para desenvolvimento):** Abra o projeto `doc-processor` em sua IDE de preferência e execute a aplicação Quarkus.
   - **Via Maven:**
     ```bash
     cd ../doc-processor
     mvn quarkus:dev
     ```
   - **Via Docker (descomente no `docker-compose.yml`):** Se preferir, descomente a seção `doc-processor` no `docker-compose.yml` e reinicie o compose.

## Serviços e Portas

| Serviço                | Porta Local | Descrição                                           |
| ----------------------- | ----------- | ----------------------------------------------------- |
| **PostgreSQL**    | `5433`    | Banco de dados principal.                             |
| **Adminer**       | `8081`    | UI para gerenciar o PostgreSQL.                       |
| **Kafka UI**      | `8080`    | UI para gerenciar o Kafka (tópicos, mensagens, etc). |
| **Kafka Connect** | `8083`    | API para gerenciar conectores (Debezium, etc).        |
| **Doc Processor** | `8181`    | Serviço de processamento de documentos (local).      |

## Configuração

- **`doc-processor/src/main/resources/application.properties`**: Contém as configurações do Quarkus, incluindo os canais de entrada e saída do Kafka e a conexão com o banco de dados para o ambiente de desenvolvimento.
- **`docker/docker-compose.yml`**: Orquestra todos os serviços da infraestrutura.
- **`docker/configs/create-connector.sh`**: Script para criar o conector do Debezium. A configuração do conector está no corpo do `curl`.
- **`docker/configs/init-database.sql`**: Script SQL para inicializar o schema do banco de dados no PostgreSQL.

## Como Usar

1. Com toda a stack em execução, acesse o Adminer em `http://localhost:8081` e conecte-se ao servidor `postgres` com o usuário `admin` e senha `postgresxpto`.
2. Insira ou modifique dados nas tabelas `documento`, `doc_categoria` ou `doc_tag`.
3. Acesse a Kafka UI em `http://localhost:8080` e observe as mensagens chegando nos tópicos `debezium.public.*` e `docview.public.documento`.
4. Observe os logs do serviço `doc-processor` para ver o processamento das mensagens.

## Opcional

    Se preferir, pode ser usado o comando **mise** (https://github.com/jdx/mise) para automatizar alguns comandos e processos. Na pasta
    'docker' existe um arquivo 'mise.toml' com algumas tasks já configuradas.
