// Creer/modifier ville 

import React, { useState } from 'react';
import { MapPin, Globe } from 'lucide-react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Alert from '../ui/Alert';

const VilleForm = ({ initialData, onSubmit, loading = false, isEdit = false }) => {
  const [form, setForm] = useState({
    nom: initialData?.nom || '',
    region: initialData?.region || '',
    pays: initialData?.pays || 'Cameroun',
  });
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!form.nom || !form.region) {
      setError('Le nom et la région sont obligatoires');
      return;
    }
    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Input
        label="Nom de la ville *"
        value={form.nom}
        onChange={(e) => setForm({ ...form, nom: e.target.value })}
        placeholder="Bafoussam"
        required
        icon={MapPin}
      />

      <Input
        label="Région *"
        value={form.region}
        onChange={(e) => setForm({ ...form, region: e.target.value })}
        placeholder="Ouest"
        required
        icon={Globe}
      />

      <Input
        label="Pays"
        value={form.pays}
        onChange={(e) => setForm({ ...form, pays: e.target.value })}
        placeholder="Cameroun"
      />

      <Button type="submit" variant="primary" fullWidth loading={loading}>
        {isEdit ? 'Mettre à jour' : 'Créer la ville'}
      </Button>
    </form>
  );
};

export default VilleForm;