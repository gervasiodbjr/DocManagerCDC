#!/bin/sh
set -e

COMPOSE_FILE="docker/docker-compose.yml"

echo "Subindo containers..."
docker compose -f "$COMPOSE_FILE" up -d

echo
echo "Aguardando todos os containers ficarem ativos..."

check_containers() {
  docker compose -f "$COMPOSE_FILE" ps --status running --quiet | wc -l
}

TOTAL_CONTAINERS=$(docker compose -f "$COMPOSE_FILE" config --services | wc -l)

while true; do
  RUNNING=$(check_containers)
  if [ "$RUNNING" -eq "$TOTAL_CONTAINERS" ]; then
    echo "Todos os $TOTAL_CONTAINERS containers estão rodando."
    break
  else
    echo "$RUNNING de $TOTAL_CONTAINERS containers prontos. Aguardando..."
    sleep 2
  fi
done

echo
echo "Aguardando Kafka Connect responder..."
until curl -s http://localhost:8083/ | grep -q "version"; do
  sleep 2
done

echo
echo "Criando o conector <postgres-debezium-connector> no kafka-connect"
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"name": "postgres-debezium-connector","config": {"connector.class": "io.debezium.connector.postgresql.PostgresConnector","plugin.name": "pgoutput","database.hostname": "postgres","database.port": "5432","database.user": "admin","database.password": "postgresxpto","database.dbname": "doc-manager","table.include.list": "public.documento,public.doc_categoria,public.doc_tag","topic.prefix": "debezium","schema.history.internal.kafka.bootstrap.servers": "kafka:9092","schema.history.internal.kafka.topic": "schema-changes.doc-manager"}}' \
  http://localhost:8083/connectors/

echo
echo "Projeto iniciado com sucesso!"
echo "Acesse o gerenciador do banco Postgres pela url 'http://localhost:8081' [Sistema: PostgreSQL / Usuario: admin / Senha: postgresxpto/ Banco: doc-manager]"
echo "Acesse o kafka-ui pela url 'http://localhost:8080'"
echo