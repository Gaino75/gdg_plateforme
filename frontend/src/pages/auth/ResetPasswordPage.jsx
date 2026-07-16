// Reinitialisation mot de passe

import React, { useState } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { Lock, ArrowLeft, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const [motDePasse, setMotDePasse] = useState('');
  const [confirmation, setConfirmation] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const resetToken = searchParams.get('token');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (motDePasse !== confirmation) {
      setError('Les mots de passe ne correspondent pas');
      return;
    }

    if (motDePasse.length < 8) {
      setError('Le mot de passe doit contenir au moins 8 caractères');
      return;
    }

    if (!resetToken) {
      setError('Token de réinitialisation invalide');
      return;
    }

    setLoading(true);
    try {
      await axiosInstance.post(API.AUTH.RESET_PASSWORD, {
        resetToken,
        nouveauMotDePasse: motDePasse,
      });
      setSuccess(true);
      setTimeout(() => navigate('/connexion'), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Une erreur est survenue');
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
          <p className="text-xs tracking-widest text-gray-400 mb-2">RÉINITIALISATION</p>
          <h1 className="text-3xl font-bold leading-tight mb-8">
            Créez un nouveau mot de passe sécurisé.
          </h1>
          <p className="text-4xl font-bold">256-bit</p>
          <p className="text-gray-400 text-sm">chiffrement bcrypt</p>
        </div>
        <p className="text-gray-500 text-xs">Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</p>
      </div>

      <div className="flex-1 flex items-center justify-center p-8">
        <div className="w-full max-w-sm">
          <Link to="/connexion" className="text-sm text-gray-500 flex items-center gap-1 mb-6 hover:text-gray-800">
            <ArrowLeft size={15} /> Retour à la connexion
          </Link>

          {success ? (
            <div className="bg-green-50 rounded-xl p-6">
              <CheckCircle className="text-green-600 mb-3" size={24} />
              <h2 className="text-lg font-bold text-gray-900 mb-1">Mot de passe réinitialisé</h2>
              <p className="text-sm text-gray-600">
                Votre mot de passe a été modifié avec succès. Vous allez être redirigé vers la connexion.
              </p>
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              <Lock className="text-gray-400 mb-3" size={22} />
              <h2 className="text-2xl font-bold text-gray-900">Nouveau mot de passe</h2>
              <p className="text-gray-500 text-sm mb-6">
                Choisissez un mot de passe sécurisé pour votre compte.
              </p>

              {error && (
                <div className="bg-red-50 text-red-700 text-sm rounded-lg px-4 py-2 mb-4">{error}</div>
              )}

              <label className="text-sm font-medium text-gray-700">Nouveau mot de passe</label>
              <input
                type="password"
                required
                minLength={8}
                value={motDePasse}
                onChange={(e) => setMotDePasse(e.target.value)}
                placeholder="8 caractères minimum"
                className="w-full mt-1 mb-4 px-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]"
              />

              <label className="text-sm font-medium text-gray-700">Confirmer le mot de passe</label>
              <input
                type="password"
                required
                value={confirmation}
                onChange={(e) => setConfirmation(e.target.value)}
                placeholder="Confirmez votre mot de passe"
                className="w-full mt-1 mb-6 px-3 py-2.5 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#1E3A5F]"
              />

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-[#1E3A5F] hover:bg-[#18304F] text-white font-medium py-2.5 rounded-lg disabled:opacity-60 transition-colors"
              >
                {loading ? 'Réinitialisation...' : 'Réinitialiser le mot de passe →'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}