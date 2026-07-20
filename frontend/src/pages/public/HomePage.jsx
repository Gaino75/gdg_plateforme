// page d'acceuil (liste enseignes +recherche)

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Search, MapPin, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import { formatNumber } from '../../utils/formatters';
import SearchBar from '../../components/ui/SearchBar';
import Loader from '../../components/ui/Loader';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';

const HomePage = () => {

  const [enseignes, setEnseignes] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');


  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {

      //appel des vrai endpoint pour ne pas simuler les stats
      const [enseignesRes, statsAgences, statsVilles] = await Promise.all([
        axiosInstance.get(API.AGENCES.ENSEIGNES),
        axiosInstance.get(API.AGENCES.STATISTIQUES),
        axiosInstance.get(API.AGENCES.VILLES),
      ]);

      setEnseignes(enseignesRes.data);

      // stats reel
      setStats({
        nbEnseignes: enseignesRes.data.length,
        nbAgences: statsAgences.data.totalAgences,
        nbVilles: statsVilles.data.length,

      }
      );

    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredEnseignes = enseignes.filter(e =>
    e.nom.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="container-custom py-8">
      {/* Hero Section */}
      <section className="text-center mb-12">
        <h1 className="text-4xl md:text-5xl font-bold text-[#1E3A5F] mb-4">
          Trouvez du gaz domestique près de chez vous
        </h1>
        <p className="text-gray-600 text-lg max-w-2xl mx-auto">
          Consultez la disponibilité en temps réel dans toutes les stations partenaires
        </p>
      </section>

      {/* Statistiques */}
      {stats && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          <div className="bg-white p-4 rounded-xl border border-gray-200 text-center">
            <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats.nbEnseignes)}</div>
            <div className="text-gray-500 text-sm">Compagnies</div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-gray-200 text-center">
            <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats.nbAgences)}</div>
            <div className="text-gray-500 text-sm">Stations</div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-gray-200 text-center">
            <div className="text-2xl font-bold text-[#2ECC71]">{formatNumber(stats.nbDisponibles)}</div>
            <div className="text-gray-500 text-sm">En stock</div>
          </div>
          <div className="bg-white p-4 rounded-xl border border-gray-200 text-center">
            <div className="text-2xl font-bold text-[#FF6B35]">{formatNumber(stats.nbVilles)}</div>
            <div className="text-gray-500 text-sm">Villes couvertes</div>
          </div>
        </div>
      )}

      {/* Recherche */}
      <div className="max-w-md mx-auto mb-8">
        <SearchBar
          placeholder="Rechercher une compagnie ou une ville..."
          value={searchTerm}
          onChange={setSearchTerm}
        />
      </div>

      {/* Enseignes */}
      <h2 className="text-2xl font-bold text-[#1E3A5F] mb-6">
        Nos compagnies partenaires
      </h2>

      {filteredEnseignes.length === 0 ? (
        <p className="text-gray-500 text-center py-10">Aucune compagnie trouvée</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {filteredEnseignes.map((enseigne) => (
            <Link key={enseigne.id} to={`/enseigne/${enseigne.id}`}>
              <Card hover className="text-center">
                <div className="flex flex-col items-center">
                  {enseigne.logo ? (
                    <img
                      src={enseigne.logo}
                      alt={enseigne.nom}
                      className="h-16 w-16 object-contain mb-3"
                    />
                  ) : (
                    <div className="h-16 w-16 bg-gray-100 rounded-full flex items-center justify-center mb-3">
                      <Fuel className="text-gray-400" size={24} />
                    </div>
                  )}
                  <h3 className="font-semibold text-gray-900">{enseigne.nom}</h3>
                  <p className="text-sm text-gray-500 mt-1">
                    {enseigne.nombreAgences || 0} agences
                  </p>
                  <Badge status="available" className="mt-2">Disponible</Badge>
                </div>
              </Card>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
};

export default HomePage;