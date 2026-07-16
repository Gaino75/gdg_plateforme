// Formulaire de conexion

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, ArrowRight } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import Button from '../ui/Button';
import Input from '../ui/Input';
import Alert from '../ui/Alert';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(email, password);
      navigate('/consommateur');
    } catch (err) {
      setError(err.response?.data?.message || 'Email ou mot de passe incorrect');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Input
        label="Adresse email"
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="vous@exemple.com"
        icon={Mail}
        required
      />

      <Input
        label="Mot de passe"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="••••••••"
        icon={Lock}
        required
      />

      <div className="text-right">
        <Link to="/mot-de-passe-oublie" className="text-sm text-[#FF6B35] hover:underline">
          Mot de passe oublié ?
        </Link>
      </div>

      <Button type="submit" variant="orange" fullWidth loading={loading} size="lg" className="gap-2">
        Se connecter <ArrowRight size={18} />
      </Button>
    </form>
  );
};

export default LoginForm;