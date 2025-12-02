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

git clone https:https://github.com/isaiasjunior593/multiplataforma-2/
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

Validação

Validação com Público-Alvo Específico

⦁	Identificação específica do público-alvo
⦁	Nome da pessoa : Aparecida, funcionaria do pet shop 4 patas
⦁	Localização exata:bairro:  Sapiranga - Fortaleza - Ce
⦁	Contexto e necessidades específicas: 
O objetivo da visita foi apresentar o projeto de um aplicativo desenvolvido pela equipe, voltado ao apoio e à facilitação do processo de adoção de animais, bem como obter a validação da solução proposta por um estabelecimento que atua diretamente nessa área.
A apresentação do projeto foi realizada no pet shop 4 Patas, localizado no bairro Sapiranga em Fortaleza -CE que aceita pegar animais para adoção responsável. Devido à inviabilidade de apresentação presencial, a reunião ocorreu de forma remota, por meio de uma chamada na plataforma Google Meet. Durante a apresentação, o funcionamento do aplicativo foi demonstrado e explicado, destacando suas funcionalidades e benefícios para o processo de adoção.
O aplicativo desenvolvido foi analisado e validado pela funcionária Aparecida Firmino, que acompanhou a apresentação e considerou a proposta adequada às necessidades do pet shop e ao contexto de adoção de animais.


Comprovação Obrigatória

![WhatsApp Image 2025-11-30 at 15 37 38](https://github.com/user-attachments/assets/34739d69-e22f-4061-ac03-42f5db987b3c)

![WhatsApp Image 2025-11-30 at 15 38 04](https://github.com/user-attachments/assets/c6d2d012-fb49-45ab-99af-ed480e432b2e)

Equipe
Isaias Porto de Freitas Junior(atribuição - validação)
Mariana Ferreira dos Santos(atribuição - validação e feedback)
Raphael Rodrigues de Sousa(atribuição -front end )
Eugenio Sancho Barroso Neto(atribuição - mobile)
Francisco Ivamar Silva Leite(atrbuição - mobile )
julia santos atrbuição - front end
