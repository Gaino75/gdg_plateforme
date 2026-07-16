// Notification toast

import React, { useEffect } from 'react';
import { X, CheckCircle, AlertCircle, Info, AlertTriangle } from 'lucide-react';

const Toast = ({ message, type = 'info', onClose, duration = 3000 }) => {
  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(onClose, duration);
      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  const variants = {
    success: { icon: CheckCircle, bg: 'bg-green-50', border: 'border-green-400', text: 'text-green-800' },
    error: { icon: AlertCircle, bg: 'bg-red-50', border: 'border-red-400', text: 'text-red-800' },
    warning: { icon: AlertTriangle, bg: 'bg-yellow-50', border: 'border-yellow-400', text: 'text-yellow-800' },
    info: { icon: Info, bg: 'bg-blue-50', border: 'border-blue-400', text: 'text-blue-800' },
  };

  const variant = variants[type] || variants.info;
  const Icon = variant.icon;

  return (
    <div className={`fixed bottom-4 right-4 max-w-sm ${variant.bg} border-l-4 ${variant.border} p-4 rounded-lg shadow-lg animate-fade-in z-50`}>
      <div className="flex items-start gap-3">
        <Icon className={`${variant.text} flex-shrink-0 mt-0.5`} size={18} />
        <p className={`${variant.text} text-sm flex-1`}>{message}</p>
        <button onClick={onClose} className={`${variant.text} hover:opacity-70`}>
          <X size={16} />
        </button>
      </div>
    </div>
  );
};

export default Toast;