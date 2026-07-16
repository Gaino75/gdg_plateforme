// Page contact

import React, { useState } from 'react';
import { Mail, Phone, MapPin, Send } from 'lucide-react';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Textarea from '../../components/ui/Textarea';
import Alert from '../../components/ui/Alert';

const ContactPage = () => {
  const [form, setForm] = useState({ nom: '', email: '', message: '' });
  const [sent, setSent] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    // TODO: Appel API
    setTimeout(() => {
      setSent(true);
      setLoading(false);
      setForm({ nom: '', email: '', message: '' });
    }, 1500);
  };

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-[#1E3A5F]">Contactez-nous</h1>
        <p className="text-gray-500 mt-2">Une question ? Un problème ? N'hésitez pas à nous contacter</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="text-center">
          <Mail size={24} className="mx-auto text-[#FF6B35] mb-2" />
          <p className="font-medium">Email</p>
          <p className="text-sm text-gray-500">contact@gdg.cm</p>
        </Card>
        <Card className="text-center">
          <Phone size={24} className="mx-auto text-[#FF6B35] mb-2" />
          <p className="font-medium">Téléphone</p>
          <p className="text-sm text-gray-500">+237 690 000 000</p>
        </Card>
        <Card className="text-center">
          <MapPin size={24} className="mx-auto text-[#FF6B35] mb-2" />
          <p className="font-medium">Adresse</p>
          <p className="text-sm text-gray-500">Yaoundé, Cameroun</p>
        </Card>
      </div>

      <Card>
        {sent && <Alert type="success" message="Message envoyé avec succès !" className="mb-4" />}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Nom"
              value={form.nom}
              onChange={(e) => setForm({ ...form, nom: e.target.value })}
              required
            />
            <Input
              label="Email"
              type="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              required
            />
          </div>
          <Textarea
            label="Message"
            value={form.message}
            onChange={(e) => setForm({ ...form, message: e.target.value })}
            rows={5}
            required
          />
          <Button type="submit" variant="orange" fullWidth loading={loading} className="gap-2">
            <Send size={18} /> Envoyer
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default ContactPage;