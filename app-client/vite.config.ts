import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

const BACKEND = 'http://localhost';

export default defineConfig({
  plugins: [sveltekit()],
  server: {
    port: 5173,
    strictPort: false,
    proxy: {
      '/api': { target: BACKEND, changeOrigin: true },
      '/editor': { target: BACKEND, changeOrigin: true },
      '/health': { target: BACKEND, changeOrigin: true }
    }
  }
});
