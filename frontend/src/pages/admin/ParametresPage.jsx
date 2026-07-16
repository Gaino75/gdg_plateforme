// parametre globaux

import React, { useState, useEffect } from 'react';
import { Settings, Save } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';

const ParametresPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [params, setParams] = useState([]);

  useEffect(() => {
    fetchParams();
  }, []);

  const fetchParams = async () => {
    try {
      const response = await axiosInstance.get(API.ADMIN.PARAMETRES);
      setParams(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des paramètres', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async (cle, valeur) => {
    setSaving(true);
    setError('');
    try {
      await axiosInstance.put(`${API.ADMIN.PARAMETRES}/${cle}`, { valeur });
      await fetchParams();
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
    <div className="max-w-3xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Paramètres système</h1>
        <p className="text-gray-500">Gérez les paramètres globaux de la plateforme</p>
      </div>

      {success && <Alert type="success" message="Paramètres enregistrés !" />}
      {error && <Alert type="error" message={error} />}

      <Card>
        <div className="space-y-6">
          {params.map((p) => (
            <div key={p.cle} className="border-b border-gray-100 pb-4 last:border-0">
              <div className="flex flex-col md:flex-row md:items-center gap-3">
                <div className="flex-1">
                  <label className="text-sm font-medium text-gray-700">{p.description || p.cle}</label>
                  <p className="text-xs text-gray-400">{p.cle}</p>
                </div>
                <div className="flex items-center gap-3">
                  <Input
                    type="text"
                    value={p.valeur}
                    onChange={(e) => {
                      const newParams = params.map(param =>
                        param.cle === p.cle ? { ...param, valeur: e.target.value } : param
                      );
                      setParams(newParams);
                    }}
                    className="w-48"
                  />
                  <Button
                    size="sm"
                    onClick={() => handleUpdate(p.cle, p.valeur)}
                    loading={saving}
                  >
                    <Save size={14} />
                  </Button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
};

export default ParametresPage;