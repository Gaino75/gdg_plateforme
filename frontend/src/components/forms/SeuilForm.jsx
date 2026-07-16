// Definir seuil critique

import React, { useState } from 'react';
import { AlertTriangle, Save } from 'lucide-react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Alert from '../ui/Alert';

const SeuilForm = ({ initialValue, onSubmit, loading = false }) => {
  const [seuil, setSeuil] = useState(initialValue || 5);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);

    if (seuil < 0) {
      setError('Le seuil doit être positif');
      return;
    }

    onSubmit(seuil, () => {
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}
      {success && <Alert type="success" message="Seuil mis à jour avec succès !" />}

      <div className="flex items-center gap-3">
        <AlertTriangle size={20} className="text-orange-500" />
        <p className="text-sm text-gray-600">
          Définissez la quantité minimale de stock à partir de laquelle une alerte sera déclenchée.
        </p>
      </div>

      <Input
        label="Seuil critique"
        type="number"
        min={0}
        value={seuil}
        onChange={(e) => setSeuil(parseInt(e.target.value) || 0)}
        required
      />

      <Button type="submit" variant="primary" loading={loading} className="gap-2">
        <Save size={18} /> Enregistrer le seuil
      </Button>
    </form>
  );
};

export default SeuilForm;