// Creer/modifier enseigne

import React, { useState } from 'react';
import { Building, Globe, Phone, Mail } from 'lucide-react';
import Input from '../ui/Input';
import Textarea from '../ui/Textarea';
import Button from '../ui/Button';
import Alert from '../ui/Alert';

const EnseigneForm = ({ initialData, onSubmit, loading = false, isEdit = false }) => {
  const [form, setForm] = useState({
    nom: initialData?.nom || '',
    logo: initialData?.logo || '',
    description: initialData?.description || '',
    siteWeb: initialData?.siteWeb || '',
    telephone: initialData?.telephone || '',
    emailContact: initialData?.emailContact || '',
  });
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!form.nom) {
      setError('Le nom de l\'enseigne est obligatoire');
      return;
    }
    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Input
        label="Nom de l'enseigne *"
        value={form.nom}
        onChange={(e) => setForm({ ...form, nom: e.target.value })}
        placeholder="TotalEnergies"
        required
        icon={Building}
      />

      <Input
        label="URL du logo"
        value={form.logo}
        onChange={(e) => setForm({ ...form, logo: e.target.value })}
        placeholder="https://example.com/logo.png"
      />

      <Textarea
        label="Description"
        value={form.description}
        onChange={(e) => setForm({ ...form, description: e.target.value })}
        placeholder="Description de l'enseigne..."
        rows={3}
      />

      <Input
        label="Site web"
        value={form.siteWeb}
        onChange={(e) => setForm({ ...form, siteWeb: e.target.value })}
        placeholder="https://total.cm"
        icon={Globe}
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Téléphone"
          value={form.telephone}
          onChange={(e) => setForm({ ...form, telephone: e.target.value })}
          placeholder="+237 690 000 000"
          icon={Phone}
        />
        <Input
          label="Email de contact"
          type="email"
          value={form.emailContact}
          onChange={(e) => setForm({ ...form, emailContact: e.target.value })}
          placeholder="contact@total.cm"
          icon={Mail}
        />
      </div>

      <Button type="submit" variant="primary" fullWidth loading={loading}>
        {isEdit ? 'Mettre à jour' : 'Créer l\'enseigne'}
      </Button>
    </form>
  );
};

export default EnseigneForm;