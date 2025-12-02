const mongoose = require('mongoose');
const dotenv = require('dotenv');
const path = require('path');

// Ajusta o caminho para encontrar o arquivo .env na raiz do backend
dotenv.config({ path: path.join(__dirname, '../.env') });

// Importa o modelo Animal
const Animal = require('./models/Animal');

console.log('⏳ Tentando conectar ao MongoDB...');

mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log('✅ MongoDB Conectado! Iniciando Seed...'))
  .catch((err) => {
    console.error('❌ Erro na conexão:', err.message);
    process.exit(1);
  });

const importData = async () => {
  try {
    // Limpa dados antigos
    await Animal.deleteMany();
    console.log('🧹 Dados antigos removidos...');

    // Cria os novos animais
    await Animal.create([
      {
        nome: 'Rex Seed',
        raca: 'Vira-lata',
        genero: 'Macho',
        castrado: true,
        obs: 'Criado via script de Seed (Backend)'
      },
      {
        nome: 'Luna Seed',
        raca: 'Gato Siamês',
        genero: 'Fêmea',
        castrado: false,
        obs: 'Criada via script de Seed (Backend)'
      }
    ]);

    console.log('🏁 Sucesso! Animais criados.');
    process.exit();
  } catch (error) {
    console.error('❌ Erro ao salvar:', error);
    process.exit(1);
  }
};

// Pequeno delay para garantir que a conexão abriu antes de tentar escrever
mongoose.connection.once('open', () => {
    importData();
});