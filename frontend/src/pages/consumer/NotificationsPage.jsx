// mes notifications 
import { useEffect, useState } from 'react';
import { CheckCheck, Package, Calendar, CreditCard, Fuel } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { useAuth } from '../../context/AuthContext';
import TopNav from '../../components/layout/TopNav';

const ICONS = { SEUIL_CRITIQUE: Package, RESERVATION_CONFIRMEE: Calendar, PAIEMENT_CONFIRME: CreditCard, STOCK_DISPONIBLE: Fuel };
const TABS = [
  { key: 'toutes', label: 'Toutes' }, { key: 'non-lues', label: 'Non lues' },
  { key: 'alertes', label: 'Alertes' }, { key: 'reservations', label: 'Réservations' },
];

export default function NotificationsPage() {
  const { user } = useAuth();
  const [notifs, setNotifs] = useState([]);
  const [tab, setTab] = useState('toutes');

  const load = async () => {
    const url = tab === 'non-lues'
      ? `/api/notifications/user/${user.id}/unread`
      : `/api/notifications/user/${user.id}`;
    const { data } = await axiosInstance.get(url);
    setNotifs(data);
  };

  useEffect(() => { if (user?.id) load(); }, [tab, user]);

  const marquerLue = async (id) => {
    await axiosInstance.put(`/api/notifications/${id}/read`);
    load();
  };

  const marquerTout = async () => {
    await axiosInstance.put(`/api/notifications/user/${user.id}/read-all`);
    load();
  };

  const nonLues = notifs.filter((n) => !n.lu).length;

  return (
    <div className="min-h-screen bg-slate-50">
      <TopNav active="notifications" />
      <div className="max-w-2xl mx-auto px-6 py-10">
        <div className="flex items-center justify-between mb-1">
          <h1 className="text-2xl font-bold text-slate-900">Notifications</h1>
          <button onClick={marquerTout} className="text-sm text-teal-700 font-medium flex items-center gap-1 hover:underline">
            <CheckCheck size={15} /> Tout marquer comme lu
          </button>
        </div>
        <p className="text-slate-500 text-sm mb-5">{nonLues} notification{nonLues !== 1 ? 's' : ''} non lue{nonLues !== 1 ? 's' : ''}.</p>

        <div className="flex gap-2 mb-6">
          {TABS.map((t) => (
            <button key={t.key} onClick={() => setTab(t.key)}
              className={`px-3 py-1.5 rounded-full text-sm font-medium ${tab === t.key ? 'bg-teal-800 text-white' : 'bg-white border border-slate-200 text-slate-600'}`}>
              {t.label}
            </button>
          ))}
        </div>

        <div className="space-y-2">
          {notifs.map((n) => {
            const Icon = ICONS[n.typeNotification] || Package;
            return (
              <button key={n.id} onClick={() => marquerLue(n.id)}
                className="w-full text-left bg-white border border-slate-200 rounded-xl px-4 py-3 flex items-start gap-3 hover:border-teal-200">
                <div className="bg-teal-50 text-teal-700 rounded-lg p-2 shrink-0"><Icon size={16} /></div>
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-slate-900 text-sm">{n.titre}</p>
                  <p className="text-slate-500 text-xs truncate">{n.message}</p>
                </div>
                <div className="text-right shrink-0">
                  <p className="text-xs text-slate-400">{new Date(n.dateCreation).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })}</p>
                  {!n.lu && <span className="inline-block w-2 h-2 rounded-full bg-orange-400 mt-1" />}
                </div>
              </button>
            );
          })}
          {notifs.length === 0 && <p className="text-center text-slate-400 text-sm py-10">Aucune notification.</p>}
        </div>
      </div>
    </div>
  );
}