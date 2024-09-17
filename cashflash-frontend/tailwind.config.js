import daisyui from 'daisyui';
import typography from 'tailwindcss'

export default {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [typography, daisyui],
  daisyui: {
    themes: ["light", "dark"],
  },
}
