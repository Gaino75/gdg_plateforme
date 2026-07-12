// inscription (choix role)

import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { User, Mail, Phone, Store, MapPin, Lock, ArrowRight, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';

export default function RegisterPage() {
  const [role, setRole] = useState('CONSOMMATEUR'); // ou 'DISTRIBUTEUR'
  const [form, setForm] = useState({
    nom: '', prenom: '', email: '', telephone: '', motDePasse: '',
    villeResidence: '', dateNaissance: '',
    nomAgence: '', villeAgence: '',
  });
  const [accepteConditions, setAccepteConditions] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

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
      if (role === 'CONSOMMATEUR') {
        await axiosInstance.post('/auth/register/consommateur', {
          nom: form.nom, prenom: form.prenom, email: form.email,
          motDePasse: form.motDePasse, telephone: form.telephone,
          villeResidence: form.villeResidence, dateNaissance: form.dateNaissance || null,
        });
      } else {
        const { data: distributeur } = await axiosInstance.post('/auth/register/distributeur', {
          nom: form.nom, prenom: form.prenom, email: form.email,
          motDePasse: form.motDePasse, telephone: form.telephone,
        });
        // Connexion immédiate pour récupérer un token, puis création de l'agence
        const { data: session } = await axiosInstance.post('/auth/login', {
          email: form.email, motDePasse: form.motDePasse,
        });
        await axiosInstance.post('/api/agences/distributeur', {
          nom: form.nomAgence, adresse: form.villeAgence, latitude: 0, longitude: 0,
          telephone: form.telephone, email: form.email,
          enseigneId: 1, // TODO: remplacer par un vrai sélecteur d'enseigne
          villeId: 1,    // TODO: remplacer par un vrai sélecteur de ville (GET /api/villes)
        }, { headers: { 'X-Distributeur-Id': session.id, Authorization: `Bearer ${session.token}` } });
      }
      navigate('/connexion');
    } catch (err) {
      setError(err.response?.data?.message || 'Une erreur est survenue.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex flex-col justify-between w-2/5 bg-slate-900 text-white p-10">
        <div className="flex items-center gap-2">
          <div className="bg-teal-700 rounded-lg p-2"><Fuel size={20} /></div>
          <span className="font-bold text-lg">GPG</span>
        </div>
        <div>
          <p className="text-xs tracking-widest text-slate-400 mb-2">NOUVEAU COMPTE</p>
          <h1 className="text-3xl font-bold leading-tight mb-8">
            Rejoignez le réseau qui numérise la distribution de gaz.
          </h1>
          <p className="text-4xl font-bold">27</p>
          <p className="text-slate-400 text-sm">villes couvertes</p>
        </div>
        <p className="text-slate-500 text-xs">Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>

      <div className="flex-1 flex items-center justify-center bg-slate-50 p-8">
        <form onSubmit={handleSubmit} className="w-full max-w-sm">
          <h2 className="text-2xl font-bold text-slate-900">Créer un compte</h2>
          <p className="text-slate-500 text-sm mb-6">Choisissez votre profil pour commencer.</p>

          <div className="flex bg-slate-100 rounded-lg p-1 mb-5">
            <button type="button" onClick={() => setRole('CONSOMMATEUR')}
              className={`flex-1 py-2 rounded-md text-sm font-medium flex items-center justify-center gap-1.5 ${role === 'CONSOMMATEUR' ? 'bg-white shadow text-slate-900' : 'text-slate-500'}`}>
              <User size={15} /> Consommateur
            </button>
            <button type="button" onClick={() => setRole('DISTRIBUTEUR')}
              className={`flex-1 py-2 rounded-md text-sm font-medium flex items-center justify-center gap-1.5 ${role === 'DISTRIBUTEUR' ? 'bg-white shadow text-slate-900' : 'text-slate-500'}`}>
              <Store size={15} /> Distributeur
            </button>
          </div>

          {error && <div className="bg-red-50 text-red-700 text-sm rounded-lg px-4 py-2 mb-4">{error}</div>}

          <div className="grid grid-cols-2 gap-3 mb-4">
            <div>
              <label className="text-sm font-medium text-slate-700">Nom</label>
              <input required value={form.nom} onChange={update('nom')} placeholder="Kamdem"
                className="w-full mt-1 px-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
            </div>
            <div>
              <label className="text-sm font-medium text-slate-700">Prénom</label>
              <input required value={form.prenom} onChange={update('prenom')} placeholder="Éric"
                className="w-full mt-1 px-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
            </div>
          </div>

          <label className="text-sm font-medium text-slate-700">Adresse email</label>
          <div className="relative mt-1 mb-4">
            <Mail size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="email" required value={form.email} onChange={update('email')} placeholder="vous@exemple.com"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
          </div>

          <label className="text-sm font-medium text-slate-700">Téléphone</label>
          <div className="relative mt-1 mb-4">
            <Phone size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input required value={form.telephone} onChange={update('telephone')} placeholder="+237 6XX XXX XXX"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
          </div>

          {role === 'CONSOMMATEUR' ? (
            <>
              <label className="text-sm font-medium text-slate-700">Ville de résidence</label>
              <div className="relative mt-1 mb-4">
                <MapPin size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input value={form.villeResidence} onChange={update('villeResidence')} placeholder="Dschang"
                  className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
            </>
          ) : (
            <>
              <label className="text-sm font-medium text-slate-700">Nom de l'agence</label>
              <div className="relative mt-1 mb-4">
                <Store size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input required value={form.nomAgence} onChange={update('nomAgence')} placeholder="Total Bafoussam Centre"
                  className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
              <label className="text-sm font-medium text-slate-700">Ville de l'agence</label>
              <div className="relative mt-1 mb-2">
                <MapPin size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input required value={form.villeAgence} onChange={update('villeAgence')} placeholder="Bafoussam"
                  className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
              </div>
              <p className="text-xs text-amber-700 bg-amber-50 rounded-lg px-3 py-2 mb-4">
                Votre agence sera visible après validation par un administrateur GPG.
              </p>
            </>
          )}

          <label className="text-sm font-medium text-slate-700">Mot de passe</label>
          <div className="relative mt-1 mb-4">
            <Lock size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="password" required minLength={8} value={form.motDePasse} onChange={update('motDePasse')}
              placeholder="8 caractères minimum"
              className="w-full pl-10 pr-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />
          </div>

          <label className="flex items-center gap-2 text-sm text-slate-600 mb-5">
            <input type="checkbox" checked={accepteConditions} onChange={(e) => setAccepteConditions(e.target.checked)} />
            J'accepte les <span className="text-teal-700 font-medium">conditions d'utilisation</span>
          </label>

          <button type="submit" disabled={loading}
            className="w-full bg-teal-800 hover:bg-teal-900 text-white font-medium py-2.5 rounded-lg flex items-center justify-center gap-2 disabled:opacity-60">
            {loading ? 'Création...' : 'Créer mon compte'} <ArrowRight size={18} />
          </button>

          <p className="text-center text-sm text-slate-500 mt-4">
            Déjà inscrit ? <Link to="/connexion" className="text-teal-700 font-medium hover:underline">Se connecter</Link>
          </p>
        </form>
      </div>
    </div>
  );
}