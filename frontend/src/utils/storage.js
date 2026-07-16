// localstorage helpers

/**
 * Sauvegarde une valeur dans localStorage
 */
export const setStorage = (key, value) => {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (error) {
    console.error('Erreur de sauvegarde:', error);
  }
};

/**
 * Récupère une valeur du localStorage
 */
export const getStorage = (key, defaultValue = null) => {
  try {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : defaultValue;
  } catch (error) {
    console.error('Erreur de récupération:', error);
    return defaultValue;
  }
};

/**
 * Supprime une valeur du localStorage
 */
export const removeStorage = (key) => {
  try {
    localStorage.removeItem(key);
  } catch (error) {
    console.error('Erreur de suppression:', error);
  }
};

/**
 * Efface tout le localStorage (déconnexion)
 */
export const clearStorage = () => {
  try {
    localStorage.clear();
  } catch (error) {
    console.error('Erreur de nettoyage:', error);
  }
};