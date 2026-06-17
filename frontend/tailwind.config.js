/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#F59E0B',
        accent: '#EAB308',
        surface: '#F8FAFC',
        'surface-card': '#FFFFFF',
        'surface-border': '#E5E7EB',
      }
    },
  },
  plugins: [],
}
