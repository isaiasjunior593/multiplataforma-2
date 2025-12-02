const mongoose = require('mongoose');
const dotenv = require('dotenv');

// 1. AQUI MUDOU: Apontamos para o Animal.js em vez de User
const Animal = require('../../backend/src/models/Animal'); 

// Carrega as variáveis de ambiente do backend
dotenv.config({ path: '../../backend/.env' }); 

// Conecta ao Banco
mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log('Mongo Conectado para Seed...'))
  .catch((err) => {
    console.error(err);
    process.exit(1);
  });

const importData = async () => {
  try {
    // 2. Limpa os animais antigos
    await Animal.deleteMany(); 
    console.log('Dados antigos removidos...');

    // 3. Cria animais de teste
    await Animal.create([
      {
        nome: 'Rex Seed',
        raca: 'Vira-lata',
        genero: 'Macho',
        castrado: true,
        obs: 'Criado via script de Seed'
      },
      {
        nome: 'Luna Seed',
        raca: 'Gato Siamês',
        genero: 'Fêmea',
        castrado: false,
        obs: 'Criada via script de Seed'
      }
    ]);

    console.log('✅ Dados de Animais importados com sucesso!');
    process.exit();
  } catch (error) {
    console.error('❌ Erro com os dados:', error);
    process.exit(1);
  }
};

importData();