#!/bin/sh

curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"name": "postgres-debezium-connector","config": {"connector.class": "io.debezium.connector.postgresql.PostgresConnector","plugin.name": "pgoutput","database.hostname": "postgres","database.port": "5432","database.user": "admin","database.password": "postgresxpto","database.dbname": "doc-manager","table.include.list": "public.documento,public.doc_categoria,public.doc_tag","topic.prefix": "debezium","schema.history.internal.kafka.bootstrap.servers": "kafka:9092","schema.history.internal.kafka.topic": "schema-changes.doc-manager"}}' \
  'http://localhost:8083/connectors/'