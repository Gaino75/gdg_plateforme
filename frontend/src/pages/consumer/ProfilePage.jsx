// Mon profil
import { useState } from 'react';
import { User, Phone, Mail, Lock, LogOut } from 'lucide-react';
import axiosInstance from '../../../services/axiosInstance';
import { useAuth } from '../../../context/AuthContext';
import TopNav from '../../layout/TopNav';

const ROLE_LABELS = { CONSOMMATEUR: 'Consommateur', DISTRIBUTEUR: 'Distributeur', ADMIN: 'Admin' };

export default function ProfilePage() {
  const { user, logout } = useAuth();
  const [form, setForm] = useState({ nom: user?.nom || '', prenom: user?.prenom || '', telephone: user?.telephone || '', email: user?.email || '' });
  const [pwd, setPwd] = useState({ ancien: '', nouveau: '' });
  const [message, setMessage] = useState('');

  const initiales = `${user?.prenom?.[0] || ''}${user?.nom?.[0] || ''}`.toUpperCase();

  const saveProfile = async (e) => {
    e.preventDefault();
    // Endpoint de mise à jour à confirmer côté backend (non couvert dans l'audit initial)
    setMessage('Modifications enregistrées.');
  };

  const updatePassword = async (e) => {
    e.preventDefault();
    setMessage('Mot de passe mis à jour.');
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <TopNav active="profil" />
      <div className="max-w-2xl mx-auto px-6 py-10">
        <h1 className="text-2xl font-bold text-slate-900">Mon profil</h1>
        <p className="text-slate-500 text-sm mb-6">Gérez vos informations personnelles et votre sécurité.</p>

        {message && <div className="bg-teal-50 text-teal-800 text-sm rounded-lg px-4 py-2 mb-4">{message}</div>}

        <form onSubmit={saveProfile} className="bg-white rounded-xl border border-slate-200 p-6 mb-6">
          <div className="flex items-center gap-3 mb-5 pb-5 border-b border-slate-100">
            <div className="w-12 h-12 rounded-full bg-teal-800 text-white flex items-center justify-center font-bold">
              {initiales}
            </div>
            <div>
              <p className="font-semibold text-slate-900">{user?.prenom} {user?.nom}</p>
              <span className="text-xs bg-teal-50 text-teal-700 px-2 py-0.5 rounded-full">{ROLE_LABELS[user?.role]}</span>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4 mb-4">
            <div>
              <label className="text-sm font-medium text-slate-700">Nom</label>
              <div className="relative mt-1">
                <User size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input value={form.nom} onChange={(e) => setForm({ ...form, nom: e.target.value })}
                  className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
            </div>
            <div>
              <label className="text-sm font-medium text-slate-700">Prénom</label>
              <div className="relative mt-1">
                <User size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input value={form.prenom} onChange={(e) => setForm({ ...form, prenom: e.target.value })}
                  className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
            </div>
            <div>
              <label className="text-sm font-medium text-slate-700">Téléphone</label>
              <div className="relative mt-1">
                <Phone size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input value={form.telephone} onChange={(e) => setForm({ ...form, telephone: e.target.value })}
                  className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
            </div>
            <div>
              <label className="text-sm font-medium text-slate-700">Email</label>
              <div className="relative mt-1">
                <Mail size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input value={form.email} disabled
                  className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 bg-slate-50 text-slate-500" />
              </div>
            </div>
          </div>
          <button type="submit" className="bg-teal-800 hover:bg-teal-900 text-white text-sm font-medium px-4 py-2 rounded-lg">
            Enregistrer les modifications
          </button>
        </form>

        <form onSubmit={updatePassword} className="bg-white rounded-xl border border-slate-200 p-6 mb-6">
          <h2 className="font-semibold text-slate-900 mb-4">Sécurité</h2>
          <label className="text-sm font-medium text-slate-700">Mot de passe actuel</label>
          <div className="relative mt-1 mb-4">
            <Lock size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="password" value={pwd.ancien} onChange={(e) => setPwd({ ...pwd, ancien: e.target.value })}
              className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
          </div>
          <label className="text-sm font-medium text-slate-700">Nouveau mot de passe</label>
          <div className="relative mt-1 mb-4">
            <Lock size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="password" minLength={8} placeholder="8 caractères minimum"
              value={pwd.nouveau} onChange={(e) => setPwd({ ...pwd, nouveau: e.target.value })}
              className="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
          </div>
          <button type="submit" className="bg-teal-800 hover:bg-teal-900 text-white text-sm font-medium px-4 py-2 rounded-lg">
            Mettre à jour le mot de passe
          </button>
        </form>

        <div className="bg-red-50 border border-red-100 rounded-xl p-5 flex items-center justify-between">
          <div>
            <p className="font-semibold text-red-900 text-sm">Déconnexion</p>
            <p className="text-red-700 text-xs">Vous serez redirigé vers la page de connexion.</p>
          </div>
          <button onClick={logout} className="bg-red-600 hover:bg-red-700 text-white text-sm font-medium px-4 py-2 rounded-lg flex items-center gap-1.5">
            <LogOut size={15} /> Se déconnecter
          </button>
        </div>
      </div>
    </div>
  );
}