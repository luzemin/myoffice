<script lang="ts">
  import { onMount } from 'svelte';
  import AppShell from '$lib/components/AppShell.svelte';
  import EmptyState from '$lib/components/EmptyState.svelte';
  import type { UserBrief } from '$lib/types/api';
  import { listUsers, createUser } from '$lib/api/user';
  import { toast } from '$lib/stores/toast';

  let users: UserBrief[] = [];
  let loading = false;

  let dialogOpen = false;
  let newUsername = '';
  let newPassword = '';
  let submitting = false;

  async function refresh() {
    loading = true;
    try {
      users = await listUsers(true);
    } catch {
      /* toasted */
    } finally {
      loading = false;
    }
  }

  async function submitCreate() {
    if (!newUsername.trim()) return toast.error('请填写用户名');
    submitting = true;
    try {
      await createUser(newUsername.trim(), newPassword || undefined);
      toast.success('用户已创建');
      dialogOpen = false;
      newUsername = '';
      newPassword = '';
      await refresh();
    } catch {
      /* toasted */
    } finally {
      submitting = false;
    }
  }

  onMount(refresh);
</script>

<AppShell>
  <div class="flex flex-wrap items-end justify-between gap-3">
    <div>
      <h1 class="text-2xl font-semibold tracking-tight text-ink-100">用户管理</h1>
      <p class="text-sm text-ink-400">管理可登录工作台的普通用户</p>
    </div>
    <div class="flex items-center gap-2">
      <button class="btn-secondary" on:click={refresh} disabled={loading}>{loading ? '刷新中...' : '刷新'}</button>
      <button class="btn-primary" on:click={() => (dialogOpen = true)}>+ 新建用户</button>
    </div>
  </div>

  <section class="mt-6">
    {#if loading && users.length === 0}
      <div class="glass p-8 text-center text-sm text-ink-400">加载中...</div>
    {:else if users.length === 0}
      <EmptyState title="暂无用户" description="点击右上角按钮创建第一个用户" />
    {:else}
      <div class="glass overflow-hidden">
        <table class="w-full text-sm">
          <thead class="border-b border-white/5 bg-white/[0.02] text-left text-xs uppercase tracking-wider text-ink-400">
            <tr>
              <th class="px-5 py-3 font-medium">ID</th>
              <th class="px-5 py-3 font-medium">用户名</th>
            </tr>
          </thead>
          <tbody>
            {#each users as u (u.id)}
              <tr class="border-b border-white/5 last:border-0 hover:bg-white/[0.02]">
                <td class="px-5 py-3 text-ink-300">#{u.id}</td>
                <td class="px-5 py-3 text-ink-100">{u.username}</td>
              </tr>
            {/each}
          </tbody>
        </table>
      </div>
    {/if}
  </section>
</AppShell>

{#if dialogOpen}
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-ink-950/70 px-4 py-8 backdrop-blur-sm animate-fade-in"
    on:click={(e) => { if (e.target === e.currentTarget) dialogOpen = false; }}
    role="presentation"
  >
    <div class="glass-strong w-full max-w-md p-6 animate-slide-up">
      <h2 class="text-base font-semibold text-ink-100">新建用户</h2>
      <p class="text-xs text-ink-400">留空密码将使用默认值 <code class="text-ink-200">changeit</code></p>

      <div class="mt-4 flex flex-col gap-3">
        <div>
          <label class="label" for="nu-name">用户名</label>
          <input id="nu-name" class="input" bind:value={newUsername} placeholder="例如：alice" />
        </div>
        <div>
          <label class="label" for="nu-pwd">密码（可选）</label>
          <input id="nu-pwd" type="password" class="input" bind:value={newPassword} placeholder="留空则使用默认密码" />
        </div>
      </div>

      <div class="divider my-5"></div>
      <div class="flex justify-end gap-2">
        <button class="btn-secondary" on:click={() => (dialogOpen = false)} disabled={submitting}>取消</button>
        <button class="btn-primary" on:click={submitCreate} disabled={submitting}>
          {submitting ? '创建中...' : '创建'}
        </button>
      </div>
    </div>
  </div>
{/if}
