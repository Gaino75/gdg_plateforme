// navigation admin

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { LayoutDashboard, Store, Users, AlertTriangle, CreditCard, Package, FileText, Settings, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ROUTES } from '../../constants/routes';

const NavbarAdmin = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="bg-[#1E3A5F] text-white sticky top-0 z-40">
      <div className="container-custom h-16 flex items-center justify-between">
        <Link to={ROUTES.ADMIN_DASHBOARD} className="flex items-center gap-2">
          <img src="/logo-gdg.png" alt="GDG" className="h-8 w-8 invert" />
          <span className="font-bold">GDG</span>
          <span className="text-xs text-gray-300 hidden md:inline">Administration</span>
        </Link>

        <div className="flex items-center gap-4 text-sm">
          <Link to={ROUTES.ADMIN_DASHBOARD} className="hover:text-[#FF6B35] transition-colors">
            <LayoutDashboard size={16} className="inline mr-1" /> Dashboard
          </Link>
          <Link to={ROUTES.ADMIN_AGENCES} className="hover:text-[#FF6B35] transition-colors">
            <Store size={16} className="inline mr-1" /> Agences
          </Link>
          <Link to={ROUTES.ADMIN_UTILISATEURS} className="hover:text-[#FF6B35] transition-colors">
            <Users size={16} className="inline mr-1" /> Utilisateurs
          </Link>
          <Link to={ROUTES.ADMIN_SIGNALEMENTS} className="hover:text-[#FF6B35] transition-colors">
            <AlertTriangle size={16} className="inline mr-1" /> Signalements
          </Link>
          <Link to={ROUTES.ADMIN_PAIEMENTS} className="hover:text-[#FF6B35] transition-colors">
            <CreditCard size={16} className="inline mr-1" /> Paiements
          </Link>
          <Link to={ROUTES.ADMIN_STOCKS} className="hover:text-[#FF6B35] transition-colors">
            <Package size={16} className="inline mr-1" /> Stocks
          </Link>
          <Link to={ROUTES.ADMIN_JOURNAL} className="hover:text-[#FF6B35] transition-colors">
            <FileText size={16} className="inline mr-1" /> Audit
          </Link>
          <Link to={ROUTES.ADMIN_PARAMETRES} className="hover:text-[#FF6B35] transition-colors">
            <Settings size={16} className="inline mr-1" /> Paramètres
          </Link>
          <button onClick={handleLogout} className="text-red-300 hover:text-red-400 transition-colors">
            <LogOut size={16} className="inline mr-1" />
          </button>
        </div>
      </div>
    </nav>
  );
};

export default NavbarAdmin;