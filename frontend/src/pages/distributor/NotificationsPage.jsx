// Notifications et alertes 

import React, { useState, useEffect } from 'react';
import { Bell, Package, AlertTriangle, CheckCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNotifications } from '../../hooks/useNotifications';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime } from '../../utils/formatters';

const NotificationsPage = () => {
  const { user } = useAuth();
  const { notifications, unreadCount, loading, markAsRead, markAllAsRead, refetch } = useNotifications(user?.id);

  const getIcon = (type) => {
    const icons = {
      STOCK_DISPONIBLE: Package,
      SEUIL_CRITIQUE: AlertTriangle,
      RESERVATION_CONFIRMEE: CheckCircle,
      PAIEMENT_CONFIRME: CheckCircle,
    };
    const Icon = icons[type] || Bell;
    return <Icon size={16} className="text-[#FF6B35]" />;
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Notifications</h1>
          <p className="text-gray-500">{unreadCount} notification non lue</p>
        </div>
        {unreadCount > 0 && (
          <Button variant="outline" size="sm" onClick={markAllAsRead}>
            Tout marquer comme lu
          </Button>
        )}
      </div>

      <div className="space-y-3">
        {notifications.map((notif) => (
          <Card key={notif.id} className={`${!notif.lu ? 'border-l-4 border-[#FF6B35]' : ''}`}>
            <div className="flex items-start gap-3">
              <div className="bg-[#FF6B35]/10 p-2 rounded-lg flex-shrink-0">
                {getIcon(notif.typeNotification)}
              </div>
              <div className="flex-1">
                <p className="font-medium text-gray-900">{notif.titre}</p>
                <p className="text-sm text-gray-500">{notif.message}</p>
                <p className="text-xs text-gray-400 mt-1">{formatDateTime(notif.dateCreation)}</p>
              </div>
              {!notif.lu && (
                <button
                  onClick={() => markAsRead(notif.id)}
                  className="text-xs text-[#FF6B35] hover:underline whitespace-nowrap"
                >
                  Marquer comme lu
                </button>
              )}
            </div>
          </Card>
        ))}
        {notifications.length === 0 && (
          <div className="text-center py-10">
            <Bell size={48} className="mx-auto text-gray-300 mb-3" />
            <p className="text-gray-500">Aucune notification</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default NotificationsPage;