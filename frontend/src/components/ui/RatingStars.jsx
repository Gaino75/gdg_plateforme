// Etoiles d'evaluation pour les avis des utilisateurs

import React from 'react';
import { Star } from 'lucide-react';

const RatingStars = ({ rating = 0, max = 5, size = 16, className = '' }) => {
  return (
    <div className={`flex items-center gap-0.5 ${className}`}>
      {Array.from({ length: max }).map((_, i) => (
        <Star
          key={i}
          size={size}
          className={`${i < rating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'}`}
        />
      ))}
      <span className="ml-1 text-sm text-gray-500">{rating.toFixed(1)}</span>
    </div>
  );
};

export default RatingStars;