// Spinner de chargement
import React from 'react';

const Loader = ({ size = 'md', className = '' }) => {
  const sizes = {
    sm: 'w-6 h-6',
    md: 'w-10 h-10',
    lg: 'w-16 h-16',
  };

  return (
    <div className={`flex items-center justify-center ${className}`}>
      <div className={`${sizes[size]} border-4 border-gray-200 border-t-[#1E3A5F] rounded-full animate-spin`} />
    </div>
  );
};

export default Loader;