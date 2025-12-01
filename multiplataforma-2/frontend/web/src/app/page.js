'use client'; // Necessário pois usamos hooks (useState, useRouter)
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import styles from './page.module.css'; // Podemos criar um CSS module ou usar inline style por enquanto

export default function Home() {
  const router = useRouter();
  
  // Estados para controlar a UI
  const [view, setView] = useState('selection'); // 'selection', 'login', 'register', 'forgot', 'admin'
  const [userType, setUserType] = useState(null); // 'adotante', 'abrigo'
  
  // Estados dos formulários
  const [formData, setFormData] = useState({ email: '', password: '', cnpj: '', adminId: '' });

  // Lógica de Navegação (Simulando seu JS original)
  const handleLogin = (e) => {
    e.preventDefault();
    if (userType === 'adotante') {
      router.push('/dashboard/adotante');
    } else {
      router.push('/dashboard/abrigo');
    }
  };

  const handleAdminLogin = (e) => {
    e.preventDefault();
    alert('Admin logado! (Simulação)');
    // router.push('/admin');
  };

  // Funções auxiliares de renderização (Componentização interna)
  const renderSelection = () => (
    <div className="login-container">
      <h1>Bem-vindo(a)</h1>
      <div className="select-buttons">
        <button 
          className="select-btn" 
          onClick={() => { setUserType('adotante'); setView('login'); }}
        >
          Adotante
        </button>
        <button 
          className="select-btn" 
          onClick={() => { setUserType('abrigo'); setView('login'); }}
        >
          Abrigo de Animais
        </button>
      </div>
      
      <div className="action-links">
        <span onClick={() => setView('forgot')} className="link">Esqueci minha senha</span> | 
        <span onClick={() => setView('register')} className="link">Cadastre-se</span> | 
        <span onClick={() => setView('admin')} className="link">Admin</span>
      </div>
    </div>
  );

  const renderLogin = () => (
    <div className="login-container">
      <h1>Acesso de {userType === 'adotante' ? 'Adotante' : 'Abrigo'}</h1>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          {userType === 'adotante' ? (
            <input type="email" placeholder="Seu Email" required className="input-field" />
          ) : (
            <input type="text" placeholder="Seu CNPJ" required className="input-field" />
          )}
        </div>
        <div className="form-group">
          <input type="password" placeholder="Senha" required className="input-field" />
        </div>
        <button type="submit" className="submit-btn">ENTRAR</button>
        <p onClick={() => { setView('selection'); setUserType(null); }} className="back-link">Voltar</p>
      </form>
    </div>
  );

  return (
    <main style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
      {/* Aqui reaproveitamos o CSS que você já fez, aplicando via style ou classes globais */}
      <style jsx>{`
        .login-container { background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); width: 100%; max-width: 380px; text-align: center; }
        .select-btn { width: 100%; padding: 15px; margin-bottom: 15px; border: 2px solid var(--cor-principal); background: white; color: var(--cor-principal); font-weight: bold; cursor: pointer; border-radius: 10px; }
        .select-btn:hover { background: var(--cor-fundo); }
        .submit-btn { width: 100%; padding: 15px; background: var(--cor-secundaria); color: white; border: none; border-radius: 8px; font-weight: bold; cursor: pointer; margin-top: 10px; }
        .input-field { width: 100%; padding: 12px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 8px; box-sizing: border-box; }
        .link { color: var(--cor-principal); cursor: pointer; font-weight: 600; margin: 0 5px; }
        .back-link { color: var(--cor-principal); cursor: pointer; margin-top: 15px; text-decoration: underline; }
      `}</style>

      {view === 'selection' && renderSelection()}
      {view === 'login' && renderLogin()}
      {/* Você pode adicionar os renders de 'register' e 'admin' seguindo a mesma lógica */}
    </main>
  );
}
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