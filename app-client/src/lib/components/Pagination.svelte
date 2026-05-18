<script lang="ts">
  import { createEventDispatcher } from 'svelte';

  export let current = 1;
  export let pages = 1;
  export let total = 0;
  export let pageSize = 5;

  const dispatch = createEventDispatcher<{ change: { page: number } }>();

  function go(p: number) {
    if (p < 1 || p > pages || p === current) return;
    dispatch('change', { page: p });
  }

  $: from = total === 0 ? 0 : (current - 1) * pageSize + 1;
  $: to = Math.min(current * pageSize, total);
</script>

<div class="flex flex-col items-center justify-between gap-3 sm:flex-row">
  <div class="text-xs text-ink-400">
    第 <span class="text-ink-100">{from}</span>–<span class="text-ink-100">{to}</span> 条 · 共
    <span class="text-ink-100">{total}</span> 条
  </div>
  <div class="flex items-center gap-1">
    <button class="btn-ghost px-3 py-1 text-xs" disabled={current <= 1} on:click={() => go(current - 1)}>上一页</button>
    <span class="px-3 text-xs text-ink-300">
      <span class="text-ink-100">{current}</span> / {pages || 1}
    </span>
    <button class="btn-ghost px-3 py-1 text-xs" disabled={current >= pages} on:click={() => go(current + 1)}>下一页</button>
  </div>
</div>
