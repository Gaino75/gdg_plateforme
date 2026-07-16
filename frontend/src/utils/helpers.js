// fonctions diverses

/**
 * Retourne les initiales d'un nom complet
 */
export const getInitials = (firstName, lastName) => {
  return `${firstName?.[0] || ''}${lastName?.[0] || ''}`.toUpperCase();
};

/**
 * Tronque un texte
 */
export const truncate = (text, maxLength = 100) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

/**
 * Génère un ID unique
 */
export const generateId = () => {
  return Date.now().toString(36) + Math.random().toString(36).substring(2);
};

/**
 * Retourne le temps relatif (ex: "il y a 5 min")
 */
export const timeAgo = (date) => {
  const now = new Date();
  const diff = now - new Date(date);
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 1) return 'à l\'instant';
  if (minutes < 60) return `il y a ${minutes} min`;
  if (hours < 24) return `il y a ${hours} h`;
  if (days < 7) return `il y a ${days} j`;
  return formatDate(date);
};

/**
 * Retourne la catégorie de gaz en fonction du poids
 */
export const getCategorieLabel = (poids) => {
  const categories = {
    3: '3 kg',
    6: '6 kg',
    9: '9 kg',
    12.5: '12,5 kg',
    25: '25 kg',
    30: '30 kg',
  };
  return categories[poids] || `${poids} kg`;
};

/**
 * Calcule le montant total avec TVA
 */
export const calculateTotalWithTax = (amount, taxRate = 0) => {
  const tax = amount * (taxRate / 100);
  return amount + tax;
};