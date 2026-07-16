// Badge statut (Disponible/Limite/RUpture)

import React from 'react';

const Badge = ({ status, children, className = '' }) => {
  const statusStyles = {
    available: 'bg-[#2ECC71] text-white',
    limited: 'bg-[#F39C12] text-white',
    out: 'bg-[#E74C3C] text-white',
    pending: 'bg-[#FF6B35] text-white',
    confirmed: 'bg-[#2ECC71] text-white',
    cancelled: 'bg-gray-400 text-white',
    expired: 'bg-gray-400 text-white',
    active: 'bg-[#2ECC71] text-white',
    inactive: 'bg-gray-400 text-white',
    suspended: 'bg-[#E74C3C] text-white',
    en_attente: 'bg-[#FF6B35] text-white',
  };

  const labels = {
    available: 'Disponible',
    limited: 'Stock limité',
    out: 'Rupture',
    pending: 'En attente',
    confirmed: 'Confirmée',
    cancelled: 'Annulée',
    expired: 'Expirée',
    active: 'Actif',
    inactive: 'Inactif',
    suspended: 'Suspendu',
    en_attente: 'En attente',
  };

  const style = statusStyles[status] || 'bg-gray-400 text-white';
  const label = children || labels[status] || status;

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${style} ${className}`}>
      {label}
    </span>
  );
};

export default Badge;