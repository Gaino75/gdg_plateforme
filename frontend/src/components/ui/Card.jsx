// Carte generique

import React from 'react';

const Card = ({ children, title, subtitle, className = '', hover = false, padding = true }) => {
  return (
    <div className={`
      bg-white rounded-xl border border-gray-200
      ${padding ? 'p-6' : ''}
      ${hover ? 'card-hover' : ''}
      ${className}
    `}>
      {(title || subtitle) && (
        <div className="mb-4">
          {title && <h3 className="text-lg font-semibold text-gray-900">{title}</h3>}
          {subtitle && <p className="text-sm text-gray-500">{subtitle}</p>}
        </div>
      )}
      {children}
    </div>
  );
};

export default Card;