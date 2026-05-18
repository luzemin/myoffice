<script lang="ts">
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';

  onMount(() => {
    const auth = $authStore;
    if (!auth) goto('/login');
    else if (auth.role === 'ADMIN') goto('/admin/users');
    else goto('/tasks');
  });
</script>

<div class="flex min-h-screen items-center justify-center text-sm text-ink-400">正在加载...</div>
