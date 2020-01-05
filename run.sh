#!/bin/bash

echo "Gerando JAR"
mvn clean install -DskipTests -U

echo "Criando image....."
sudo docker image  build -t ponto .

echo "Inicializando container......"
sudo docker container run --rm --name pontoeletronico  --network=host  -p 8080:8080 ponto