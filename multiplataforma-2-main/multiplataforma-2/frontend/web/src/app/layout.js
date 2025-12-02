// AQUI ESTÁ A CORREÇÃO: Importar o CSS global para que funcione em todo o site
import '../global.css'; 
import { Inter } from 'next/font/google';

const inter = Inter({ subsets: ['latin'] });

export const metadata = {
  title: 'Gestão de Abrigo',
  description: 'Sistema integrado Web e Mobile',
};

export default function RootLayout({ children }) {
  return (
    <html lang="pt-br">
      <body className={inter.className}>{children}</body>
    </html>
  );
}