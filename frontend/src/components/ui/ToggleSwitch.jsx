// Switch pour les abonnements

import React from 'react';

const ToggleSwitch = ({ checked, onChange, label, className = '' }) => {
  return (
    <label className={`flex items-center gap-3 cursor-pointer ${className}`}>
      <div className="relative">
        <input
          type="checkbox"
          checked={checked}
          onChange={(e) => onChange(e.target.checked)}
          className="sr-only"
        />
        <div className={`
          w-11 h-6 rounded-full transition-colors duration-200
          ${checked ? 'bg-[#2ECC71]' : 'bg-gray-300'}
        `}>
          <div className={`
            w-5 h-5 bg-white rounded-full shadow-md transform transition-transform duration-200
            ${checked ? 'translate-x-5' : 'translate-x-0.5'}
            mt-0.5
          `} />
        </div>
      </div>
      {label && <span className="text-sm text-gray-700">{label}</span>}
    </label>
  );
};

export default ToggleSwitch;