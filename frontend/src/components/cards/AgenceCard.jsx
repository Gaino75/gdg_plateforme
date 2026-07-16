// Carte Station (liste agences)

import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Star } from 'lucide-react';
import Card from '../ui/Card';
import Badge from '../ui/Badge';

const AgenceCard = ({ agence, showDetail = true }) => {
  const isAvailable = agence.statut === 'ACTIF';

  return (
    <Card hover>
      <div className="flex flex-col">
        <div className="flex items-start justify-between">
          <h3 className="font-semibold text-gray-900">{agence.nom}</h3>
          <Badge status={isAvailable ? 'available' : 'inactive'}>
            {isAvailable ? 'Disponible' : 'Fermé'}
          </Badge>
        </div>
        <div className="flex items-center gap-1 text-sm text-gray-500 mt-1">
          <MapPin size={14} />
          <span>{agence.adresse}, {agence.ville?.nom}</span>
        </div>
        <div className="flex items-center gap-3 mt-2 text-sm">
          <span className="flex items-center gap-1">
            <Star size={14} className="text-yellow-400 fill-yellow-400" />
            <span>4.8</span>
          </span>
          <span className="text-gray-400">·</span>
          <span className="text-gray-500">{agence.telephone}</span>
        </div>
        {showDetail && (
          <Link
            to={`/agence/${agence.id}`}
            className="mt-3 text-sm text-[#FF6B35] hover:underline text-center"
          >
            Voir le stock →
          </Link>
        )}
      </div>
    </Card>
  );
};

export default AgenceCard;