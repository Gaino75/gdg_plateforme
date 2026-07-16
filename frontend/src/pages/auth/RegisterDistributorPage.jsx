// Inscription distributeur

import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { User, Mail, Phone, Store, MapPin, Lock, ArrowRight, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

export default function RegisterDistributorPage() {
  const [form, setForm] = useState({
    nom: '', prenom: '', email: '', telephone: '', motDePasse: '',
    nomAgence: '', villeAgence: '', enseigneId: '',
  });
  const [enseignes, setEnseignes] = useState([]);
  const [accepteConditions, setAccepteConditions] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchEnseignes();
  }, []);

  const fetchEnseignes = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ENSEIGNES);
      setEnseignes(response.data);
    } catch (err) {
      console.error('Erreur de chargement des enseignes', err);
    }
  };

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!accepteConditions) {
      setError('Merci d\'accepter les conditions d\'utilisation.');
      return;
    }
    setLoading(true);
    try {
      // 1. Créer le compte distributeur
      await axiosInstance.post(API.AUTH.REGISTER_DISTRIBUTOR, {
        nom: form.nom,
        prenom: form.prenom,
        email: form.email,
        motDePasse: form.motDePasse,
        telephone: form.telephone,
      });

      // 2. Se connecter pour obtenir un token
      const { data: session } = await axiosInstance.post(API.AUTH.LOGIN, {
        email: form.email,
        motDePasse: form.motDePasse,
      });

      // 3. Créer l'agence
      await axiosInstance.post(API.AGENCES.DISTRIBUTEUR, {
        nom: form.nomAgence,
        adresse: form.villeAgence,
        latitude: 0,
        longitude: 0,
        telephone: form.telephone,
        email: form.email,
        enseigneId: form.enseigneId,
        villeId: 1, // TODO: sélectionner la ville
      }, {
        headers: {
          'X-Distributeur-Id': session.id,
          Authorization: `Bearer ${session.token}`,
        },
      });

      navigate('/connexion');
    } catch (err) {
      setError(err.response?.data?.message || 'Une erreur est survenue.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex bg-gray-50">
      <div className="hidden lg:flex flex-col justify-between w-2/5 bg-[#1E3A5F] text-white p-10">
        <div className="flex items-center gap-2">
          <div className="bg-[#FF6B35] rounded-lg p-2"><Fuel size={20} /></div>
          <span className="font-bold text-lg">GDG</span>
        </div>
        <div>
          <p className="text-xs tracking-widest text-gray-400 mb-2">INSCRIPTION DISTRIBUTEUR</p>
          <h1 className="text-3xl font-bold leading-tight mb-8">
            Rejoignez le réseau de distribution de gaz.
          </h1>
          <p className="text-4xl font-bold">27</p>
          <p className="text-gray-400 text-sm">villes couvertes</p>
        </div>
        <p className="text-gray-500 text-xs">Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>

      <div className="flex-1 flex items-center justify-center p-8">
        <form onSubmit={handleSubmit} className="w-full max-w-sm">
          <h2 className="text-2xl font-bold text-gray-900">Créer un compte</h2>
          <p className="text-gray-500 text-sm mb-6">Inscription en tant que distributeur.</p>

          {error && <div className="bg-red-50 text-red-700 text-sm rounded-lg px-4 py-2 mb-4">{error}</div>}

          <div className="grid grid-cols-2 gap-3 mb-4">
            <div>
              <label className="text-sm font-medium text-gray-700">Nom</label>
              <input required value={form.nom} onChange={update('nom')} placeholder="Kamdem"
                className="w-full mt-1 px-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
            </div>
            <div>
              <label className="text-sm font-medium text-gray-700">Prénom</label>
              <input required value={form.prenom} onChange={update('prenom')} placeholder="Éric"
                className="w-full mt-1 px-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
            </div>
          </div>

          <label className="text-sm font-medium text-gray-700">Adresse email</label>
          <div className="relative mt-1 mb-4">
            <Mail size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input type="email" required value={form.email} onChange={update('email')} placeholder="vous@exemple.com"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
          </div>

          <label className="text-sm font-medium text-gray-700">Téléphone</label>
          <div className="relative mt-1 mb-4">
            <Phone size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input required value={form.telephone} onChange={update('telephone')} placeholder="+237 6XX XXX XXX"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
          </div>

          <label className="text-sm font-medium text-gray-700">Nom de l'agence</label>
          <div className="relative mt-1 mb-4">
            <Store size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input required value={form.nomAgence} onChange={update('nomAgence')} placeholder="Total Bafoussam Centre"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
          </div>

          <label className="text-sm font-medium text-gray-700">Ville de l'agence</label>
          <div className="relative mt-1 mb-4">
            <MapPin size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input required value={form.villeAgence} onChange={update('villeAgence')} placeholder="Bafoussam"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
          </div>

          <label className="text-sm font-medium text-gray-700">Enseigne</label>
          <select
            required
            value={form.enseigneId}
            onChange={update('enseigneId')}
            className="w-full mt-1 mb-4 px-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]"
          >
            <option value="">Sélectionner une enseigne</option>
            {enseignes.map((e) => (
              <option key={e.id} value={e.id}>{e.nom}</option>
            ))}
          </select>

          <p className="text-xs text-amber-700 bg-amber-50 rounded-lg px-3 py-2 mb-4">
            Votre agence sera visible après validation par un administrateur GDG.
          </p>

          <label className="text-sm font-medium text-gray-700">Mot de passe</label>
          <div className="relative mt-1 mb-4">
            <Lock size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input type="password" required minLength={8} value={form.motDePasse} onChange={update('motDePasse')}
              placeholder="8 caractères minimum"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]" />
          </div>

          <label className="flex items-center gap-2 text-sm text-gray-600 mb-5">
            <input type="checkbox" checked={accepteConditions} onChange={(e) => setAccepteConditions(e.target.checked)} />
            J'accepte les <span className="text-[#FF6B35] font-medium">conditions d'utilisation</span>
          </label>

          <button type="submit" disabled={loading}
            className="w-full bg-[#1E3A5F] hover:bg-[#18304F] text-white font-medium py-2.5 rounded-lg flex items-center justify-center gap-2 disabled:opacity-60 transition-colors">
            {loading ? 'Création...' : 'Créer mon compte'} <ArrowRight size={18} />
          </button>

          <p className="text-center text-sm text-gray-500 mt-4">
            Déjà inscrit ? <Link to="/connexion" className="text-[#FF6B35] font-medium hover:underline">Se connecter</Link>
          </p>
        </form>
      </div>
    </div>
  );
}