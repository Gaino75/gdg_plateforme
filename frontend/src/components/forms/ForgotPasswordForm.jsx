// Mot de passe oublie
import React, { useState } from 'react';
import { Mail, ArrowRight } from 'lucide-react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Alert from '../ui/Alert';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

const ForgotPasswordForm = ({ onSuccess }) => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [sent, setSent] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await axiosInstance.post(API.AUTH.FORGOT_PASSWORD, { email });
      setSent(true);
      if (onSuccess) onSuccess();
    } catch (err) {
      setError(err.response?.data?.message || 'Une erreur est survenue');
    } finally {
      setLoading(false);
    }
  };

  if (sent) {
    return (
      <div className="bg-green-50 p-6 rounded-xl text-center">
        <Mail className="text-green-600 mx-auto mb-3" size={48} />
        <h3 className="text-lg font-bold text-gray-900">Email envoyé</h3>
        <p className="text-sm text-gray-600 mt-2">
          Si un compte existe avec cet email, un lien de réinitialisation vous a été envoyé.
        </p>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <p className="text-sm text-gray-500">
        Indiquez votre email, nous vous enverrons un lien de réinitialisation.
      </p>

      <Input
        label="Adresse email"
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="vous@exemple.com"
        icon={Mail}
        required
      />

      <Button type="submit" variant="primary" fullWidth loading={loading} className="gap-2">
        Envoyer le lien <ArrowRight size={18} />
      </Button>
    </form>
  );
};

export default ForgotPasswordForm;