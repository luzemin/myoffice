<script lang="ts">
  import { toasts } from '$lib/stores/toast';
</script>

<div class="pointer-events-none fixed inset-x-0 top-4 z-[60] flex flex-col items-center gap-2 px-4">
  {#each $toasts as t (t.id)}
    <div
      class="glass pointer-events-auto flex min-w-[260px] max-w-md items-start gap-3 px-4 py-2.5 text-sm animate-slide-up"
      class:border-emerald-400={t.kind === 'success'}
      class:border-rose-400={t.kind === 'error'}
      class:border-amber-400={t.kind === 'warning'}
      class:border-cyan-400={t.kind === 'info'}
    >
      <span
        class="mt-1 inline-block h-2 w-2 shrink-0 rounded-full"
        class:bg-emerald-400={t.kind === 'success'}
        class:bg-rose-400={t.kind === 'error'}
        class:bg-amber-400={t.kind === 'warning'}
        class:bg-cyan-400={t.kind === 'info'}
      ></span>
      <span class="text-ink-100">{t.message}</span>
    </div>
  {/each}
</div>
