// Journal des actions 

import React, { useState, useEffect } from 'react';
import { FileText, Search, Calendar } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Input from '../../components/ui/Input';
import Loader from '../../components/ui/Loader';
import Pagination from '../../components/ui/Pagination';
import { formatDateTime } from '../../utils/formatters';

const JournalAuditPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [journals, setJournals] = useState([]);
  const [pagination, setPagination] = useState({ page: 0, totalPages: 1, total: 0 });
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchJournal(0);
  }, []);

  const fetchJournal = async (page) => {
    try {
      const response = await axiosInstance.get(`${API.ADMIN.JOURNAL}?page=${page}&size=20`);
      setJournals(response.data.content || []);
      setPagination({
        page: response.data.pageable?.pageNumber || 0,
        totalPages: response.data.totalPages || 1,
        total: response.data.totalElements || 0,
      });
    } catch (error) {
      console.error('Erreur de chargement du journal', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Journal d'audit</h1>
        <p className="text-gray-500">Historique paginé des actions</p>
      </div>

      <div className="flex flex-wrap items-center gap-4">
        <div className="flex-1 min-w-[200px]">
          <Input
            placeholder="Rechercher une action..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            icon={Search}
          />
        </div>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Calendar size={16} />
          <span>{pagination.total} entrées</span>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Administrateur</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Entité concernée</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {journals.map((j, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-sm text-gray-600">{formatDateTime(j.dateAction)}</td>
                <td className="px-4 py-3 font-medium text-gray-900">
                  {j.adminNom || j.utilisateurId || 'Système'}
                </td>
                <td className="px-4 py-3">
                  <span className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${
                    j.action?.includes('VALIDER') ? 'bg-green-100 text-green-800' :
                    j.action?.includes('SUSPENDRE') ? 'bg-red-100 text-red-800' :
                    j.action?.includes('REJETER') ? 'bg-red-100 text-red-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {j.action}
                  </span>
                </td>
                <td className="px-4 py-3 text-sm text-gray-600">
                  {j.entiteType} {j.entiteId ? `#${j.entiteId}` : ''}
                </td>
              </tr>
            ))}
            {journals.length === 0 && (
              <tr>
                <td colSpan={4} className="px-4 py-8 text-center text-gray-500">
                  Aucune entrée dans le journal
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <Pagination
        currentPage={pagination.page}
        totalPages={pagination.totalPages}
        onPageChange={fetchJournal}
      />
    </div>
  );
};

export default JournalAuditPage;