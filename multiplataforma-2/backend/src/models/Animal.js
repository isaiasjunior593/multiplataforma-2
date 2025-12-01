const mongoose = require('mongoose');

const AnimalSchema = new mongoose.Schema({
  nome: { type: String, required: true },
  raca: { type: String, required: true },
  genero: { type: String, enum: ['Macho', 'Fêmea'], default: 'Macho' },
  castrado: { type: Boolean, default: false },
  obs: { type: String },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Animal', AnimalSchema);