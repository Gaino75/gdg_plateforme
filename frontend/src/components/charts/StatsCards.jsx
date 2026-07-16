// cartes KPI (chiffres cles) pour le dashboard

import React from 'react';
import Card from '../ui/Card';

const StatsCards = ({ stats }) => {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {stats.map((stat, index) => (
        <Card key={index} className="text-center">
          <div className={`text-2xl font-bold ${stat.color || 'text-[#1E3A5F]'}`}>
            {stat.value}
          </div>
          <p className="text-sm text-gray-500">{stat.label}</p>
          {stat.sub && <p className="text-xs text-gray-400">{stat.sub}</p>}
        </Card>
      ))}
    </div>
  );
};

export default StatsCards;