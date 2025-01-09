#!/bin/bash

if [[ $1 =~ ^-?[0-9]+$ ]]; then
  # É um número inteiro válido (incluindo negativos)
  if (( $1 < 1 )); then
    ./shutdown.sh
    exit
  fi
else
  echo "Erro: O argumento não é um número válido"
  exit
fi

db_base_name="arqsoftv3usersauth"
db_base_port=5434

# Identificar o índice mais alto já em execução
latest_i=$(docker ps --filter "name=^${db_base_name}[1-9][0-9]*$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')

if (( latest_i > $1 )); then
  for ((i = $1 + 1; i <= latest_i; i++)); do
    db_name="${db_base_name}${i}"
    db_port=$(($db_base_port + i))

    docker stop ${db_name}
    docker rm ${db_name}

    echo "Parado ${db_name} na porta ${db_port}"
  done
else
  if (( latest_i < $1 )); then
    for ((i = latest_i + 1; i <= $1; i++)); do
      db_name="${db_base_name}${i}"
      db_port=$((db_base_port + i))

      docker run -d \
        --name "${db_name}" \
        --network lms-overlay-network \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -p "${db_port}:5432" \
        postgres

      echo "Iniciado ${db_name} na porta ${db_port}"
    done
  fi
fi

echo "Executando $1 instâncias de arqsoftv3usersauth, cada uma conectando a um banco de dados PostgreSQL diferente"

if docker service ls --filter "name=arqsoftv3usersauth" --format "{{.Name}}" | grep -q "^arqsoftv3usersauth$"; then
  docker service scale arqsoftv3usersauth=$1
else
  docker service create -d \
    --name arqsoftv3usersauth \
    --env SPRING_PROFILES_ACTIVE=bootstrap,sql,idGeneratorHash,facebook,recommendationY \
    --env spring.datasource.url=jdbc:postgresql://arqsoftv3usersauth{{.Task.Slot}}:5432/postgres \
    --env spring.datasource.username=postgres \
    --env spring.datasource.password=password \
    --env spring.rabbitmq.host=lms-rabbitamqp \
    --publish 8090:8080 \
    --network lms-overlay-network \
    arqsoftv3usersauth:latest



  docker service scale arqsoftv3usersauth=$1
fi
