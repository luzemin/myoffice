<script lang="ts">
  import { createEventDispatcher } from 'svelte';

  export let title = '登录';
  export let subtitle = '';
  export let submitLabel = '登录';
  export let loading = false;
  export let username = '';
  export let password = '';

  const dispatch = createEventDispatcher<{ submit: { username: string; password: string } }>();

  function onSubmit() {
    if (!username.trim() || !password) return;
    dispatch('submit', { username: username.trim(), password });
  }
</script>

<div class="glass-strong relative w-full max-w-sm overflow-hidden p-7 animate-slide-up">
  <div
    class="pointer-events-none absolute -top-24 -right-24 h-56 w-56 rounded-full opacity-30 blur-3xl"
    style="background-image: radial-gradient(circle, #22d3ee 0%, transparent 60%);"
  ></div>
  <div
    class="pointer-events-none absolute -bottom-24 -left-24 h-56 w-56 rounded-full opacity-25 blur-3xl"
    style="background-image: radial-gradient(circle, #8b5cf6 0%, transparent 60%);"
  ></div>

  <div class="relative">
    <div class="mb-5 flex items-center gap-3">
      <span
        class="inline-flex h-10 w-10 items-center justify-center rounded-xl text-ink-950 font-bold"
        style="background-image: linear-gradient(135deg,#22d3ee,#14b8a6 50%,#3b82f6);"
      >M</span>
      <div>
        <h1 class="text-lg font-semibold leading-tight text-ink-100">{title}</h1>
        {#if subtitle}<p class="text-xs text-ink-400">{subtitle}</p>{/if}
      </div>
    </div>

    <form on:submit|preventDefault={onSubmit} class="flex flex-col gap-3">
      <div>
        <label class="label" for="login-user">用户名</label>
        <input id="login-user" class="input" autocomplete="username" bind:value={username} placeholder="请输入用户名" />
      </div>
      <div>
        <label class="label" for="login-pwd">密码</label>
        <input id="login-pwd" type="password" class="input" autocomplete="current-password" bind:value={password} placeholder="请输入密码" />
      </div>
      <button type="submit" class="btn-primary mt-2 w-full" disabled={loading}>
        {loading ? '登录中...' : submitLabel}
      </button>
    </form>

    <div class="mt-4 text-xs text-ink-400">
      <slot name="footer" />
    </div>
  </div>
</div>
