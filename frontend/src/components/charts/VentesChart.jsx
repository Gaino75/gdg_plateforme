// Graphique venes par periodes

import React from 'react';

const VentesChart = ({ data, title }) => {
  const maxValue = Math.max(...data.map(d => d.value), 1);

  return (
    <div>
      {title && <h3 className="text-sm font-medium text-gray-700 mb-3">{title}</h3>}
      <div className="flex items-end h-48 gap-2">
        {data.map((item, index) => {
          const height = (item.value / maxValue) * 100;
          const color = item.value > 0 ? '#1E3A5F' : '#E2E8F0';
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

export default VentesChart;