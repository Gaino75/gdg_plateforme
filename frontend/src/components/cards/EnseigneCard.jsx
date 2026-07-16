// carte compagnie (page Accueil)

import React from 'react';
import { Link } from 'react-router-dom';
import { Fuel, MapPin } from 'lucide-react';
import Card from '../ui/Card';
import Badge from '../ui/Badge';

const EnseigneCard = ({ enseigne }) => {
  return (
    <Link to={`/enseigne/${enseigne.id}`}>
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
  );
};

export default EnseigneCard;