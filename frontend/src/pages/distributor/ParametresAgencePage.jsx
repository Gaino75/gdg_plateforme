// Parametres (logo, seuil, horaires)
/*
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Textarea from '../../components/ui/Textarea';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';
import { ROUTES } from '../../constants/routes';
import { Link } from 'react-router-dom';

const ParametresAgencePage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    nom: '',
    telephone: '',
    email: '',
    adresse: '',
    latitude: '',
    longitude: '',
    enteteFacture: '',
    piedFacture: '',
    logoFacture: null,
  });

  const [horaires, setHoraires] = useState([
    { jour: 'Lundi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Mardi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Mercredi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Jeudi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Vendredi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Samedi', ouverture: '07:30', fermeture: '19:00', ferme: false },
    { jour: 'Dimanche', ouverture: '07:30', fermeture: '19:00', ferme: false },
  ]);

  useEffect(() => {
    fetchAgence();
  }, []);

  const fetchAgence = async () => {
    try {
      const response = await axiosInstance.get(`${API.AGENCES.BASE}/${user.agenceId}`);
      const data = response.data;
      setForm({
        nom: data.nom || '',
        telephone: data.telephone || '',
        email: data.email || '',
        adresse: data.adresse || '',
        latitude: data.latitude || '',
        longitude: data.longitude || '',
        enteteFacture: data.enteteFacture || '',
        piedFacture: data.piedFacture || '',
        logoFacture: data.logoFacture || null,
      });
    } catch (error) {
      console.error('Erreur de chargement de l\'agence', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await axiosInstance.put(`${API.AGENCES.BASE}/${user.agenceId}`, form);
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de mise à jour');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Gestion de mon agence</h1>
        <p className="text-gray-500">
          Ces informations apparaissent sur vos factures et votre fiche publique.
        </p>
      </div>

      <Card>
        {success && <Alert type="success" message="Modifications enregistrées !" className="mb-4" />}
        {error && <Alert type="error" message={error} className="mb-4" />}

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Logo *///}
/*
<div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
  <p className="text-sm text-gray-500 mb-2">Logo de facture</p>
  <p className="text-xs text-gray-400">PNG ou SVG, 512×512px recommandé</p>
  <Button variant="outline" size="sm" className="mt-3">
    Choisir un fichier
  </Button>
</div>

<div className="grid grid-cols-2 gap-4">
  <Input
    label="Nom de l'agence"
    value={form.nom}
    onChange={(e) => setForm({ ...form, nom: e.target.value })}
    required
  />
  <Input
    label="Téléphone"
    value={form.telephone}
    onChange={(e) => setForm({ ...form, telephone: e.target.value })}
  />
</div>

<Input
  label="Email"
  type="email"
  value={form.email}
  onChange={(e) => setForm({ ...form, email: e.target.value })}
/>

<Input
  label="Adresse"
  value={form.adresse}
  onChange={(e) => setForm({ ...form, adresse: e.target.value })}
/>

<div className="grid grid-cols-2 gap-4">
  <Input
    label="Latitude"
    value={form.latitude}
    onChange={(e) => setForm({ ...form, latitude: e.target.value })}
    placeholder="5.473°"
  />
  <Input
    label="Longitude"
    value={form.longitude}
    onChange={(e) => setForm({ ...form, longitude: e.target.value })}
    placeholder="10.417°"
  />
</div>

<Textarea
  label="En-tête personnalisé de facture"
  value={form.enteteFacture}
  onChange={(e) => setForm({ ...form, enteteFacture: e.target.value })}
  placeholder="Total Bafoussam Centre — Distribution agréée de gaz domestique"
  rows={2}
/>

<Textarea
  label="Pied de facture"
  value={form.piedFacture}
  onChange={(e) => setForm({ ...form, piedFacture: e.target.value })}
  placeholder="Merci de votre confiance"
  rows={2}
/>

{/* Horaires *///}

/*
<div>
  <h3 className="font-semibold text-gray-900 mb-3">Horaires d'ouverture</h3>
  <div className="space-y-2">
    {horaires.map((h, index) => (
      <div key={h.jour} className="flex items-center gap-3">
        <span className="w-24 text-sm font-medium text-gray-600">{h.jour}</span>
        <Input
          type="time"
          value={h.ouverture}
          onChange={(e) => {
            const newHoraires = [...horaires];
            newHoraires[index].ouverture = e.target.value;
            setHoraires(newHoraires);
          }}
          className="w-28"
        />
        <span className="text-gray-400">–</span>
        <Input
          type="time"
          value={h.fermeture}
          onChange={(e) => {
            const newHoraires = [...horaires];
            newHoraires[index].fermeture = e.target.value;
            setHoraires(newHoraires);
          }}
          className="w-28"
        />
        <label className="flex items-center gap-1 text-sm text-gray-500">
          <input
            type="checkbox"
            checked={h.ferme}
            onChange={(e) => {
              const newHoraires = [...horaires];
              newHoraires[index].ferme = e.target.checked;
              setHoraires(newHoraires);
            }}
          />
          Fermé
        </label>
      </div>
    ))}
  </div>
</div>

<Button type="submit" variant="primary" fullWidth loading={loading}>
  Enregistrer les modifications
</Button>
</form>
</Card>
</div>
);
};

export default ParametresAgencePage;
*/

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Save, Store, Phone, MapPin } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Textarea from '../../components/ui/Textarea';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';

const ParametresAgencePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    nom: '',
    telephone: '',
    email: '',
    adresse: '',
    latitude: '',
    longitude: '',
    logoFacture: '',
    enteteFacture: '',
    piedFacture: '',
  });

  useEffect(() => {
    fetchAgence();
  }, []);

  const fetchAgence = async () => {
    try {
      const response = await axiosInstance.get(`${API.AGENCES.BASE}/${user.agenceId}`);
      const data = response.data;
      setForm({
        nom: data.nom || '',
        telephone: data.telephone || '',
        email: data.email || '',
        adresse: data.adresse || '',
        latitude: data.latitude || '',
        longitude: data.longitude || '',
        logoFacture: data.logoFacture || '',
        enteteFacture: data.enteteFacture || '',
        piedFacture: data.piedFacture || '',
        // AJOUTER pour affichage info (optionnel)
        enseigneNom: data.enseigne?.nom || '',
        villeNom: data.ville?.nom || '',
        statut: data.statut || '',

      });
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    // AJOUTER validation email
    if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      setError('Veuillez entrer une adresse email valide');
      return;
    }

    setSaving(true);

    try {
      await axiosInstance.put(`${API.AGENCES.BASE}/${user.agenceId}`, form);
      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de mise à jour');
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Paramètres de l'agence</h1>
      <p className="text-gray-500">Gérez les informations de votre agence</p>

      <Card>
        {success && <Alert type="success" message="Modifications enregistrées !" className="mb-4" />}
        {error && <Alert type="error" message={error} className="mb-4" />}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input label="Nom" value={form.nom} onChange={(e) => setForm({ ...form, nom: e.target.value })} required />
            <Input label="Téléphone" value={form.telephone} onChange={(e) => setForm({ ...form, telephone: e.target.value })} />
          </div>

          <Input label="Email" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <Input label="Adresse" value={form.adresse} onChange={(e) => setForm({ ...form, adresse: e.target.value })} />

          <div className="grid grid-cols-2 gap-4">
            <Input label="Latitude" value={form.latitude} onChange={(e) => setForm({ ...form, latitude: e.target.value })} />
            <Input label="Longitude" value={form.longitude} onChange={(e) => setForm({ ...form, longitude: e.target.value })} />
          </div>

          <Textarea label="En-tête facture" value={form.enteteFacture} onChange={(e) => setForm({ ...form, enteteFacture: e.target.value })} rows={2} />
          <Textarea label="Pied facture" value={form.piedFacture} onChange={(e) => setForm({ ...form, piedFacture: e.target.value })} rows={2} />

          <Button type="submit" variant="primary" fullWidth loading={saving}>
            <Save size={18} /> Enregistrer
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default ParametresAgencePage;