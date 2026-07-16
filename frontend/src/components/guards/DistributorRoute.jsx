// ROUTE reservees distributeurs

import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { ROLES } from '../../constants/roles';
import Loader from '../ui/Loader';

const DistributorRoute = ({ children }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <Loader />
      </div>
    );
  }

  if (!user || user.role !== ROLES.DISTRIBUTEUR) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default DistributorRoute;