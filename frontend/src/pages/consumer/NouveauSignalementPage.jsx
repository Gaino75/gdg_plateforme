// Signaler une rupture/disponibilite 

import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { AlertTriangle, ArrowLeft, CheckCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Select from '../../components/ui/Select';
import Textarea from '../../components/ui/Textarea';
import Input from '../../components/ui/Input';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';
import { ROUTES } from '../../constants/routes';

const NouveauSignalementPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [agences, setAgences] = useState([]);

  const [form, setForm] = useState({
    agenceId: location.state?.agenceId || '',
    categorieProduitId: '',
    typeSignalement: 'RUPTURE',
    commentaire: '',
  });

  useEffect(() => {
    fetchAgences();
  }, []);

  const fetchAgences = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ACTIVES);
      setAgences(response.data);
    } catch (error) {
      console.error('Erreur de chargement des agences', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await axiosInstance.post(API.NOTIFICATIONS.SIGNALEMENTS, {
        ...form,
        consommateurId: user.id,
        agenceId: parseInt(form.agenceId),
        categorieProduitId: parseInt(form.categorieProduitId),
      });
      setSuccess(true);
      setTimeout(() => {
        navigate(ROUTES.CONSUMER_SIGNALEMENTS);
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de l\'envoi du signalement');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="max-w-md mx-auto text-center py-10">
        <div className="bg-green-50 p-6 rounded-xl">
          <CheckCircle size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-gray-900">Signalement envoyé !</h2>
          <p className="text-gray-500 mt-2">Merci de contribuer à la fiabilité du réseau.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <Link to={ROUTES.CONSUMER_SIGNALEMENTS} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <h1 className="text-2xl font-bold text-gray-900">Signaler un problème</h1>
        <p className="text-gray-500">Aidez la communauté en signalant les ruptures ou disponibilités</p>

        {error && <Alert type="error" message={error} className="mt-4" />}

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Select
            label="Agence concernée"
            name="agenceId"
            value={form.agenceId}
            onChange={(e) => setForm({ ...form, agenceId: e.target.value })}
            options={agences.map(a => ({ value: a.id, label: a.nom }))}
            required
          />

          <Select
            label="Type de signalement"
            name="typeSignalement"
            value={form.typeSignalement}
            onChange={(e) => setForm({ ...form, typeSignalement: e.target.value })}
            options={[
              { value: 'RUPTURE', label: '⚠️ Rupture de stock' },
              { value: 'DISPONIBLE', label: '✅ Produit disponible' },
            ]}
            required
          />

          <Input
            label="Catégorie de produit"
            type="text"
            value={form.categorieProduitId}
            onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
            placeholder="Ex: 9 kg, 12.5 kg..."
            required
          />

          <Textarea
            label="Commentaire (optionnel)"
            value={form.commentaire}
            onChange={(e) => setForm({ ...form, commentaire: e.target.value })}
            placeholder="Précisez votre signalement..."
            rows={3}
          />

          <div className="bg-gray-50 p-4 rounded-lg text-sm text-gray-500">
            <p>Un signalement n'est publié qu'après <strong>2 confirmations concordantes en moins de 30 minutes</strong>.</p>
          </div>

          <Button type="submit" variant="orange" fullWidth loading={loading} size="lg">
            <AlertTriangle size={18} /> Envoyer le signalement
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default NouveauSignalementPage;