// Carte notification (page Notifications)
/*
import React from 'react';
import { Bell, Fuel, Calendar, CreditCard, Package } from 'lucide-react';
import Card from '../ui/Card';
import { formatDateTime } from '../../utils/formatters';

const NotificationCard = ({ notification }) => {
  const getIcon = (type) => {
    const icons = {
      STOCK_DISPONIBLE: Fuel,
      SEUIL_CRITIQUE: Package,
      RESERVATION_CONFIRMEE: Calendar,
      PAIEMENT_CONFIRME: CreditCard,
    };
    const Icon = icons[type] || Bell;
    return <Icon size={16} className="text-[#FF6B35]" />;
  };

  return (
    <Card className={`${!notification.lu ? 'border-l-4 border-[#FF6B35]' : ''}`}>
      <div className="flex items-start gap-3">
        <div className="bg-[#FF6B35]/10 p-2 rounded-lg flex-shrink-0">
          {getIcon(notification.typeNotification)}
        </div>
        <div className="flex-1 min-w-0">
          <p className="font-medium text-gray-900 text-sm">{notification.titre}</p>
          <p className="text-sm text-gray-500 truncate">{notification.message}</p>
          <p className="text-xs text-gray-400 mt-1">{formatDateTime(notification.dateCreation)}</p>
        </div>
        {!notification.lu && (
          <span className="w-2 h-2 rounded-full bg-[#FF6B35] flex-shrink-0 mt-1" />
        )}
      </div>
    </Card>
  );
};

export default NotificationCard;

*/

import React from 'react';
import { Bell, Fuel, Calendar, CreditCard, Package } from 'lucide-react';
import Card from '../ui/Card';
import { formatDateTime } from '../../utils/formatters';

const NotificationCard = ({ notification }) => {
  const getIcon = (type) => {
    const icons = {
      STOCK_DISPONIBLE: Fuel,
      SEUIL_CRITIQUE: Package,
      RESERVATION_CONFIRMEE: Calendar,
      PAIEMENT_CONFIRME: CreditCard,
    };
    const Icon = icons[type] || Bell;
    return <Icon size={16} className="text-[#FF6B35]" />;
  };

  return (
    <Card className={`${!notification.lu ? 'border-l-4 border-[#FF6B35]' : ''}`}>
      <div className="flex items-start gap-3">
        <div className="bg-[#FF6B35]/10 p-2 rounded-lg flex-shrink-0">
          {getIcon(notification.typeNotification)}
        </div>
        <div className="flex-1 min-w-0">
          <p className="font-medium text-gray-900 text-sm">{notification.titre}</p>
          <p className="text-sm text-gray-500 truncate">{notification.message}</p>
          <p className="text-xs text-gray-400 mt-1">{formatDateTime(notification.dateCreation)}</p>
        </div>
        {!notification.lu && (
          <span className="w-2 h-2 rounded-full bg-[#FF6B35] flex-shrink-0 mt-1" />
        )}
      </div>
    </Card>
  );
};

export default NotificationCard;