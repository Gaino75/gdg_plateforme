// Ecran vide 
import React from 'react';
import { FolderOpen } from 'lucide-react';

const EmptyState = ({ title, description, icon: Icon = FolderOpen, className = '' }) => {
  return (
    <div className={`flex flex-col items-center justify-center py-12 ${className}`}>
      <div className="w-20 h-20 rounded-full bg-gray-100 flex items-center justify-center mb-4">
        <Icon className="text-gray-400" size={32} />
      </div>
      <h3 className="text-lg font-semibold text-gray-900 mb-1">{title}</h3>
      <p className="text-gray-500 text-center max-w-sm">{description}</p>
    </div>
  );
};

export default EmptyState;