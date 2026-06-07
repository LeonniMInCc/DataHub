/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#3B82F6',
        accent: '#10B981',
        dark: '#0F172A',
        'dark-card': 'rgba(30, 41, 59, 0.7)',
        'dark-border': 'rgba(71, 85, 105, 0.5)',
      }
    },
  },
  plugins: [],
}
