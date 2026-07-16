// Menu lateral dashboard

import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Store,
  Package,
  ShoppingCart,
  CalendarCheck,
  Bell,
  Settings,
  Users,
  FileText,
  AlertTriangle,
  CreditCard,
  BarChart3,
} from 'lucide-react';
import { ROLES } from '../../constants/roles';
import { ROUTES } from '../../constants/routes';

const Sidebar = ({ user }) => {
  if (!user) return null;

  const isConsumer = user.role === ROLES.CONSOMMATEUR;
  const isDistributor = user.role === ROLES.DISTRIBUTEUR;
  const isAdmin = user.role === ROLES.ADMIN;

  // Menu Consommateur
  const consumerMenu = [
    { icon: LayoutDashboard, label: 'Tableau de bord', path: ROUTES.CONSUMER_DASHBOARD },
    { icon: Package, label: 'Mes réservations', path: ROUTES.CONSUMER_RESERVATIONS },
    { icon: FileText, label: 'Mes factures', path: ROUTES.CONSUMER_FACTURES },
    { icon: Bell, label: 'Notifications', path: ROUTES.CONSUMER_NOTIFICATIONS },
    { icon: AlertTriangle, label: 'Signalements', path: ROUTES.CONSUMER_SIGNALEMENTS },
    { icon: Settings, label: 'Mon profil', path: ROUTES.CONSUMER_PROFIL },
  ];

  // Menu Distributeur
  const distributorMenu = [
    { icon: LayoutDashboard, label: 'Tableau de bord', path: ROUTES.DISTRIBUTOR_DASHBOARD },
    { icon: Package, label: 'Gestion du stock', path: ROUTES.DISTRIBUTOR_STOCK },
    { icon: ShoppingCart, label: 'Enregistrer une vente', path: ROUTES.DISTRIBUTOR_VENTE },
    { icon: FileText, label: 'Historique des ventes', path: ROUTES.DISTRIBUTOR_HISTORIQUE },
    { icon: CalendarCheck, label: 'Réservations reçues', path: ROUTES.DISTRIBUTOR_RESERVATIONS },
    { icon: BarChart3, label: 'Statistiques', path: ROUTES.DISTRIBUTOR_STATISTIQUES },
    { icon: Settings, label: 'Paramètres', path: ROUTES.DISTRIBUTOR_PARAMETRES },
  ];

  // Menu Admin
  const adminMenu = [
    { icon: LayoutDashboard, label: 'Tableau de bord', path: ROUTES.ADMIN_DASHBOARD },
    { icon: Store, label: 'Enseignes', path: ROUTES.ADMIN_ENSEIGNES },
    { icon: Store, label: 'Villes', path: ROUTES.ADMIN_VILLES },
    { icon: Store, label: 'Agences', path: ROUTES.ADMIN_AGENCES },
    { icon: Users, label: 'Demandes', path: ROUTES.ADMIN_DEMANDES },
    { icon: Users, label: 'Utilisateurs', path: ROUTES.ADMIN_UTILISATEURS },
    { icon: AlertTriangle, label: 'Signalements', path: ROUTES.ADMIN_SIGNALEMENTS },
    { icon: CreditCard, label: 'Paiements', path: ROUTES.ADMIN_PAIEMENTS },
    { icon: Package, label: 'Stock global', path: ROUTES.ADMIN_STOCKS },
    { icon: BarChart3, label: 'Statistiques', path: ROUTES.ADMIN_STATISTIQUES },
    { icon: FileText, label: 'Journal d\'audit', path: ROUTES.ADMIN_JOURNAL },
    { icon: Settings, label: 'Paramètres', path: ROUTES.ADMIN_PARAMETRES },
  ];

  const menu = isConsumer ? consumerMenu : isDistributor ? distributorMenu : adminMenu;

  return (
    <aside className="w-64 bg-white border-r border-gray-200 flex-shrink-0 hidden md:block overflow-y-auto">
      <div className="p-4">
        <div className="flex items-center gap-3 mb-6 pb-4 border-b border-gray-100">
          <div className="w-10 h-10 rounded-full bg-[#1E3A5F] text-white flex items-center justify-center font-bold text-sm">
            {user.prenom?.[0]}{user.nom?.[0]}
          </div>
          <div className="flex-1 min-w-0">
            <p className="font-medium text-gray-900 text-sm truncate">
              {user.prenom} {user.nom}
            </p>
            <p className="text-xs text-gray-500">{user.role}</p>
          </div>
        </div>

        <nav className="space-y-1">
          {menu.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => `
                flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors
                ${isActive 
                  ? 'bg-[#1E3A5F] text-white' 
                  : 'text-gray-700 hover:bg-gray-100'
                }
              `}
            >
              <item.icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>
      </div>
    </aside>
  );
};

export default Sidebar;