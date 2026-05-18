<script lang="ts">
  import '../app.css';
  import { onMount } from 'svelte';
  import { page } from '$app/stores';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';
  import Toast from '$lib/components/Toast.svelte';

  // Routes that do not require authentication.
  const PUBLIC_PATHS = ['/login', '/admin/login'];

  function isPublic(pathname: string): boolean {
    return PUBLIC_PATHS.some((p) => pathname === p || pathname.startsWith(`${p}/`));
  }

  // Route guard: redirect unauthenticated users to /login,
  // redirect non-admin users away from /admin/**, and block admin from /tasks.
  $: {
    const path = $page.url.pathname;
    const auth = $authStore;
    if (typeof window !== 'undefined') {
      if (!auth && !isPublic(path) && path !== '/') {
        goto('/login');
      } else if (auth && path.startsWith('/admin') && path !== '/admin/login' && auth.role !== 'ADMIN') {
        goto('/tasks');
      }
    }
  }
</script>

<Toast />
<slot />
