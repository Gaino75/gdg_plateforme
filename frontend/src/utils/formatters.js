// formatagee dates, prix, nombres

/**
 * Formate un prix en FCFA
 */
export const formatPrice = (amount) => {
  if (!amount && amount !== 0) return '0 FCFA';
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'XAF',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount);
};

/**
 * Formate une date
 */
export const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

/**
 * Formate une date avec l'heure
 */
export const formatDateTime = (date) => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

/**
 * Formate une heure (HH:MM)
 */
export const formatTime = (date) => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleTimeString('fr-FR', {
    hour: '2-digit',
    minute: '2-digit',
  });
};

/**
 * Formate un nombre avec séparateur de milliers
 */
export const formatNumber = (number) => {
  if (!number && number !== 0) return '0';
  return new Intl.NumberFormat('fr-FR').format(number);
};

/**
 * Retourne un texte pour le statut d'une réservation
 */
export const getReservationStatusLabel = (status) => {
  const labels = {
    'EN_ATTENTE': 'En attente',
    'PAYEE': 'Payée',
    'CONFIRMEE': 'Confirmée',
    'ANNULEE': 'Annulée',
    'EXPIREE': 'Expirée',
    'RECUPEREE': 'Récupérée',
  };
  return labels[status] || status;
};

/**
 * Retourne la couleur d'un statut de réservation
 */
export const getReservationStatusColor = (status) => {
  const colors = {
    'EN_ATTENTE': 'text-orange-500 bg-orange-50',
    'PAYEE': 'text-blue-500 bg-blue-50',
    'CONFIRMEE': 'text-green-500 bg-green-50',
    'ANNULEE': 'text-gray-500 bg-gray-50',
    'EXPIREE': 'text-red-500 bg-red-50',
    'RECUPEREE': 'text-green-500 bg-green-50',
  };
  return colors[status] || 'text-gray-500 bg-gray-50';
};