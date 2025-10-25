# Life-OS

![Status](https://img.shields.io/badge/status-em_desenvolvimento-orange.svg)

Um sistema operativo pessoal, baseado em terminal, para gestão de vida.

## O Conceito

O Life-OS é uma exploração pessoal na sistematização da vida diária. O conceito central é aplicar os princípios de engenharia de software — como modularidade, gestão de estado e interfaces claras — a tarefas quotidianas.

Este projeto não tem como objetivo clonar ferramentas de produtividade existentes. É um sistema de opinião (*opinionated*), construído de raiz, com um foco total nos seguintes princípios:

* **Controlo Local:** Todos os dados residem num único ficheiro de base de dados local (`.db`). Sem *cloud*, sem subscrições, sem dependência de rede.
* **Interface TUI (Text User Interface):** Toda a interação é feita através do terminal. Isto garante performance máxima, ausência de distrações e total acessibilidade via teclado.
* **Simplicidade:** O formato de dados (SQLite) e a lógica da aplicação (Java) são deliberadamente simples, permitindo fácil manutenção e extensibilidade.

## A Arquitetura

A aplicação está a ser construída com uma stack de tecnologia robusta e focada no ambiente de terminal:

* **Linguagem:** [Java 17+](https://www.oracle.com/java/)
* **Interface TUI:** [Lanterna 3](https://github.com/mabe02/lanterna) (Uma biblioteca pura Java para criar interfaces gráficas no terminal)
* **Base de Dados:** [SQLite](https://www.sqlite.org/index.html) (Para armazenamento local num único ficheiro)
* **Build/Dependências:** [Apache Maven](https://maven.apache.org/)

---

## Como Executar (Ambiente Linux)

Como o projeto ainda está em desenvolvimento ativo, a única forma de o executar é compilando a partir do código-fonte.

### 1. Pré-requisitos

Certifique-se que tem o **JDK 17+** (Java) e o **Maven** instalados no seu sistema.

```bash
# Exemplo para Debian/Ubuntu
sudo apt update
sudo apt install openjdk-17-jdk maven