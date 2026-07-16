// Acces au contexte auth

import { useAuth as useAuthContext } from '../context/AuthContext';

export const useAuth = () => {
  return useAuthContext();
};