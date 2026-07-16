// pied de page

import React from 'react';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-white border-t border-gray-200 py-6">
      <div className="container-custom">
        <div className="flex flex-col md:flex-row justify-between items-center gap-4">
          <div className="flex items-center gap-2">
            <img src="/logo-gdg.svg" alt="GDG" className="h-8 w-8" />
            <span className="font-semibold text-gray-900">GDG</span>
            <span className="text-sm text-gray-500">Gaz Distribution & Gestion</span>
          </div>
          <div className="text-sm text-gray-500">
            © {currentYear} GDG. Tous droits réservés.
          </div>
          <div className="flex gap-4 text-sm text-gray-500">
            <span>Réseau Total · Ola Energy · Tradex · Mrs · Green Oil</span>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;