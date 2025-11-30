# multiplataforma-2
Nome do sistema: Abrigo Solidário
Descrição:
O Abrigo Solidário é um sistema web desenvolvido para facilitar o gerenciamento de abrigos de animais, permitindo o controle de cadastros, adoções, voluntários e doações de forma simples e eficiente.

Problema solucionado:
Muitos abrigos enfrentam dificuldades na organização de informações sobre animais disponíveis para adoção, controle de vacinas, e acompanhamento de voluntários. Este sistema centraliza e automatiza essas tarefas, reduzindo erros e melhorando a comunicação entre equipe e adotantes.

| Funcionalidade        | Descrição                                                                            | Status     |
| --------------------- | ------------------------------------------------------------------------------------ | ---------- |
| Cadastro de Animais   | Registro de novos animais com informações detalhadas (raça, idade, saúde, foto etc.) | ✅ Completo |
| Adoção de Animais     | Processo de adoção e registro de adotantes                                           | ✅ Completo |
| Gestão de Voluntários | Cadastro e gerenciamento de voluntários                                              | ✅ Completo |
| Login e Autenticação  | Acesso restrito a administradores e voluntários                                      | 🟡 Parcial |

Screenshots das telas principais:
<img width="1320" height="585" alt="2" src="https://github.com/user-attachments/assets/b72ccd7f-86a7-4bb3-881e-91014ff312c1" />

Tecnologias Utilizadas
Linguagens de programação -> ruby(mobile),javascript 
 Frameworks e bibliotecas -> Node.js
 Banco de dados -> Mongodb

Arquitetura do Sistema

Visão Geral:
O sistema segue uma arquitetura cliente-servidor dividida em frontend e backend, conectados via API REST.

Componentes Principais:

Frontend: Interface desenvolvida em html,css e javascript, responsável pela interação com o usuário.

Backend: API em Node.js, responsável pela lógica de negócios e comunicação com o banco de dados.

Banco de Dados: MongoDB hospedado no Atlas.

Instruções de Instalação e Execução

Pré-requisitos

Node.js (>= 18.x)
NPM ou Yarn
Conta no MongoDB Atlas (ou banco local configurado)

Instalação

Clone o repositório:

git clone https://github.com/seuusuario/abrigo-solidario.git
cd abrigo-solidario

Instale as dependências:

npm install

Configure as variáveis de ambiente:
Crie um arquivo .env na raiz com:

MONGO_URI=mongodb+srv://usuario:senha@cluster.mongodb.net/
JWT_SECRET=sua_chave_secreta
PORT=3000

Execute o servidor:

npm start

