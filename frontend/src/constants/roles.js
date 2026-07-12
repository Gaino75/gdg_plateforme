// Roles utilisateurs

export const ROLES = {
  CONSOMMATEUR: 'CONSOMMATEUR',
  DISTRIBUTEUR: 'DISTRIBUTEUR',
  ADMIN: 'ADMIN',
};

export const ROLE_LABELS = {
  [ROLES.CONSOMMATEUR]: 'Consommateur',
  [ROLES.DISTRIBUTEUR]: 'Distributeur',
  [ROLES.ADMIN]: 'Administrateur',
};

export const ROLE_COLORS = {
  [ROLES.CONSOMMATEUR]: '#2ECC71',
  [ROLES.DISTRIBUTEUR]: '#FF6B35',
  [ROLES.ADMIN]: '#1E3A5F',
};

export const ROLE_ROUTES = {
  [ROLES.CONSOMMATEUR]: '/consommateur',
  [ROLES.DISTRIBUTEUR]: '/distributeur',
  [ROLES.ADMIN]: '/admin',
};