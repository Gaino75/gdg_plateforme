// Barre de niveau de stock (visuelle)
import React from 'react';

const StockBar = ({ value, max, label, className = '' }) => {
  const percentage = max > 0 ? Math.min((value / max) * 100, 100) : 0;
  const isOut = value === 0;
  const isCritical = value > 0 && value <= max * 0.2;

  const color = isOut ? 'bg-red-500' : isCritical ? 'bg-orange-500' : 'bg-green-500';

  return (
    <div className={className}>
      {label && <div className="flex justify-between text-sm mb-1">
        <span className="text-gray-600">{label}</span>
        <span className="font-medium">{value}</span>
      </div>}
      <div className="w-full bg-gray-200 rounded-full h-2.5 overflow-hidden">
        <div className={`${color} h-2.5 rounded-full transition-all duration-500`} style={{ width: `${percentage}%` }} />
      </div>
    </div>
  );
};

export default StockBar;