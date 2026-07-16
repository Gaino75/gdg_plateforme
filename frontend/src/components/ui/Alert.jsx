// Message d'alerte

import React from 'react';
import { AlertCircle, CheckCircle, Info, XCircle } from 'lucide-react';

const Alert = ({ type = 'info', message, className = '', onClose }) => {
  const variants = {
    success: {
      bg: 'bg-green-50',
      border: 'border-green-400',
      text: 'text-green-800',
      icon: CheckCircle,
    },
    error: {
      bg: 'bg-red-50',
      border: 'border-red-400',
      text: 'text-red-800',
      icon: XCircle,
    },
    warning: {
      bg: 'bg-yellow-50',
      border: 'border-yellow-400',
      text: 'text-yellow-800',
      icon: AlertCircle,
    },
    info: {
      bg: 'bg-blue-50',
      border: 'border-blue-400',
      text: 'text-blue-800',
      icon: Info,
    },
  };

  const variant = variants[type] || variants.info;
  const Icon = variant.icon;

  return (
    <div className={`${variant.bg} border-l-4 ${variant.border} p-4 rounded-lg ${className}`}>
      <div className="flex items-start gap-3">
        <Icon className={`${variant.text} flex-shrink-0 mt-0.5`} size={18} />
        <p className={`${variant.text} text-sm flex-1`}>{message}</p>
        {onClose && (
          <button onClick={onClose} className={`${variant.text} hover:opacity-70`}>
            <XCircle size={18} />
          </button>
        )}
      </div>
    </div>
  );
};

export default Alert;