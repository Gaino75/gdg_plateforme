// Paiements a verifier 

import React, { useState, useEffect } from 'react';
import { CreditCard, CheckCircle, XCircle, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime, formatPrice } from '../../utils/formatters';

const PaiementsPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [paiements, setPaiements] = useState([]);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchPaiements();
  }, []);

  const fetchPaiements = async () => {
    try {
      const response = await axiosInstance.get(API.PAIEMENTS.A_VERIFIER);
      setPaiements(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des paiements', error);
    } finally {
      setLoading(false);
    }
  };

  const handleResoudre = async (id, confirmer) => {
    try {
      await axiosInstance.put(API.PAIEMENTS.RESOUDRE(id, confirmer));
      await fetchPaiements();
    } catch (error) {
      console.error('Erreur de résolution', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Paiements à vérifier</h1>
        <p className="text-gray-500">
          Statut CONFIRME_A_VERIFIER — argent reçu, réservation non confirmée automatiquement
        </p>
      </div>

      <div className="max-w-sm">
        <Input
          placeholder="Rechercher une transaction..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          icon={Search}
        />
      </div>

      <div className="space-y-3">
        {paiements.map((p) => (
          <Card key={p.id} className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="font-semibold text-gray-900">{p.reference}</span>
                <Badge status="pending">À vérifier</Badge>
              </div>
              <p className="text-sm text-gray-600">
                {p.consommateurId} · {p.modePaiement}
              </p>
              <div className="flex items-center gap-3 text-xs text-gray-400">
                <span>{formatDateTime(p.dateInitiation)}</span>
                <span>·</span>
                <span className="font-medium text-[#1E3A5F]">{formatPrice(p.montant)}</span>
              </div>
            </div>
            <div className="flex gap-2">
              <Button variant="success" size="sm" className="gap-1" onClick={() => handleResoudre(p.id, true)}>
                <CheckCircle size={14} /> Confirmer
              </Button>
              <Button variant="danger" size="sm" className="gap-1" onClick={() => handleResoudre(p.id, false)}>
                <XCircle size={14} /> Rembourser
              </Button>
            </div>
          </Card>
        ))}
        {paiements.length === 0 && (
          <p className="text-center py-10 text-gray-500">Aucun paiement à vérifier</p>
        )}
      </div>
    </div>
  );
};

export default PaiementsPage;