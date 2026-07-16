// Jauge circulaire de stock

import React from 'react';

const StockJauge = ({ label, quantity, seuil, className = '' }) => {
  const isOut = quantity === 0;
  const isCritical = quantity <= seuil && quantity > 0;
  const percentage = Math.min((quantity / (seuil * 3)) * 100, 100);
  
  const color = isOut ? 'bg-[#E74C3C]' : isCritical ? 'bg-[#F39C12]' : 'bg-[#2ECC71]';
  const statusLabel = isOut ? 'Rupture' : isCritical ? 'Stock faible' : 'Normal';

  return (
    <div className={`bg-white p-4 rounded-xl border border-gray-200 ${className}`}>
      <div className="flex justify-between items-center mb-2">
        <span className="font-medium text-gray-700">{label}</span>
        <span className={`text-sm font-semibold ${isOut ? 'text-red-500' : isCritical ? 'text-orange-500' : 'text-green-500'}`}>
          {quantity} unités
        </span>
      </div>
      <div className="w-full bg-gray-200 rounded-full h-3 overflow-hidden">
        <div
          className={`${color} h-3 rounded-full transition-all duration-500`}
          style={{ width: `${percentage}%` }}
        />
      </div>
      <div className="flex justify-between mt-1.5">
        <span className={`text-xs ${isOut ? 'text-red-500' : isCritical ? 'text-orange-500' : 'text-gray-500'}`}>
          {statusLabel}
        </span>
        <span className="text-xs text-gray-400">Seuil: {seuil}</span>
      </div>
    </div>
  );
};

export default StockJauge;