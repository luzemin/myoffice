/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    extend: {
      colors: {
        ink: {
          950: '#05070d',
          900: '#0a0e1a',
          850: '#0f1424',
          800: '#141a2e',
          700: '#1f2740',
          600: '#2b3450',
          500: '#3b4566',
          400: '#5a6488',
          300: '#8892b0',
          200: '#b0bbd6',
          100: '#dbe2f5'
        },
        accent: {
          cyan: '#22d3ee',
          teal: '#14b8a6',
          blue: '#3b82f6',
          violet: '#8b5cf6',
          rose: '#f43f5e',
          amber: '#f59e0b',
          emerald: '#10b981'
        }
      },
      fontFamily: {
        sans: [
          'Inter',
          '"Noto Sans SC"',
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          'sans-serif'
        ],
        mono: ['"JetBrains Mono"', 'Menlo', 'Consolas', 'monospace']
      },
      boxShadow: {
        glow: '0 0 0 1px rgba(34,211,238,.25), 0 8px 32px -8px rgba(34,211,238,.35)',
        card: '0 1px 0 rgba(255,255,255,.04) inset, 0 8px 30px -12px rgba(0,0,0,.6)'
      },
      backgroundImage: {
        'mesh-cyan':
          'radial-gradient(ellipse at top left, rgba(34,211,238,.18), transparent 50%), radial-gradient(ellipse at bottom right, rgba(139,92,246,.18), transparent 50%)'
      },
      animation: {
        'fade-in': 'fadeIn .25s ease-out both',
        'slide-up': 'slideUp .3s ease-out both'
      },
      keyframes: {
        fadeIn: { '0%': { opacity: 0 }, '100%': { opacity: 1 } },
        slideUp: {
          '0%': { transform: 'translateY(8px)', opacity: 0 },
          '100%': { transform: 'translateY(0)', opacity: 1 }
        }
      }
    }
  },
  plugins: []
};
