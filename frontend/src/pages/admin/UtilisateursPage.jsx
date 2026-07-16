// Gestion utiliateurs

import React, { useState, useEffect } from 'react';
import { Search, Ban, CheckCircle, Trash2 } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { formatDateTime } from '../../utils/formatters';
import { ROLES, ROLE_LABELS } from '../../constants/roles';

const UtilisateursPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [utilisateurs, setUtilisateurs] = useState([]);
  const [search, setSearch] = useState('');
  const [showConfirm, setShowConfirm] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axiosInstance.get(API.AUTH.ADMIN_USERS);
      setUtilisateurs(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des utilisateurs', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredUsers = utilisateurs.filter(u =>
    u.nom?.toLowerCase().includes(search.toLowerCase()) ||
    u.email?.toLowerCase().includes(search.toLowerCase()) ||
    u.role?.toLowerCase().includes(search.toLowerCase())
  );

  const handleAction = async (id, action) => {
    try {
      if (action === 'suspendre') {
        await axiosInstance.put(API.AUTH.SUSPENDRE_USER(id), { motif: 'Action admin' });
      } else if (action === 'reactiver') {
        await axiosInstance.put(API.AUTH.REACTIVER_USER(id));
      } else if (action === 'supprimer') {
        await axiosInstance.delete(API.AUTH.DELETE_USER(id));
      }
      await fetchUsers();
    } catch (error) {
      console.error('Erreur', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Gestion des utilisateurs</h1>
        <p className="text-gray-500">{utilisateurs.length} utilisateurs enregistrés</p>
      </div>

      <div className="max-w-sm">
        <Input
          placeholder="Rechercher un utilisateur..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          icon={Search}
        />
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nom</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Rôle</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {filteredUsers.map((u) => (
              <tr key={u.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 font-medium text-gray-900">
                  {u.prenom} {u.nom}
                </td>
                <td className="px-4 py-3">
                  <Badge status={u.role === ROLES.ADMIN ? 'active' : u.role === ROLES.DISTRIBUTEUR ? 'pending' : 'available'}>
                    {ROLE_LABELS[u.role] || u.role}
                  </Badge>
                </td>
                <td className="px-4 py-3 text-sm text-gray-600">{u.email}</td>
                <td className="px-4 py-3">
                  <Badge status={u.statut === 'ACTIF' ? 'available' : u.statut === 'SUSPENDU' ? 'suspended' : 'inactive'}>
                    {u.statut}
                  </Badge>
                </td>
                <td className="px-4 py-3 text-sm text-gray-500">{formatDateTime(u.dateInscription)}</td>
                <td className="px-4 py-3">
                  <div className="flex gap-1">
                    {u.statut === 'ACTIF' && u.id !== user.id && (
                      <Button variant="danger" size="sm" onClick={() => handleAction(u.id, 'suspendre')} className="gap-1">
                        <Ban size={14} />
                      </Button>
                    )}
                    {u.statut === 'SUSPENDU' && (
                      <Button variant="success" size="sm" onClick={() => handleAction(u.id, 'reactiver')} className="gap-1">
                        <CheckCircle size={14} />
                      </Button>
                    )}
                    {u.id !== user.id && (
                      <Button variant="danger" size="sm" onClick={() => handleAction(u.id, 'supprimer')} className="gap-1">
                        <Trash2 size={14} />
                      </Button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
            {filteredUsers.length === 0 && (
              <tr>
                <td colSpan={6} className="px-4 py-8 text-center text-gray-500">
                  Aucun utilisateur trouvé
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <ConfirmDialog
        isOpen={!!showConfirm}
        onClose={() => setShowConfirm(null)}
        onConfirm={() => {
          setShowConfirm(null);
        }}
        title="Confirmer l'action"
        message="Êtes-vous sûr de vouloir effectuer cette action sur cet utilisateur ?"
      />
    </div>
  );
};

export default UtilisateursPage;