// Navigation distributeur

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { LayoutDashboard, Package, ShoppingCart, Bell, Settings, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ROUTES } from '../../constants/routes';

const NavbarDistributor = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-40">
      <div className="container-custom h-16 flex items-center justify-between">
        <Link to={ROUTES.DISTRIBUTOR_DASHBOARD} className="flex items-center gap-2">
          <img src="/logo-gdg.svg" alt="GDG" className="h-8 w-8" />
          <span className="font-bold text-[#1E3A5F]">GDG</span>
          <span className="text-sm text-gray-400 hidden md:inline">Distributeur</span>
        </Link>

        <div className="flex items-center gap-4">
          <Link to={ROUTES.DISTRIBUTOR_DASHBOARD} className="text-sm text-gray-600 hover:text-[#1E3A5F]">
            <LayoutDashboard size={16} className="inline mr-1" /> Dashboard
          </Link>
          <Link to={ROUTES.DISTRIBUTOR_STOCK} className="text-sm text-gray-600 hover:text-[#1E3A5F]">
            <Package size={16} className="inline mr-1" /> Stock
          </Link>
          <Link to={ROUTES.DISTRIBUTOR_VENTE} className="text-sm text-gray-600 hover:text-[#1E3A5F]">
            <ShoppingCart size={16} className="inline mr-1" /> Vente
          </Link>
          <Link to={ROUTES.DISTRIBUTOR_RESERVATIONS} className="text-sm text-gray-600 hover:text-[#1E3A5F]">
            <Bell size={16} className="inline mr-1" /> Réservations
          </Link>
          <Link to={ROUTES.DISTRIBUTOR_PARAMETRES} className="text-sm text-gray-600 hover:text-[#1E3A5F]">
            <Settings size={16} className="inline mr-1" /> Paramètres
          </Link>
          <button onClick={handleLogout} className="text-sm text-red-500 hover:text-red-700">
            <LogOut size={16} className="inline mr-1" /> Déconnexion
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavbarDistributor;