// Graphique evolution stock

import React from 'react';

const StockChart = ({ data, title }) => {
  const maxValue = Math.max(...data.map(d => d.value), 1);

  return (
    <div>
      {title && <h3 className="text-sm font-medium text-gray-700 mb-3">{title}</h3>}
      <div className="space-y-2">
        {data.map((item, index) => {
          const percentage = (item.value / maxValue) * 100;
          const color = item.value === 0 ? '#E74C3C' : 
                       item.value <= 5 ? '#F39C12' : '#2ECC71';
          return (
            <div key={index} className="space-y-1">
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">{item.label}</span>
                <span className="font-medium">{item.value}</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="h-2 rounded-full transition-all duration-500"
                  style={{
                    width: `${Math.min(percentage, 100)}%`,
                    backgroundColor: color,
                  }}
                />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default StockChart;