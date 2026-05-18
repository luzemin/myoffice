<script lang="ts">
  import { goto } from '$app/navigation';
  import LoginCard from '$lib/components/LoginCard.svelte';
  import { authStore } from '$lib/stores/auth';
  import { toast } from '$lib/stores/toast';
  import { userLogin } from '$lib/api/auth';

  let loading = false;

  async function onSubmit(e: CustomEvent<{ username: string; password: string }>) {
    loading = true;
    try {
      const res = await userLogin(e.detail.username, e.detail.password);
      authStore.set({
        token: res.token,
        role: 'USER',
        userId: res.id ?? null,
        username: res.username
      });
      toast.success(`欢迎回来，${res.username}`);
      goto('/tasks');
    } catch {
      /* error toasted in client */
    } finally {
      loading = false;
    }
  }
</script>

<div class="relative flex min-h-screen items-center justify-center px-4">
  <div class="absolute inset-x-0 top-6 mx-auto max-w-5xl px-4 text-center">
    <p class="text-[11px] uppercase tracking-[0.4em] text-ink-400">MyOffice · 协同文档工作台</p>
  </div>
  <LoginCard
    title="用户登录"
    subtitle="使用账号密码进入工作台"
    submitLabel="登录"
    {loading}
    on:submit={onSubmit}
  >
    <span slot="footer">
      管理员？<a class="text-accent-cyan hover:underline" href="/admin/login">进入管理员入口 →</a>
    </span>
  </LoginCard>
</div>
