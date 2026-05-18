<script lang="ts">
  import { page } from '$app/stores';
  import { goto } from '$app/navigation';
  import { authStore, logout } from '$lib/stores/auth';

  function doLogout() {
    logout();
    goto('/login');
  }

  $: pathname = $page.url.pathname;
  $: isAdmin = $authStore?.role === 'ADMIN';
</script>

<div class="min-h-screen flex">
  <aside class="hidden md:flex w-60 shrink-0 flex-col border-r border-white/5 bg-ink-900/60 backdrop-blur-xl">
    <div class="px-5 pt-6 pb-4">
      <a href="/" class="flex items-center gap-2 text-ink-100">
        <span
          class="inline-flex h-8 w-8 items-center justify-center rounded-lg text-ink-950 font-bold"
          style="background-image: linear-gradient(135deg,#22d3ee,#14b8a6 50%,#3b82f6);"
        >M</span>
        <span class="text-base font-semibold tracking-wide">MyOffice</span>
      </a>
      <p class="mt-1 text-[11px] uppercase tracking-[0.2em] text-ink-400">协同文档工作台</p>
    </div>

    <nav class="mt-2 flex flex-col gap-1 px-3">
      <a class="nav-link" href="/tasks" aria-current={pathname.startsWith('/tasks') ? 'page' : undefined}>
        <span class="i">·</span>
        <span>任务</span>
      </a>
      {#if isAdmin}
        <a class="nav-link" href="/admin/users" aria-current={pathname.startsWith('/admin/users') ? 'page' : undefined}>
          <span class="i">·</span>
          <span>用户管理</span>
        </a>
      {/if}
    </nav>

    <div class="mt-auto p-3">
      <div class="glass flex items-center justify-between gap-3 px-3 py-2">
        <div class="min-w-0">
          <div class="truncate text-sm text-ink-100">{$authStore?.username ?? '游客'}</div>
          <div class="text-[11px] text-ink-400">{isAdmin ? '管理员' : '普通用户'}</div>
        </div>
        <button class="btn-ghost px-2 py-1 text-xs" on:click={doLogout}>退出</button>
      </div>
    </div>
  </aside>

  <main class="flex-1 min-w-0">
    <header class="sticky top-0 z-30 flex items-center justify-between border-b border-white/5 bg-ink-950/60 px-6 py-3 backdrop-blur-xl md:hidden">
      <a href="/" class="flex items-center gap-2">
        <span
          class="inline-flex h-7 w-7 items-center justify-center rounded-md text-ink-950 font-bold text-sm"
          style="background-image: linear-gradient(135deg,#22d3ee,#14b8a6 50%,#3b82f6);"
        >M</span>
        <span class="text-sm font-semibold">MyOffice</span>
      </a>
      <button class="btn-ghost px-2 py-1 text-xs" on:click={doLogout}>退出</button>
    </header>
    <div class="px-6 py-6 md:px-10 md:py-8">
      <slot />
    </div>
  </main>
</div>
