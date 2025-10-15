# Gerador de QR Code

Este projeto é um gerador de QR Code distribuído, composto por um frontend em Next.js e um backend em Spring Boot, ambos conteinerizados com Docker. A aplicação permite gerar QR Codes e armazená-los no AWS S3.

## Índice

  - [Visão Geral](https://www.google.com/search?q=%23vis%C3%A3o-geral)
  - [Arquitetura](https://www.google.com/search?q=%23arquitetura)
      - [Desenho de Arquitetura](https://www.google.com/search?q=%23desenho-de-arquitetura)
      - [Comunicação](https://www.google.com/search?q=%23comunica%C3%A7%C3%A3o)
  - [Tecnologias Utilizadas](https://www.google.com/search?q=%23tecnologias-utilizadas)
      - [Frontend](https://www.google.com/search?q=%23frontend)
      - [Backend](https://www.google.com/search?q=%23backend)
      - [Cloud](https://www.google.com/search?q=%23cloud)
  - [Configuração e Instalação](https://www.google.com/search?q=%23configura%C3%A7%C3%A3o-e-instala%C3%A7%C3%A3o)
      - [Pré-requisitos](https://www.google.com/search?q=%23pr%C3%A9-requisitos)
      - [Backend](https://www.google.com/search?q=%23backend-1)
          - [Configuração do AWS S3](https://www.google.com/search?q=%23configura%C3%A7%C3%A3o-do-aws-s3)
          - [Build e Execução com Docker](https://www.google.com/search?q=%23build-e-execu%C3%A7%C3%A3o-com-docker)
      - [Frontend](https://www.google.com/search?q=%23frontend-1)
          - [Instalação e Execução](https://www.google.com/search?q=%23instala%C3%A7%C3%A3o-e-execu%C3%A7%C3%A3o)
  - [Uso](https://www.google.com/search?q=%23uso)
  - [Vídeo Base](https://www.google.com/search?q=%23v%C3%ADdeo-base)
  - [Repositório GitHub](https://www.google.com/search?q=%23reposit%C3%B3rio-github)

-----

## Visão Geral

Este projeto visa demonstrar uma aplicação completa de geração de QR Codes, utilizando um stack moderno e distribuído. O frontend oferece uma interface intuitiva para o usuário inserir o texto/URL e gerar o QR Code, enquanto o backend processa a solicitação, gera a imagem do QR Code e a persiste no AWS S3.

## Arquitetura

### Desenho de Arquitetura

```mermaid
graph TD
    A[Usuário] -->|Requisição HTTP| B(Frontend - Next.js)
    B -->|Requisição HTTP (POST /qrcode)| C(Backend - Spring Boot)
    C -->|Geração de QR Code (Google ZXing)| D{Lógica de Geração}
    C -->|Upload de Imagem (AWS SDK)| E(AWS S3)
    E -->|URL Pública| C
    C -->|URL do QR Code| B
    B -->|Exibe QR Code| A
```

### Comunicação

1.  **Frontend para Backend:** O frontend (Next.js) envia uma requisição HTTP `POST` para o endpoint `/qrcode` do backend, contendo o texto ou URL a ser convertido em QR Code.
2.  **Backend e Geração de QR Code:** O backend (Spring Boot) recebe a requisição, utiliza a biblioteca Google ZXing para gerar a imagem do QR Code em formato de bytes.
3.  **Backend para AWS S3:** Após a geração, o backend utiliza o AWS SDK para realizar o upload da imagem do QR Code para um bucket S3 configurado. O nome do arquivo é gerado de forma única (UUID).
4.  **AWS S3 e URL Pública:** Uma vez que a imagem é armazenada no S3, o serviço retorna uma URL pública para acesso ao QR Code.
5.  **Backend para Frontend:** O backend retorna a URL pública do QR Code para o frontend.
6.  **Frontend e Usuário:** O frontend recebe a URL e a exibe para o usuário, permitindo o download ou visualização direta do QR Code gerado.

## Tecnologias Utilizadas

### Frontend

  - **Next.js:** Framework React para construção de aplicações web.
  - **TypeScript:** Superset de JavaScript que adiciona tipagem estática.
  - **Tailwind CSS:** Framework CSS utility-first para estilização rápida e responsiva.
  - **Docker:** Para conteinerização da aplicação frontend.

### Backend

  - **Spring Boot:** Framework Java para construção de aplicações robustas e escaláveis.
  - **Java 21:** Linguagem de programação.
  - **Google ZXing:** Biblioteca para geração de QR Codes.
  - **AWS SDK for Java:** Para integração com serviços AWS, especificamente S3.
  - **Maven:** Ferramenta de automação de build.
  - **Docker:** Para conteinerização da aplicação backend.

### Cloud

  - **AWS S3:** Serviço de armazenamento de objetos para persistência dos QR Codes gerados.

## Configuração e Instalação

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

  - Java Development Kit (JDK) 21
  - Node.js e npm (ou Yarn)
  - Docker
  - Conta AWS com permissões para criar e gerenciar buckets S3, e gerar chaves de acesso (Access Key ID e Secret Access Key).

### Backend

O backend é uma aplicação Spring Boot que gera e armazena QR Codes no AWS S3.

#### Configuração do AWS S3

1.  **Crie um Bucket S3:**

      * Acesse o console da AWS S3.
      * Crie um novo bucket. **Anote o nome do bucket e a região**, pois serão necessários para as variáveis de ambiente.
      * **Desative a opção "Bloquear todo o acesso público"** para permitir que os objetos do bucket sejam acessíveis publicamente para leitura. Você precisará confirmar que reconhece que as configurações atuais podem tornar os objetos públicos.

2.  **Configure uma Política de Bucket:**

      * No seu bucket recém-criado, vá para a aba "Permissões".
      * Clique em "Editar" na seção "Política de Bucket".
      * Adicione a seguinte política, substituindo `SEU_BUCKET_NAME` pelo nome do seu bucket:

    <!-- end list -->

    ```json
    {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Principal": "*",
                "Action": "s3:GetObject",
                "Resource": "arn:aws:s3:::SEU_BUCKET_NAME/*"
            }
        ]
    }
    ```

      * Salve as alterações. Esta política permite que qualquer pessoa leia os objetos no seu bucket.

3.  **Gere Chaves de Acesso AWS:**

      * No console da AWS, vá para "IAM" (Identity and Access Management).
      * Crie um novo usuário ou selecione um existente.
      * Vá para a aba "Credenciais de segurança" e crie um novo "Access Key".
      * **Anote o Access Key ID e o Secret Access Key**, pois serão usados como variáveis de ambiente. Certifique-se de que este usuário tenha permissões para `s3:PutObject` no seu bucket.

#### Build e Execução com Docker

1.  **Navegue até o diretório `backend` do projeto.**

2.  **Crie um arquivo `.env`** na raiz do diretório `backend` com suas credenciais AWS e o nome do bucket/região:

    ```
    AWS_ACCESS_KEY_ID=SEU_ACCESS_KEY_ID
    AWS_SECRET_ACCESS_KEY=SEU_SECRET_ACCESS_KEY
    AWS_REGION=sua-regiao-aws (ex: us-east-1)
    AWS_BUCKET_NAME=nome-do-seu-bucket
    ```

    **Importante:** Adicione `.env` ao seu `.gitignore` para não versionar suas credenciais.

3.  **Construa a imagem Docker do backend:**

    ```bash
    docker build -t qrcode-generator-backend:1.0 .
    ```

4.  **Execute o contêiner Docker do backend:**

    ```bash
    docker run -p 8080:8080 --env-file ./.env qrcode-generator-backend:1.0
    ```

    O backend estará acessível em `http://localhost:8080`.

### Frontend

O frontend é uma aplicação Next.js que consome o backend para gerar QR Codes.

#### Instalação e Execução

1.  **Navegue até o diretório `frontend` do projeto.**

2.  **Instale as dependências:**

    ```bash
    npm install
    # ou
    yarn install
    ```

3.  **Crie um arquivo `.env.local`** na raiz do diretório `frontend` para configurar a URL do backend:

    ```
    NEXT_PUBLIC_API_URL=http://localhost:8080/qrcode
    ```

4.  **Inicie o servidor de desenvolvimento:**

    ```bash
    npm run dev
    # ou
    yarn dev
    ```

    O frontend estará acessível em `http://localhost:3000`.

## Uso

1.  Certifique-se de que o backend e o frontend estão rodando.
2.  Acesse o frontend em `http://localhost:3000`.
3.  Insira o texto ou URL no campo fornecido.
4.  Clique no botão para gerar o QR Code.
5.  A imagem do QR Code será exibida, e a URL pública para o arquivo no AWS S3 será fornecida.

## Vídeo Base

Este projeto foi inspirado e teve parte do backend desenvolvida seguindo o tutorial:
[Projeto BACKEND do ZERO | Gerador de QR Code com Java, AWS S3 e Docker](http://www.youtube.com/watch?v=71WGVa79BWE) por Fernanda Kipper | Dev.
