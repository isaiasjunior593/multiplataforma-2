# Documentação da API

Esta API fornece endpoints para gerenciamento de animais do abrigo.

**Base URL (Local):** `http://localhost:5000`
**Formato de Dados:** JSON

---

## Endpoints

### 1. Verificar Status da API
Verifica se o servidor está online.

- **Método:** `GET`
- **Rota:** `/`
- **Resposta Sucesso (200):**
  ```json
  "API Abrigo Rodando 🚀"
  2. Listar Animais
Retorna a lista completa de animais cadastrados, ordenados pelos mais recentes.

Método: GET

Rota: /api/animais

Resposta Sucesso (200):
{
  "success": true,
  "data": [
    {
      "_id": "656...",
      "nome": "Rex",
      "raca": "Vira-lata",
      "genero": "Macho",
      "castrado": true,
      "obs": "Dócil",
      "createdAt": "2023-12-01T10:00:00.000Z"
    },
    ...
  ]
}
3. Cadastrar Animal
Registra um novo animal no banco de dados.

Método: POST

Rota: /api/animais

Headers: Content-Type: application/json

Corpo da Requisição (Body):
{
  "nome": "Luna",       // String (Obrigatório)
  "raca": "Siamês",     // String (Obrigatório)
  "genero": "Fêmea",    // String ("Macho" ou "Fêmea")
  "castrado": false,    // Boolean
  "obs": "Precisa de vacina" // String
}
Resposta Sucesso (201):
{
  "success": true,
  "data": { ...objeto criado... }
}
Resposta Erro (400):
{
  "success": false,
  "error": "Mensagem de erro de validação (ex: Nome é obrigatório)"
}