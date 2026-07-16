/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#EBF0F5',
          100: '#D1DDEA',
          200: '#A3BBD5',
          300: '#7599C0',
          400: '#4777AB',
          500: '#1E3A5F',
          600: '#18304F',
          700: '#12263F',
          800: '#0C1C2F',
          900: '#06121F',
        },
        orange: {
          50: '#FFF0EA',
          100: '#FFD6C4',
          200: '#FFB89E',
          300: '#FF9978',
          400: '#FF7B52',
          500: '#FF6B35',
          600: '#E55A2A',
          700: '#CC4A20',
          800: '#B23916',
          900: '#99290C',
        },
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
    },
  },
  plugins: [],
};