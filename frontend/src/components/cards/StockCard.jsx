// carte stock par categorie

import React from 'react';
import { Fuel } from 'lucide-react';
import Card from '../ui/Card';
import Button from '../ui/Button';
import Badge from '../ui/Badge';
import { formatPrice } from '../../utils/formatters';

const StockCard = ({ stock, onReserve }) => {
  const isOut = stock.quantiteDisponible === 0;
  const isLow = stock.quantiteDisponible > 0 && stock.quantiteDisponible <= 3;

  return (
    <Card>
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-[#1E3A5F]/10 p-2 rounded-lg">
            <Fuel size={20} className="text-[#1E3A5F]" />
          </div>
          <div>
            <p className="font-semibold text-gray-900">{stock.libelle}</p>
            <p className="text-sm text-gray-500">{formatPrice(stock.prixUnitaire)}</p>
          </div>
        </div>
        <Badge status={isOut ? 'out' : isLow ? 'limited' : 'available'}>
          {isOut ? 'Rupture' : isLow ? 'Stock faible' : 'Disponible'}
        </Badge>
      </div>
      <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100">
        <span className="text-sm text-gray-500">
          {stock.quantiteDisponible} unités disponibles
        </span>
        <Button
          variant={isOut ? 'outline' : 'orange'}
          size="sm"
          onClick={onReserve}
          disabled={isOut}
        >
          {isOut ? 'Indisponible' : 'Réserver'}
        </Button>
      </div>
    </Card>
  );
};

export default StockCard;