// Paioement mobile money 

import React, { useState } from 'react';
import { CreditCard, Phone } from 'lucide-react';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Button from '../ui/Button';
import Alert from '../ui/Alert';
import { formatPrice } from '../../utils/formatters';

const PaiementForm = ({ montant, reservationId, onSubmit, loading = false }) => {
  const [form, setForm] = useState({
    modePaiement: 'ORANGE_MONEY',
    numeroTelephone: '',
  });
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!form.numeroTelephone) {
      setError('Le numéro de téléphone est obligatoire');
      return;
    }
    onSubmit({ ...form, reservationId, montant });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <div className="text-center py-4">
        <p className="text-sm text-gray-500">Montant à régler</p>
        <p className="text-3xl font-bold text-[#1E3A5F]">{formatPrice(montant)}</p>
      </div>

      <Select
        label="Mode de paiement"
        value={form.modePaiement}
        onChange={(e) => setForm({ ...form, modePaiement: e.target.value })}
        options={[
          { value: 'ORANGE_MONEY', label: 'Orange Money' },
          { value: 'MTN_MOBILE_MONEY', label: 'MTN Mobile Money' },
        ]}
        required
      />

      <Input
        label="Numéro de téléphone"
        type="tel"
        value={form.numeroTelephone}
        onChange={(e) => setForm({ ...form, numeroTelephone: e.target.value })}
        placeholder="+237 6XX XXX XXX"
        icon={Phone}
        required
      />

      <Button type="submit" variant="orange" fullWidth loading={loading} className="gap-2">
        <CreditCard size={18} /> Payer {formatPrice(montant)}
      </Button>
    </form>
  );
};

export default PaiementForm;