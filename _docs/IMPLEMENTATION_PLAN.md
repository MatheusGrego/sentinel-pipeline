# Plano de Implementação: Sentinel Pipeline

**Versão:** 1.0
**Data:** 24/06/2025

## 1. Introdução

Este documento detalha o plano de implementação passo a passo para o projeto Sentinel Pipeline. A estratégia é dividir o desenvolvimento em fases incrementais, onde cada fase resulta em uma entrega de valor funcional e testável. Este será o meu guia principal durante a codificação, garantindo que eu siga a arquitetura e os requisitos definidos anteriormente.

## 2. Fase 0: Configuração do Ambiente e Fundações

O objetivo desta fase é preparar todo o terreno para que o desenvolvimento possa começar sem impedimentos.

- [ ] Criar o diretório principal do projeto e inicializar um repositório Git (`git init`).
- [ ] Criar a estrutura inicial de arquivos, incluindo os documentos de planejamento (`PROJECT_VISION.md`, `REQUIREMENTS.md`, etc.).
- [ ] Desenvolver a primeira versão do `docker-compose.yml` contendo apenas o serviço do **LocalStack** (com `S3` e `SQS` habilitados).
- [ ] Executar `docker-compose up` para confirmar que o LocalStack está funcionando corretamente.
- [ ] Usar o **AWS CLI** para criar o bucket e a fila que serão utilizados, garantindo que o ambiente está pronto para a aplicação.
  - Comando do Bucket: `aws --endpoint-url=http://localhost:4566 s3 mb s3://satellite-images`
  - Comando da Fila: `aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name image-analysis-queue`
- [ ] Fazer o primeiro commit no Git com toda a estrutura inicial do projeto.

## 3. Fase 1: Construção do `ingestion-service`

Nesta fase, o foco é construir o primeiro microsserviço, responsável por receber os dados.

- [ ] Dentro do projeto, criar um subdiretório `ingestion-service`.
- [ ] Usar o Spring Initializr para gerar um novo projeto Spring Boot com as dependências necessárias: `Spring Web`, `Lombok`, `Spring Cloud AWS S3` e `Spring Cloud AWS SQS`.
- [ ] Configurar o `application.properties` para apontar para os serviços do LocalStack.
- [ ] Implementar o Controller com o endpoint `POST /v1/images` que recebe um `MultipartFile`.
- [ ] Implementar a lógica no Service para:
    - [ ] Fazer o upload do arquivo recebido para o bucket no S3.
    - [ ] Enviar uma mensagem com o ID da imagem para a fila no SQS.
- [ ] Escrever um teste unitário básico para a camada de serviço, "mockando" as chamadas para os clientes AWS para validar a lógica de negócio.
- [ ] **Critério de Aceite:** Testar o endpoint manualmente (via Postman/Insomnia). Ao submeter uma imagem, o arquivo deve aparecer no bucket S3 e uma mensagem correspondente deve surgir na fila SQS.

## 4. Fase 2: Construção do `analysis-service`

Com a ingestão funcionando, o próximo passo é construir o serviço que fará o processamento.

- [ ] Criar um subdiretório `analysis-service` e gerar um novo projeto Spring Boot com as dependências: `Spring Web`, `Lombok`, `Spring Data JPA`, `PostgreSQL Driver`, e os clientes AWS.
- [ ] Adicionar o serviço do **PostgreSQL** ao `docker-compose.yml`.
- [ ] Configurar o `application.properties` deste novo serviço para conectar-se ao banco de dados e ao LocalStack.
- [ ] Criar a entidade JPA `AnalysisResult` e seu respectivo `Repository`.
- [ ] Implementar um componente `@Service` que conterá um método anotado com `@SqsListener` para "ouvir" a fila `image-analysis-queue`.
- [ ] Dentro do método listener, implementar a lógica principal:
    - [ ] Baixar a imagem do S3 usando o ID recebido na mensagem.
    - [ ] Executar a lógica de análise simulada.
    - [ ] Salvar o `AnalysisResult` no banco de dados PostgreSQL.
- [ ] Implementar o endpoint `GET /v1/results/{imageId}` para consultar os resultados.
- [ ] **Critério de Aceite:** Ao executar o fluxo da Fase 1, o `analysis-service` deve consumir a mensagem da fila automaticamente, um novo registro deve aparecer no banco de dados e este deve ser consultável via API REST.

## 5. Fase 3: Unificação e Finalização

A última fase integra tudo em uma arquitetura coesa e profissional.

- [ ] Criar os projetos Spring Boot para o `api-gateway` e o `eureka-server`.
- [ ] Adicionar os dois novos serviços ao `docker-compose.yml`.
- [ ] Configurar as rotas no `api-gateway` para direcionar o tráfego para os serviços `ingestion-service` e `analysis-service`.
- [ ] Configurar todos os serviços (gateway, ingestion, analysis) como clientes do Eureka para habilitar a descoberta de serviço.
- [ ] Criar um `Dockerfile` para cada um dos 4 serviços Java (`eureka`, `gateway`, `ingestion`, `analysis`).
- [ ] Atualizar o `docker-compose.yml` final para orquestrar toda a stack, usando os Dockerfiles para construir as imagens dos serviços.
- [ ] Realizar um teste de ponta a ponta, enviando uma imagem através do API Gateway e consultando seu resultado também através do Gateway.
- [ ] Escrever o `README.md` final na raiz do projeto, explicando a arquitetura e como executar todo o ambiente com um único comando `docker-compose up --build`.
- [ ] **Critério de Aceite:** Todo o ecossistema sobe com um único comando. O fluxo completo funciona passando pelo API Gateway. O repositório está limpo e bem documentado.
