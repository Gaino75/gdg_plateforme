//  Graphique reservations par periodes

import React from 'react';

const ReservationsChart = ({ data, title }) => {
  const maxValue = Math.max(...data.map(d => d.value), 1);

  return (
    <div>
      {title && <h3 className="text-sm font-medium text-gray-700 mb-3">{title}</h3>}
      <div className="flex items-end h-48 gap-2">
        {data.map((item, index) => {
          const height = (item.value / maxValue) * 100;
          const colors = ['#1E3A5F', '#FF6B35', '#2ECC71', '#F39C12'];
          const color = colors[index % colors.length];
          return (
            <div key={index} className="flex-1 flex flex-col items-center">
              <div
                className="w-full rounded-t transition-all duration-500"
                style={{
                  height: `${Math.max(height, 2)}%`,
                  backgroundColor: color,
                  minHeight: '4px',
                }}
              />
              <span className="text-xs text-gray-500 mt-1">{item.label}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ReservationsChart;