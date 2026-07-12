// connexion

import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Mail, Lock, Eye, EyeOff, ArrowRight, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { useAuth } from '../../context/AuthContext';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [motDePasse, setMotDePasse] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const { data } = await axiosInstance.post('/auth/login', { email, motDePasse });
      login(data.token, data);
      if (data.role === 'ADMIN') navigate('/admin');
      else if (data.role === 'DISTRIBUTEUR') navigate('/profil');
      else navigate('/profil');
    } catch (err) {
      setError(err.response?.data?.message || 'Email ou mot de passe incorrect.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex">
      {/* Panneau gauche */}
      <div className="hidden lg:flex flex-col justify-between w-2/5 bg-slate-900 text-white p-10">
        <div className="flex items-center gap-2">
          <div className="bg-teal-700 rounded-lg p-2"><Fuel size={20} /></div>
          <span className="font-bold text-lg">GPG</span>
        </div>
        <div>
          <p className="text-xs tracking-widest text-slate-400 mb-2">PLATEFORME GPG</p>
          <h1 className="text-3xl font-bold leading-tight mb-8">
            Le gaz disponible, en temps réel, partout au Cameroun.
          </h1>
          <p className="text-4xl font-bold">1 204</p>
          <p className="text-slate-400 text-sm">agences actives sur le réseau</p>
        </div>
        <p className="text-slate-500 text-xs">Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>

      {/* Formulaire */}
      <div className="flex-1 flex items-center justify-center bg-slate-50 p-8">
        <form onSubmit={handleSubmit} className="w-full max-w-sm">
          <h2 className="text-2xl font-bold text-slate-900">Connexion</h2>
          <p className="text-slate-500 text-sm mb-6">Accédez à votre espace GPG.</p>

          {error && (
            <div className="bg-red-50 text-red-700 text-sm rounded-lg px-4 py-2 mb-4">{error}</div>
          )}

          <label className="text-sm font-medium text-slate-700">Adresse email</label>
          <div className="relative mt-1 mb-4">
            <Mail size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="email" required value={email} onChange={(e) => setEmail(e.target.value)}
              placeholder="vous@exemple.com"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700"
            />
          </div>

          <label className="text-sm font-medium text-slate-700">Mot de passe</label>
          <div className="relative mt-1">
            <Lock size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type={showPassword ? 'text' : 'password'} required
              value={motDePasse} onChange={(e) => setMotDePasse(e.target.value)}
              placeholder="••••••••"
              className="w-full pl-10 pr-10 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700"
            />
            <button type="button" onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400">
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          <div className="text-right mt-2 mb-6">
            <Link to="/mot-de-passe-oublie" className="text-sm text-teal-700 hover:underline">
              Mot de passe oublié ?
            </Link>
          </div>

          <button type="submit" disabled={loading}
            className="w-full bg-teal-800 hover:bg-teal-900 text-white font-medium py-2.5 rounded-lg flex items-center justify-center gap-2 disabled:opacity-60">
            {loading ? 'Connexion...' : 'Se connecter'} <ArrowRight size={18} />
          </button>

          <p className="text-center text-sm text-slate-500 mt-4">
            Pas encore de compte ?{' '}
            <Link to="/inscription" className="text-teal-700 font-medium hover:underline">Créer un compte</Link>
          </p>
        </form>
      </div>
    </div>
  );
}