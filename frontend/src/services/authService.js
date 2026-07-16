// login, logout,register,profil

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const authService = {
  login: (email, motDePasse) => 
    axiosInstance.post(API.AUTH.LOGIN, { email, motDePasse }),
  
  registerConsumer: (data) => 
    axiosInstance.post(API.AUTH.REGISTER_CONSUMER, data),
  
  registerDistributor: (data) => 
    axiosInstance.post(API.AUTH.REGISTER_DISTRIBUTOR, data),
  
  logout: () => 
    axiosInstance.post(API.AUTH.LOGOUT),
  
  getProfil: () => 
    axiosInstance.get(API.AUTH.PROFIL),
  
  forgotPassword: (email) => 
    axiosInstance.post(API.AUTH.FORGOT_PASSWORD, { email }),
  
  resetPassword: (resetToken, nouveauMotDePasse) => 
    axiosInstance.post(API.AUTH.RESET_PASSWORD, { resetToken, nouveauMotDePasse }),
  
  // Admin
  getUsers: () => 
    axiosInstance.get(API.AUTH.ADMIN_USERS),
  
  suspendreUser: (id, motif) => 
    axiosInstance.put(API.AUTH.SUSPENDRE_USER(id), { motif }),
  
  reactiverUser: (id) => 
    axiosInstance.put(API.AUTH.REACTIVER_USER(id)),
  
  deleteUser: (id) => 
    axiosInstance.delete(API.AUTH.DELETE_USER(id)),
};