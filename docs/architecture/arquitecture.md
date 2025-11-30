Visão Geral

O Abrigo Solidário é um sistema web desenvolvido para auxiliar o gerenciamento de abrigos de animais, permitindo o cadastro, listagem e acompanhamento de animais disponíveis para adoção.

/
├── front/
├── backend/
├── database/
├── docs/
├── validation/
└── README.md

Fluxo / Arquitetura Lógica

Com base na estrutura atual, o fluxo de dados / funcionamento do sistema pode ser descrito como:

flowchart LR
    subgraph UI [Frontend (front/)]
        A1[Pages HTML/CSS/JS]
    end

    subgraph Server [Backend (backend/)] 
        B1[API / Lógica de Negócio]
    end

    subgraph DB [Database (database/)]
        C1[(Dados + armazenamento)] 
    end

    A1 -->|Requests / formulário / JS fetch / AJAX| B1
    B1 -->|Consulta / grava dados| C1
    B1 --> A1

