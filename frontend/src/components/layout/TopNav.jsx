// barre de naviagtion principale
/*
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
*/


import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Fuel, Bell, User, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ROUTES } from '../../constants/routes';

export default function TopNav({ active }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const initiales = `${user?.prenom?.[0] || ''}${user?.nom?.[0] || ''}`.toUpperCase();

  const handleLogout = async () => {
    await logout();
    navigate('/connexion');
  };

  const link = (key, label, to) => (
    <Link
      to={to}
      className={`text-sm font-medium pb-3 border-b-2 transition-colors ${
        active === key
          ? 'text-[#1E3A5F] border-[#1E3A5F]'
          : 'text-gray-500 border-transparent hover:text-[#1E3A5F]'
      }`}
    >
      {label}
    </Link>
  );

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-40">
      <div className="container-custom h-16 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-[#1E3A5F] rounded-lg p-1.5 text-white">
            <Fuel size={18} />
          </div>
          <span className="font-bold text-[#1E3A5F] text-lg">GDG</span>
        </div>

        <div className="hidden md:flex items-center gap-6">
          {link('profil', 'Mon profil', ROUTES.PROFIL)}
          {link('notifications', 'Notifications', ROUTES.NOTIFICATIONS)}
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={handleLogout}
            className="p-2 text-gray-400 hover:text-[#1E3A5F] transition-colors"
          >
            <LogOut size={18} />
          </button>
          <div className="w-9 h-9 rounded-full bg-[#1E3A5F] text-white text-xs flex items-center justify-center font-bold">
            {initiales || 'U'}
          </div>
        </div>
      </div>
    </nav>
  );
}