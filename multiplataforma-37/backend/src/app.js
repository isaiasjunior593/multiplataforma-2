const express = require('express');
const cors = require('cors');
const connectDB = require('./config/db');
const Animal = require('./models/Animal');

const app = express();

// Middlewares
app.use(cors()); // Permite conexões do Next.js (3000) e Mobile
app.use(express.json());

// Conexão DB
connectDB();

// Rotas
app.get('/', (req, res) => res.send('API Abrigo Rodando 🚀'));

// Rota POST: Salvar Animal (Usada pelo Web e Mobile)
app.post('/api/animais', async (req, res) => {
  try {
    const animal = await Animal.create(req.body);
    console.log("Novo animal recebido:", animal.nome);
    res.status(201).json({ success: true, data: animal });
  } catch (error) {
    res.status(400).json({ success: false, error: error.message });
  }
});

// Rota GET: Listar Animais
app.get('/api/animais', async (req, res) => {
  try {
    const animais = await Animal.find().sort({ createdAt: -1 });
    res.json({ success: true, data: animais });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Servidor rodando na porta ${PORT}`));