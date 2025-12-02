![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

# Sistema Helpdesk - JG Mix

**Status do Projeto:** 🚧 Em Desenvolvimento

Sistema Full Stack para gestão de chamados de suporte técnico (Helpdesk). O projeto tem como objetivo gerenciar o fluxo de trabalho entre técnicos e clientes, permitindo a abertura, acompanhamento e encerramento de ordens de serviço.

---

## 📋 Funcionalidades Implementadas

Até o momento, o sistema conta com os seguintes módulos funcionais:

- **Autenticação e Segurança:**
  - Login com JWT (JSON Web Token).
  - Proteção de rotas (Guards) no Frontend.
  - Controle de sessão e Logout.

- **Gestão de Técnicos:**
  - Cadastro completo (Create).
  - Listagem com paginação e filtros (Read).
  - Edição de dados e perfis de acesso (Update).
  - Exclusão com verificação de integridade (Delete).

- **Gestão de Clientes:**
  - CRUD completo de clientes.
  - Validação de CPF e E-mail únicos no banco de dados.

- **Gestão de Chamados (Ordens de Serviço):**
  - Criação de tickets vinculando Cliente e Técnico.
  - Definição de **Prioridade** (Baixa, Média, Alta).
  - Definição de **Status** (Aberto, Em Andamento, Encerrado).
  - Interface com indicadores visuais de status.

---

## 🛠️ Tecnologias Utilizadas

Este projeto utiliza uma arquitetura **Monorepo**, contendo Back-end e Front-end no mesmo repositório.

### Back-End (API REST)
- **Linguagem:** Java 11+
- **Framework:** Spring Boot 2.x
- **Banco de Dados:** H2 (Dev) / MySQL (Prod)
- **Segurança:** Spring Security
- **Persistência:** Spring Data JPA / Hibernate
- **Build:** Maven

### Front-End (SPA)
- **Framework:** Angular 12+
- **Design System:** Angular Material
- **Bibliotecas Auxiliares:**
  - `ngx-toastr` (Feedback visual)
  - `ngx-mask` (Máscaras de input)
  - `rxjs` (Programação reativa)

---

## 📂 Estrutura do Projeto

O projeto segue a arquitetura **Monorepo**, organizado em duas grandes estruturas:

```bash
HelpdeskJGM/
├── ☕ Back-End/               # API RESTful com Spring Boot
│   ├── src/main/java/com/turmaa/helpdeskturmaa/
│   │   ├── ⚙️ config/        # Configurações de Perfil (Test, Dev) e Beans
│   │   ├── 🎮 controllers/   # Camada de Controle (Endpoints REST)
│   │   ├── 📦 domain/        # Entidades JPA e DTOs (Modelagem de Dados)
│   │   │   ├── dtos/         # Objetos de Transferência de Dados
│   │   │   └── enums/        # Prioridade, Status, Perfil
│   │   ├── 💾 repositories/  # Camada de Acesso a Dados (Spring Data JPA)
│   │   ├── 🔐 security/      # Filtros JWT e Configurações de Segurança
│   │   └── 🧠 service/       # Regras de Negócio e Validações
│   └── pom.xml               # Gerenciamento de dependências Maven
│
└── 🅰️ Front-End/              # Aplicação Single Page (SPA) com Angular
    ├── src/app/
    │   ├── 🧩 components/    # Componentes Visuais (CRUDs)
    │   │   ├── chamado/      # Gestão de Tickets
    │   │   ├── cliente/      # Gestão de Clientes
    │   │   ├── tecnico/      # Gestão de Técnicos
    │   │   └── home/         # Dashboard e KPIs
    │   ├── ⚙️ config/        # Constantes globais (API URLs)
    │   ├── 🛡️ core/          # Serviços Essenciais (Singleton)
    │   │   ├── guards/       # Proteção de Rotas (AuthGuard)
    │   │   ├── interceptors/ # Interceptador de Token JWT
    │   │   └── services/     # Comunicação HTTP com o Backend
    │   ├── 📦 models/        # Interfaces TypeScript (Tipagem)
    │   └── 🔄 shared/        # Recursos Compartilhados
    │       ├── pipes/        # Formatadores (Status, Prioridade)
    │       └── shared.module # Centralizador de Módulos (Material)
    └── package.json          # Gerenciamento de dependências NPM

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
Antes de começar, você precisará ter instalado em sua máquina:
- **Java JDK 11** ou superior.
- **Maven** (Opcional, caso não use o wrapper `mvnw`).
- **Node.js** e **NPM**.
- **Angular CLI** (Instale globalmente com: `npm install -g @angular/cli`).

### 1️⃣ Rodando o Back-End (API)
1. Abra o terminal na pasta `Back-End`.
2. Instale as dependências e execute o projeto:
```bash
mvn spring-boot:run
```

(Ou utilize sua IDE de preferência como IntelliJ/Eclipse para rodar a classe HelpdeskturmaaApplication.java) 3. O servidor iniciará na porta 8080.

### 2️⃣ Rodando o Front-End (Interface)
1. Abra um novo terminal na pasta `Front-End`.
2. Instale as dependências do projeto (apenas na primeira vez):
   ```bash
   npm install
Execute o servidor de desenvolvimento:

```bash
ng serve
```
A aplicação estará disponível no navegador em: http://localhost:4200

## 🔐 Credenciais de Acesso (Ambiente de Teste)

O sistema possui um serviço de **DB Seeding** que popula o banco de dados automaticamente com usuários fictícios para facilitar a avaliação e os testes.

> **Nota:** Estas credenciais são recriadas sempre que o perfil `test` é ativado.

| Perfil (Role) | E-mail (Login) | Senha | Permissões |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `bill@mail.com` | `123` | Acesso total (Gerencia Técnicos, Clientes e Chamados). |
| **TÉCNICO** | `stallman@mail.com` | `456` | Visualiza e atende os chamados. |
| **CLIENTE** | `linus@mail.com` | `123` | Abre novos chamados e visualiza seus próprios tickets. |

> **Nota:** Ainda não há diferenciação no nível de acesso para cada tipo de usuário.

## 🦸 Autor

Desenvolvido por **dherii**. 

Entre em contato! 👋

* **LinkedIn:** Dherick de Sousa Bomfim(www.linkedin.com/in/dherick-sousa-180104121205dm)
* **Email:** [dhericksousab@gmail.com](mailto:dhericksousab@gmail.com)
* **GitHub:** [@dherii](https://github.com/dherii)
