// page 404

import React from 'react';
import { Link } from 'react-router-dom';
import { Home } from 'lucide-react';

const NotFoundPage = () => {
  return (
    <div className="min-h-[70vh] flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-6xl font-bold text-[#1E3A5F]">404</h1>
        <p className="text-xl text-gray-600 mt-4">Page non trouvée</p>
        <p className="text-gray-500 mt-2">La page que vous cherchez n'existe pas.</p>
        <Link
          to="/"
          className="inline-flex items-center gap-2 mt-6 px-6 py-3 bg-[#1E3A5F] text-white rounded-lg hover:bg-[#18304F] transition-colors"
        >
          <Home size={18} />
          Retour à l'accueil
        </Link>
      </div>
    </div>
  );
};

export default NotFoundPage;