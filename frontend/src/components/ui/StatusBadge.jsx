// Badge de statut personnalise

import React from 'react';
import Badge from './Badge';

const StatusBadge = ({ status, children }) => {
  const statusMap = {
    'EN_ATTENTE': 'pending',
    'PAYEE': 'confirmed',
    'CONFIRMEE': 'confirmed',
    'ANNULEE': 'cancelled',
    'EXPIREE': 'expired',
    'RECUPEREE': 'available',
    'ACTIF': 'active',
    'INACTIF': 'inactive',
    'SUSPENDU': 'suspended',
  };

  const mappedStatus = statusMap[status] || status;
  return <Badge status={mappedStatus}>{children}</Badge>;
};

export default StatusBadge;