// Routeur principal

/*
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import ForgotPasswordPage from '.pages/auth/ForgotPasswordPage';
import ProfilePage from './pages/consumer/ProfilePage';
import NotificationsPage from './pages/consumer/NotificationsPage';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/connexion" element={<LoginPage />} />
          <Route path="/inscription" element={<RegisterPage />} />
          <Route path="/mot-de-passe-oublie" element={<ForgotPasswordPage />} />
          <Route path="/profil" element={<ProfilePage />} />
          <Route path="/notifications" element={<NotificationsPage />} />
          <Route path="/*" element={<h1>page not found</h1>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
*/

import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ROUTES } from './constants/routes';

// Layouts
import PublicLayout from './components/layout/PublicLayout';
import DashboardLayout from './components/layout/DashboardLayout';
import StationsPage from './pages/public/StationsPages';
import ContactPage from './pages/public/ContactPage';

// Guards
import { PrivateRoute, ConsumerRoute, DistributorRoute, AdminRoute } from './components/guards';

// Pages Publiques
import HomePage from './pages/public/HomePage';
import EnseigneDetailPage from './pages/public/EnseigneDetailPage';
import AgenceDetailPublicPage from './pages/public/AgenceDetailPage';
import NotFoundPage from './pages/public/NotFoundPage';

// Pages Auth
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import ForgotPasswordPage from './pages/auth/ForgotPasswordPage';
import ResetPasswordPage from './pages/auth/ResetPasswordPage';

// Pages Consommateur
import ConsumerDashboardPage from './pages/consumer/DashboardPage';
import ConsumerAgenceDetailPage from './pages/consumer/AgenceDetailPage';
import ReservationsPage from './pages/consumer/ReservationsPage';
import ReservationDetailPage from './pages/consumer/ReservationDetailPage';
import NouvelleReservationPage from './pages/consumer/NouvelleReservationPage';
import PaiementPage from './pages/consumer/PaiementPage';
import FacturesPage from './pages/consumer/FacturesPage';
import NotificationsPage from './pages/consumer/NotificationsPage';
import AbonnementsPage from './pages/consumer/AbonnementsPage';
import SignalementsPage from './pages/consumer/SignalementsPage';
import NouveauSignalementPage from './pages/consumer/NouveauSignalementPage';
import ConsumerProfilePage from './pages/consumer/ProfilePage';

// Pages Distributeur
import DistributorDashboardPage from './pages/distributor/DashboardPage';
import StockPage from './pages/distributor/StockPage';
import EnregistrerVentePage from './pages/distributor/EnregistrerVentePage';
import HistoriqueVentesPage from './pages/distributor/HistoriqueVentesPage';
import ApprovisionnementPage from './pages/distributor/ApprovisionnementPage';
import DistributorReservationsPage from './pages/distributor/ReservationsPage';
import StatistiquesPage from './pages/distributor/StatistiquesPage';
import ParametresAgencePage from './pages/distributor/ParametresAgencePage';
import DistributorProfilePage from './pages/distributor/ProfilePage';

// Pages Admin
import AdminDashboardPage from './pages/admin/DashboardPage';
import EnseignesPage from './pages/admin/EnseignesPage';
import VillesPage from './pages/admin/VillesPage';
import AgencesPage from './pages/admin/AgencesPage';
import DemandesPage from './pages/admin/DemandesPage';
import UtilisateursPage from './pages/admin/UtilisateursPage';
import AdminSignalementsPage from './pages/admin/SignalementsPage';
import PaiementsPage from './pages/admin/PaiementsPage';
import StocksPage from './pages/admin/StocksPage';
import AdminStatistiquesPage from './pages/admin/StatistiquesPage';
import JournalAuditPage from './pages/admin/JournalAuditPage';
import ParametresPage from './pages/admin/ParametresPage';
import AdminProfilePage from './pages/admin/ProfilePage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Routes Publiques */}
        <Route element={<PublicLayout />}>
          <Route path={ROUTES.HOME} element={<HomePage />} />
          <Route path={ROUTES.ENSEIGNE} element={<EnseigneDetailPage />} />
          <Route path={ROUTES.AGENCE} element={<AgenceDetailPublicPage />} />
          <Route path={ROUTES.LOGIN} element={<LoginPage />} />
          <Route path={ROUTES.REGISTER} element={<RegisterPage />} />
          <Route path={ROUTES.FORGOT_PASSWORD} element={<ForgotPasswordPage />} />
          <Route path={ROUTES.RESET_PASSWORD} element={<ResetPasswordPage />} />
          <Route path="*" element={<NotFoundPage />} />
          <Route path={ROUTES.STATIONS} element={<StationsPage/>}/>
          <Route path={ROUTES.CONTACT} element={<ContactPage/>}/>
        </Route>

        {/* Routes Consommateur */}
        <Route element={<DashboardLayout />}>
          <Route path={ROUTES.CONSUMER_DASHBOARD} element={
            <ConsumerRoute><ConsumerDashboardPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_AGENCE} element={
            <ConsumerRoute><ConsumerAgenceDetailPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_RESERVATIONS} element={
            <ConsumerRoute><ReservationsPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_RESERVATION} element={
            <ConsumerRoute><ReservationDetailPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_NEW_RESERVATION} element={
            <ConsumerRoute><NouvelleReservationPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_PAIEMENT} element={
            <ConsumerRoute><PaiementPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_FACTURES} element={
            <ConsumerRoute><FacturesPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_NOTIFICATIONS} element={
            <ConsumerRoute><NotificationsPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_ABONNEMENTS} element={
            <ConsumerRoute><AbonnementsPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_SIGNALEMENTS} element={
            <ConsumerRoute><SignalementsPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_NEW_SIGNALEMENT} element={
            <ConsumerRoute><NouveauSignalementPage /></ConsumerRoute>
          } />
          <Route path={ROUTES.CONSUMER_PROFIL} element={
            <ConsumerRoute><ConsumerProfilePage /></ConsumerRoute>
          } />
        </Route>

        {/* Routes Distributeur */}
        <Route element={<DashboardLayout />}>
          <Route path={ROUTES.DISTRIBUTOR_DASHBOARD} element={
            <DistributorRoute><DistributorDashboardPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_STOCK} element={
            <DistributorRoute><StockPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_VENTE} element={
            <DistributorRoute><EnregistrerVentePage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_HISTORIQUE} element={
            <DistributorRoute><HistoriqueVentesPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_APPROVISIONNEMENT} element={
            <DistributorRoute><ApprovisionnementPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_RESERVATIONS} element={
            <DistributorRoute><DistributorReservationsPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_STATISTIQUES} element={
            <DistributorRoute><StatistiquesPage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_PARAMETRES} element={
            <DistributorRoute><ParametresAgencePage /></DistributorRoute>
          } />
          <Route path={ROUTES.DISTRIBUTOR_PROFIL} element={
            <DistributorRoute><DistributorProfilePage /></DistributorRoute>
          } />
        </Route>

        {/* Routes Admin */}
        <Route element={<DashboardLayout />}>
          <Route path={ROUTES.ADMIN_DASHBOARD} element={
            <AdminRoute><AdminDashboardPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_ENSEIGNES} element={
            <AdminRoute><EnseignesPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_VILLES} element={
            <AdminRoute><VillesPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_AGENCES} element={
            <AdminRoute><AgencesPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_DEMANDES} element={
            <AdminRoute><DemandesPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_UTILISATEURS} element={
            <AdminRoute><UtilisateursPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_SIGNALEMENTS} element={
            <AdminRoute><AdminSignalementsPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_PAIEMENTS} element={
            <AdminRoute><PaiementsPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_STOCKS} element={
            <AdminRoute><StocksPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_STATISTIQUES} element={
            <AdminRoute><AdminStatistiquesPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_JOURNAL} element={
            <AdminRoute><JournalAuditPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_PARAMETRES} element={
            <AdminRoute><ParametresPage /></AdminRoute>
          } />
          <Route path={ROUTES.ADMIN_PROFIL} element={
            <AdminRoute><AdminProfilePage /></AdminRoute>
          } />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;