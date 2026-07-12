// Routeur principal
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
<<<<<<< HEAD
import ForgotPasswordPage from '.pages/auth/ForgotPasswordPage';
import ProfilePage from './pages/consumer/ProfilePage';
import NotificationsPage from './pages/consumer/NotificationsPage';
=======
import ForgotPasswordPage from './pages/auth/ForgotPasswordPage';
import ProfilePage from './pages/common/ProfilePage';
import NotificationsPage from './pages/common/NotificationsPage';
>>>>>>> 0b027f544ec181e416c70a2d0a0afd1748a5bd94

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