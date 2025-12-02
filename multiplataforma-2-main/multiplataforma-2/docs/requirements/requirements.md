# Especificação de Requisitos - Gestão de Abrigo

Este documento descreve os requisitos funcionais e não funcionais implementados no sistema multiplataforma de Gestão de Abrigo de Animais.

## 1. Visão Geral
O sistema tem como objetivo conectar abrigos de animais a potenciais adotantes. Ele permite que os abrigos cadastrem e gerenciem os animais disponíveis (via Web e Mobile) e que os adotantes visualizem essa lista em tempo real (via Web).

## 2. Requisitos Funcionais (RF)

### RF01 - Cadastro de Animais (Multiplataforma)
- **Descrição:** O sistema deve permitir o cadastro de novos animais no banco de dados.
- **Plataformas:** Web (Dashboard do Abrigo) e Mobile (Android App).
- **Dados do Animal:**
  - Nome (Obrigatório)
  - Raça (Obrigatório)
  - Gênero (Macho/Fêmea)
  - Castrado (Sim/Não)
  - Observações (Texto livre)
- **Status:** ✅ Implementado e integrado com MongoDB.

### RF02 - Listagem de Animais (Adotante)
- **Descrição:** O sistema deve exibir uma lista de todos os animais cadastrados no banco de dados.
- **Plataforma:** Web (Dashboard do Adotante).
- **Comportamento:** A lista deve ser atualizada automaticamente ao carregar a página, buscando dados da API.
- **Status:** ✅ Implementado.

### RF03 - Gestão de Perfis (Simplificada)
- **Descrição:** O sistema deve permitir a seleção de perfil de acesso na tela inicial.
- **Perfis:** - **Abrigo:** Acesso ao cadastro de animais.
  - **Adotante:** Acesso à listagem de animais para adoção.
- **Status:** ✅ Implementado (Fluxo de navegação Web).

## 3. Requisitos Não Funcionais (RNF)

- **RNF01 - Persistência na Nuvem:** Os dados devem ser armazenados no MongoDB Atlas (Cluster na Nuvem), garantindo que Web e Mobile acessem a mesma informação.
- **RNF02 - API RESTful:** A comunicação entre os frontends e o banco de dados deve ser feita via API Node.js utilizando JSON.
- **RNF03 - Disponibilidade:** O App Android deve ser capaz de conectar ao backend local (localhost) através do IP do emulador (`10.0.2.2`).
- **RNF04 - Interface Responsiva:** O Frontend Web deve ser construído em Next.js e adaptar-se a telas de desktop.