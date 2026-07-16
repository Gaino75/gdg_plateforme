// mon profil

import React from 'react';
import { useAuth } from '../../context/AuthContext';
import Card from '../../components/ui/Card';
import { formatDate } from '../../utils/formatters';

const ProfilePage = () => {
  const { user } = useAuth();

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Mon profil</h1>
      
      <Card>
        <div className="flex items-center gap-4 mb-6">
          <div className="w-16 h-16 rounded-full bg-[#1E3A5F] text-white flex items-center justify-center text-xl font-bold">
            {user?.prenom?.[0]}{user?.nom?.[0]}
          </div>
          <div>
            <p className="text-lg font-semibold">{user?.prenom} {user?.nom}</p>
            <p className="text-sm text-gray-500">{user?.email}</p>
            <p className="text-sm text-gray-500">{user?.role}</p>
          </div>
        </div>
        <div className="space-y-2 text-sm">
          <p><strong>Téléphone :</strong> {user?.telephone || 'Non renseigné'}</p>
          <p><strong>Agence :</strong> {user?.agenceId || 'Non affecté'}</p>
          <p><strong>Inscrit le :</strong> {formatDate(user?.dateInscription)}</p>
        </div>
      </Card>
    </div>
  );
};

export default ProfilePage;