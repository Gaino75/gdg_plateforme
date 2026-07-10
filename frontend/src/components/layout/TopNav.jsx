import { Link } from 'react-router-dom';
import { Fuel } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export default function TopNav({ active }) {
  const { user } = useAuth();
  const initiales = `${user?.prenom?.[0] || ''}${user?.nom?.[0] || ''}`.toUpperCase();

  const link = (key, label, to) => (
    <Link to={to} className={`text-sm font-medium pb-3 border-b-2 ${active === key ? 'text-slate-900 border-teal-800' : 'text-slate-500 border-transparent'}`}>
      {label}
    </Link>
  );

  return (
    <nav className="bg-white border-b border-slate-200">
      <div className="max-w-4xl mx-auto px-6 h-14 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="bg-teal-800 rounded-md p-1.5 text-white"><Fuel size={15} /></div>
          <span className="font-bold text-slate-900">GPG</span>
        </div>
        <div className="flex gap-6">
          {link('profil', 'Mon profil', '/profil')}
          {link('notifications', 'Notifications', '/notifications')}
        </div>
        <div className="w-8 h-8 rounded-full bg-teal-800 text-white text-xs flex items-center justify-center font-bold">
          {initiales}
        </div>
      </div>
    </nav>
  );
}