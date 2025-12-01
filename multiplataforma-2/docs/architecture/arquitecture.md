# Arquitetura do Sistema

O projeto segue uma arquitetura **Full Stack Multiplataforma** baseada em microsserviços monolíticos (Monorepo), onde uma única API serve múltiplos clientes (Web e Mobile).

## 1. Diagrama de Alto Nível
[ Banco de Dados (MongoDB Atlas) ] ⬆ ⬇ (Mongoose) [ Backend API (Node.js + Express) ] ⬆ ⬆ (JSON/HTTP) (JSON/HTTP via Retrofit) ⬇ ⬇ [ Frontend Web ] [ Frontend Mobile ] (Next.js/React) (Android Nativo/Kotlin)

## 2. Tecnologias Utilizadas

### Backend (`/backend`)
- **Runtime:** Node.js (v18+)
- **Framework:** Express.js
- **Banco de Dados:** MongoDB (via Mongoose ODM)
- **Segurança/Config:** `dotenv` para variáveis de ambiente, `cors` para permissão de acesso cruzado.

### Frontend Web (`/frontend/web`)
- **Framework:** Next.js 14 (App Router)
- **Biblioteca UI:** React.js
- **Estilização:** CSS Modules e Global CSS (Variáveis CSS).
- **Consumo de API:** `fetch` nativo do JavaScript (Async/Await).

### Frontend Mobile (`/frontend/mobile`)
- **Linguagem:** Kotlin
- **Plataforma:** Android Nativo
- **Rede:** Retrofit 2 + Gson Converter
- **UI:** XML Layouts + ViewBinding
- **Concorrência:** Kotlin Coroutines.

## 3. Estrutura de Pastas (Monorepo)

- **`/backend`**: Contém a lógica do servidor, modelos do banco de dados e conexão.
- **`/frontend/web`**: Contém o site Next.js, páginas de login e dashboards.
- **`/frontend/mobile`**: Contém o projeto Android Studio.
- **`/database`**: Scripts de automação (Seed) para popular o banco inicialmente.

## 4. Fluxo de Dados
1. **Entrada:** O usuário preenche o formulário no Web ou Mobile.
2. **Envio:** O cliente envia uma requisição `POST` com um JSON para `http://localhost:5000/api/animais`.
3. **Processamento:** O Backend recebe, valida e usa o Mongoose para salvar no MongoDB Atlas.
4. **Leitura:** O cliente Web faz um `GET`, recebe o JSON e renderiza os "Cards" de animais na tela.