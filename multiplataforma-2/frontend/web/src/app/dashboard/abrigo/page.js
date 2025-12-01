'use client';
import { useState } from 'react';
import { FaPaw, FaSave } from 'react-icons/fa';

export default function AbrigoDashboard() {
  const [animalForm, setAnimalForm] = useState({
    nome: '', raca: '', genero: 'Macho', castrado: false, obs: ''
  });
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setAnimalForm(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const handleSaveAnimal = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      const res = await fetch('http://localhost:5000/api/animais', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(animalForm),
      });
      const data = await res.json();
      if (res.ok) {
        alert('Animal salvo com sucesso!');
        setAnimalForm({ nome: '', raca: '', genero: 'Macho', castrado: false, obs: '' });
      } else {
        alert('Erro: ' + data.error);
      }
    } catch (err) {
      alert('Erro de conexão com o servidor.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <h1>Painel Abrigo</h1>
      <form onSubmit={handleSaveAnimal} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input name="nome" placeholder="Nome" value={animalForm.nome} onChange={handleInputChange} required style={{ padding: '10px' }} />
        <input name="raca" placeholder="Raça" value={animalForm.raca} onChange={handleInputChange} required style={{ padding: '10px' }} />
        <select name="genero" value={animalForm.genero} onChange={handleInputChange} style={{ padding: '10px' }}>
            <option value="Macho">Macho</option>
            <option value="Fêmea">Fêmea</option>
        </select>
        <label>
            <input type="checkbox" name="castrado" checked={animalForm.castrado} onChange={handleInputChange} /> É castrado?
        </label>
        <textarea name="obs" placeholder="Observações" value={animalForm.obs} onChange={handleInputChange} style={{ padding: '10px' }} />
        
        <button type="submit" disabled={isLoading} style={{ padding: '15px', background: '#ff7f50', color: 'white', border: 'none', cursor: 'pointer' }}>
          {isLoading ? 'Salvando...' : 'Salvar Animal'} <FaSave />
        </button>
      </form>
    </div>
  );
}