// Zone de texte

import React from 'react';

const Textarea = ({ label, name, value, onChange, placeholder, rows = 3, required = false, error = '', className = '' }) => {
  return (
    <div className={`mb-4 ${className}`}>
      {label && (
        <label htmlFor={name} className="block text-sm font-medium text-gray-700 mb-1.5">
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}
      <textarea
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        rows={rows}
        required={required}
        className={`
          w-full px-3 py-2.5 rounded-lg border transition-colors duration-200
          focus:outline-none focus:ring-2 focus:ring-[#1E3A5F] focus:border-transparent
          ${error ? 'border-red-500 focus:ring-red-500' : 'border-gray-300'}
        `}
      />
      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
};

export default Textarea;