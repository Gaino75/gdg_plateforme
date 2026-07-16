// Mes factures

import React, { useState, useEffect } from 'react';
import { FileText, Download, Calendar, Fuel } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';
import { formatDateTime, formatPrice } from '../../utils/formatters';

const FacturesPage = () => {
  const { user } = useAuth();
  const [factures, setFactures] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFactures();
  }, []);

  const fetchFactures = async () => {
    try {
      // TODO: Remplacer par l'endpoint réel
      // const response = await axiosInstance.get(API.VENTES.FACTURES_CONSOMMATEUR(user.id));
      // Pour la démo
      setFactures([
        { id: 1, numero: 'F-2026-0881', montant: 7000, date: '2026-07-09T18:12:00', statut: 'payée' },
        { id: 2, numero: 'F-2026-0879', montant: 13000, date: '2026-07-09T09:03:00', statut: 'payée' },
        { id: 3, numero: 'F-2026-0865', montant: 84000, date: '2026-07-08T16:48:00', statut: 'payée' },
      ]);
    } catch (error) {
      console.error('Erreur de chargement des factures', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Mes factures</h1>
        <p className="text-gray-500">Toutes vos factures de gaz</p>
      </div>

      {factures.length === 0 ? (
        <div className="text-center py-10">
          <FileText size={48} className="mx-auto text-gray-300 mb-3" />
          <p className="text-gray-500">Aucune facture disponible</p>
        </div>
      ) : (
        <div className="space-y-3">
          {factures.map((facture) => (
            <Card key={facture.id} className="flex items-center justify-between">
              <div className="flex items-center gap-4">
                <div className="bg-[#1E3A5F]/10 p-3 rounded-lg">
                  <FileText size={20} className="text-[#1E3A5F]" />
                </div>
                <div>
                  <p className="font-semibold text-gray-900">{facture.numero}</p>
                  <div className="flex items-center gap-3 text-sm text-gray-500">
                    <span className="flex items-center gap-1">
                      <Calendar size={14} /> {formatDateTime(facture.date)}
                    </span>
                    <span className="font-medium text-[#1E3A5F]">
                      {formatPrice(facture.montant)}
                    </span>
                  </div>
                </div>
              </div>
              <Button variant="outline" size="sm" className="gap-2">
                <Download size={16} /> PDF
              </Button>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default FacturesPage;