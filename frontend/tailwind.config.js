/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}'
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#ECFDF5',
          100: '#D1FAE5',
          200: '#A7F3D0',
          300: '#6EE7B7',
          400: '#34D399',
          500: '#10B981',
          600: '#059669',
          700: '#047857',
          800: '#065F46',
          900: '#064E3B'
        },
        secondary: {
          500: '#F59E0B'
        },
        ink: '#111827'
      },
      fontFamily: {
        sans: ['Noto Sans SC', 'sans-serif'],
        display: ['Noto Serif SC', 'serif']
      }
    }
  },
  plugins: []
}
