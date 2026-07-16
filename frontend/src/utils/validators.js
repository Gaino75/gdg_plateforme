// validation formulaires

/**
 * Valide une adresse email
 */
export const isValidEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

/**
 * Valide un numéro de téléphone camerounais
 */
export const isValidPhone = (phone) => {
  const regex = /^(\+237)?[6-9][0-9]{8}$/;
  return regex.test(phone.replace(/\s/g, ''));
};

/**
 * Valide un mot de passe (minimum 8 caractères)
 */
export const isValidPassword = (password) => {
  return password && password.length >= 8;
};

/**
 * Valide un montant (positif)
 */
export const isValidAmount = (amount) => {
  return amount && amount > 0;
};

/**
 * Valide une quantité (positive)
 */
export const isValidQuantity = (quantity) => {
  return quantity && quantity > 0;
};

/**
 * Valide un champ requis
 */
export const isRequired = (value) => {
  return value && value.toString().trim().length > 0;
};

/**
 * Valide une longueur minimale
 */
export const minLength = (value, min) => {
  return value && value.length >= min;
};