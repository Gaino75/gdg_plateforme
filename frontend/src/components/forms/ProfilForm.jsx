// modifier profil 

import React, { useState } from 'react';
import { User, Phone, Mail, Lock } from 'lucide-react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Alert from '../ui/Alert';

const ProfilForm = ({ user, onSubmit, loading = false }) => {
  const [form, setForm] = useState({
    nom: user?.nom || '',
    prenom: user?.prenom || '',
    telephone: user?.telephone || '',
    email: user?.email || '',
  });
  const [passwordForm, setPasswordForm] = useState({ ancien: '', nouveau: '' });
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleProfileSubmit = (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    onSubmit(form);
  };

  const handlePasswordSubmit = (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    if (passwordForm.nouveau.length < 8) {
      setError('Le mot de passe doit contenir au moins 8 caractères');
      return;
    }
    // TODO: Appel API changement mot de passe
    setMessage('Mot de passe mis à jour.');
    setPasswordForm({ ancien: '', nouveau: '' });
  };

  return (
    <div className="space-y-6">
      {message && <Alert type="success" message={message} />}
      {error && <Alert type="error" message={error} />}

      {/* Profil */}
      <form onSubmit={handleProfileSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <Input
            label="Nom"
            value={form.nom}
            onChange={(e) => setForm({ ...form, nom: e.target.value })}
            icon={User}
          />
          <Input
            label="Prénom"
            value={form.prenom}
            onChange={(e) => setForm({ ...form, prenom: e.target.value })}
            icon={User}
          />
        </div>

        <Input
          label="Téléphone"
          value={form.telephone}
          onChange={(e) => setForm({ ...form, telephone: e.target.value })}
          icon={Phone}
        />

        <Input
          label="Email"
          type="email"
          value={form.email}
          disabled
          icon={Mail}
        />

        <Button type="submit" variant="primary" loading={loading}>
          Enregistrer les modifications
        </Button>
      </form>

      {/* Sécurité */}
      <form onSubmit={handlePasswordSubmit} className="space-y-4 border-t pt-6">
        <h3 className="font-semibold text-gray-900">Sécurité</h3>

        <Input
          label="Mot de passe actuel"
          type="password"
          value={passwordForm.ancien}
          onChange={(e) => setPasswordForm({ ...passwordForm, ancien: e.target.value })}
          placeholder="••••••••"
          icon={Lock}
        />

        <Input
          label="Nouveau mot de passe"
          type="password"
          value={passwordForm.nouveau}
          onChange={(e) => setPasswordForm({ ...passwordForm, nouveau: e.target.value })}
          placeholder="8 caractères minimum"
          icon={Lock}
        />

        <Button type="submit" variant="primary">
          Mettre à jour le mot de passe
        </Button>
      </form>
    </div>
  );
};

export default ProfilForm;