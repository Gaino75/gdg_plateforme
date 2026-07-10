import { useState } from 'react';
import { Link } from 'react-router-dom';
import { KeyRound, ArrowLeft, Fuel } from 'lucide-react';
import axiosInstance from '../../../services/axiosInstance';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await axiosInstance.post('/auth/forgot-password', { email });
      setSent(true);
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
          <p className="text-xs tracking-widest text-slate-400 mb-2">MOT DE PASSE OUBLIÉ</p>
          <h1 className="text-3xl font-bold leading-tight mb-8">
            Récupérez l'accès à votre compte en toute sécurité.
          </h1>
          <p className="text-4xl font-bold">256-bit</p>
          <p className="text-slate-400 text-sm">chiffrement bcrypt</p>
        </div>
        <p className="text-slate-500 text-xs">Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>

      <div className="flex-1 flex items-center justify-center bg-slate-50 p-8">
        <div className="w-full max-w-sm">
          <Link to="/connexion" className="text-sm text-slate-500 flex items-center gap-1 mb-6 hover:text-slate-800">
            <ArrowLeft size={15} /> Retour à la connexion
          </Link>

          {sent ? (
            <div className="bg-teal-50 rounded-xl p-6">
              <KeyRound className="text-teal-700 mb-3" size={24} />
              <h2 className="text-lg font-bold text-slate-900 mb-1">Email envoyé</h2>
              <p className="text-sm text-slate-600">
                Si un compte existe avec cet email, un lien de réinitialisation vient d'être envoyé à <b>{email}</b>.
              </p>
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              <KeyRound className="text-slate-400 mb-3" size={22} />
              <h2 className="text-2xl font-bold text-slate-900">Mot de passe oublié</h2>
              <p className="text-slate-500 text-sm mb-6">Indiquez votre email, nous vous enverrons un lien de réinitialisation.</p>

              {error && <div className="bg-red-50 text-red-700 text-sm rounded-lg px-4 py-2 mb-4">{error}</div>}

              <label className="text-sm font-medium text-slate-700">Adresse email</label>
              <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)}
                placeholder="vous@exemple.com"
                className="w-full mt-1 mb-5 px-3 py-2.5 rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-teal-700" />

              <button type="submit" disabled={loading}
                className="w-full bg-teal-800 hover:bg-teal-900 text-white font-medium py-2.5 rounded-lg disabled:opacity-60">
                {loading ? 'Envoi...' : 'Envoyer le lien →'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}