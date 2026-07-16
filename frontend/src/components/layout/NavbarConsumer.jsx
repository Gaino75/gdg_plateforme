// naviagtion consommateur

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Package, Bell, User, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ROUTES } from '../../constants/routes';

const NavbarConsumer = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-40">
      <div className="container-custom h-16 flex items-center justify-between">
        <Link to={ROUTES.CONSUMER_DASHBOARD} className="flex items-center gap-2">
          <img src="/logo-gdg.svg" alt="GDG" className="h-8 w-8" />
          <span className="font-bold text-[#1E3A5F]">GDG</span>
        </Link>

        <div className="flex items-center gap-6">
          <Link to={ROUTES.CONSUMER_RESERVATIONS} className="text-sm text-gray-600 hover:text-[#1E3A5F] flex items-center gap-1">
            <Package size={16} /> Réservations
          </Link>
          <Link to={ROUTES.CONSUMER_NOTIFICATIONS} className="text-sm text-gray-600 hover:text-[#1E3A5F] flex items-center gap-1">
            <Bell size={16} /> Notifications
          </Link>
          <Link to={ROUTES.CONSUMER_PROFIL} className="text-sm text-gray-600 hover:text-[#1E3A5F] flex items-center gap-1">
            <User size={16} /> Profil
          </Link>
          <button onClick={handleLogout} className="text-sm text-red-500 hover:text-red-700 flex items-center gap-1">
            <LogOut size={16} /> Déconnexion
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavbarConsumer;