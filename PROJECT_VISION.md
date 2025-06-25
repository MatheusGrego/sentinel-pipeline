# Visão e Escopo do Projeto: Analisador de Imagens de Satélite (Simulado)

**Versão:** 1.0
**Data:** 24/06/2025

## 1. Visão Geral

### 1.1. O quê? (O Produto)

Este projeto consiste na construção de uma plataforma de backend para o processamento assíncrono de imagens de satélite. A plataforma será desenvolvida como uma arquitetura de microsserviços, utilizando Java com o ecossistema Spring (Boot, Cloud), e será totalmente containerizada com Docker. A interação com serviços de nuvem (armazenamento de objetos e mensageria) será simulada localmente através do LocalStack para emular o ambiente da AWS (S3 e SQS).

### 1.2. Por quê? (A Motivação)

O objetivo principal é criar uma peça de portfólio robusta e profissional que demonstre competências técnicas avançadas em desenvolvimento de software. O projeto visa validar habilidades em:
* Design de sistemas distribuídos (microsserviços).
* Implementação de padrões de arquitetura de software (Clean Architecture).
* Comunicação assíncrona entre serviços.
* Uso de tecnologias de nuvem padrão de mercado (AWS).
* Práticas de DevOps (containerização com Docker e orquestração com Docker Compose).


### 1.3. Para quem? (Os Atores)

O principal ator do sistema é um **"Usuário"** (ou **"Cliente de API"**), que interage com a plataforma através de seus endpoints REST para submeter imagens e consultar os resultados de suas análises.

## 2. Escopo

### 2.1. Dentro do Escopo (Funcionalidades da Versão 1.0)

As seguintes funcionalidades serão implementadas:

* ✅ **Ingestão de Imagem:** Permitir que um usuário faça o upload de uma imagem (`.jpg` ou `.png`) através de um endpoint REST.
* ✅ **Armazenamento de Objeto:** Armazenar a imagem recebida em um bucket no serviço S3 (simulado pelo LocalStack).
* ✅ **Enfileiramento de Tarefa:** Após o armazenamento, publicar uma mensagem em uma fila SQS (simulada pelo LocalStack) notificando que uma nova imagem está pronta para análise.
* ✅ **Processamento Assíncrono:** Consumir a mensagem da fila de forma assíncrona por um serviço de análise dedicado.
* ✅ **Análise Simulada:** O serviço de análise irá baixar a imagem do S3 e executar uma lógica de análise simulada (sem o uso de IA/ML real), gerando um resultado em formato JSON.
* ✅ **Persistência do Resultado:** Salvar o resultado da análise em um banco de dados relacional (PostgreSQL).
* ✅ **Consulta de Resultado:** Fornecer um endpoint REST para que o usuário possa consultar o resultado da análise de uma imagem específica.

### 2.2. Fora do Escopo (Funcionalidades Futuras)

As seguintes funcionalidades são intencionalmente deixadas de fora da versão 1.0 para manter o foco:

* ❌ **Interface Gráfica (Frontend):** Toda a interação será via API, utilizando ferramentas como Postman, Insomnia ou cURL.
* ❌ **Análise de Imagem Real:** A lógica de análise será um simulacro para focar na arquitetura do sistema.
* ❌ **Autenticação e Autorização:** A primeira versão não terá um sistema de login ou perfis de usuário. Os endpoints serão públicos dentro do ambiente de execução.
