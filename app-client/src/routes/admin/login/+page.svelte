<script lang="ts">
  import { goto } from '$app/navigation';
  import LoginCard from '$lib/components/LoginCard.svelte';
  import { authStore } from '$lib/stores/auth';
  import { toast } from '$lib/stores/toast';
  import { adminLogin } from '$lib/api/auth';

  let loading = false;

  async function onSubmit(e: CustomEvent<{ username: string; password: string }>) {
    loading = true;
    try {
      const res = await adminLogin(e.detail.username, e.detail.password);
      authStore.set({
        token: res.token,
        role: 'ADMIN',
        userId: res.id ?? null,
        username: res.username
      });
      toast.success(`已登录，${res.username}`);
      goto('/admin/users');
    } catch {
      /* error toasted in client */
    } finally {
      loading = false;
    }
  }
</script>

<div class="relative flex min-h-screen items-center justify-center px-4">
  <div class="absolute inset-x-0 top-6 mx-auto max-w-5xl px-4 text-center">
    <p class="text-[11px] uppercase tracking-[0.4em] text-ink-400">MyOffice · 控制台</p>
  </div>
  <LoginCard
    title="管理员登录"
    subtitle="管理用户与系统配置"
    submitLabel="进入控制台"
    {loading}
    on:submit={onSubmit}
  >
    <span slot="footer">
      普通用户？<a class="text-accent-cyan hover:underline" href="/login">返回用户登录 →</a>
    </span>
  </LoginCard>
</div>
