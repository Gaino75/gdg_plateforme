// navigation publique

import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Menu, X } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ROUTES } from '../../constants/routes';
import { COLORS } from '../../constants/colors';

const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  const navLinks = [
    { label: 'Accueil', href: ROUTES.HOME },
    { label: 'Stations', href: ROUTES.STATIONS },
  ];

  return (
    <nav className="bg-[#1E3A5F] text-white shadow-lg sticky top-0 z-50">
      <div className="container-custom">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to={ROUTES.HOME} className="flex items-center gap-2">
            <img src="/logo-gdg.png" alt="GDG" className="h-10 w-10" />
            <span className="text-xl font-bold tracking-tight">GDG</span>
            <span className="hidden md:inline text-xs text-gray-300 font-light">
              Gaz Distribution & Gestion
            </span>
          </Link>

          {/* Navigation Desktop */}
          <div className="hidden md:flex items-center gap-6">
            {navLinks.map((link) => (
              <Link
                key={link.href}
                to={link.href}
                className="text-sm hover:text-[#FF6B35] transition-colors"
              >
                {link.label}
              </Link>
            ))}
            
            {isAuthenticated ? (
              <div className="flex items-center gap-4">
                <Link
                  to={ROUTES.CONSUMER_DASHBOARD}
                  className="text-sm hover:text-[#FF6B35] transition-colors"
                >
                  Tableau de bord
                </Link>
                <button
                  onClick={handleLogout}
                  className="bg-[#FF6B35] hover:bg-[#E55A2A] px-4 py-2 rounded-lg text-sm transition-colors"
                >
                  Déconnexion
                </button>
              </div>
            ) : (
              <div className="flex items-center gap-3">
                <Link
                  to={ROUTES.LOGIN}
                  className="text-sm hover:text-[#FF6B35] transition-colors"
                >
                  Connexion
                </Link>
                <Link
                  to={ROUTES.REGISTER}
                  className="bg-[#FF6B35] hover:bg-[#E55A2A] px-4 py-2 rounded-lg text-sm transition-colors"
                >
                  Créer un compte
                </Link>
              </div>
            )}
          </div>

          {/* Menu Mobile */}
          <button
            className="md:hidden text-white"
            onClick={() => setMobileOpen(!mobileOpen)}
          >
            {mobileOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>

        {/* Navigation Mobile */}
        {mobileOpen && (
          <div className="md:hidden pb-4 border-t border-gray-700">
            <div className="flex flex-col gap-3 pt-3">
              {navLinks.map((link) => (
                <Link
                  key={link.href}
                  to={link.href}
                  className="text-sm hover:text-[#FF6B35] transition-colors"
                  onClick={() => setMobileOpen(false)}
                >
                  {link.label}
                </Link>
              ))}
              {isAuthenticated ? (
                <>
                  <Link
                    to={ROUTES.CONSUMER_DASHBOARD}
                    className="text-sm hover:text-[#FF6B35] transition-colors"
                    onClick={() => setMobileOpen(false)}
                  >
                    Tableau de bord
                  </Link>
                  <button
                    onClick={() => { handleLogout(); setMobileOpen(false); }}
                    className="text-left text-sm text-[#FF6B35] hover:text-[#E55A2A] transition-colors"
                  >
                    Déconnexion
                  </button>
                </>
              ) : (
                <>
                  <Link
                    to={ROUTES.LOGIN}
                    className="text-sm hover:text-[#FF6B35] transition-colors"
                    onClick={() => setMobileOpen(false)}
                  >
                    Connexion
                  </Link>
                  <Link
                    to={ROUTES.REGISTER}
                    className="bg-[#FF6B35] text-center px-4 py-2 rounded-lg text-sm hover:bg-[#E55A2A] transition-colors"
                    onClick={() => setMobileOpen(false)}
                  >
                    Créer un compte
                  </Link>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;