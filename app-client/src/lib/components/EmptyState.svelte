<script lang="ts">
  export let title = '暂无数据';
  export let description = '';
</script>

<div class="glass flex flex-col items-center gap-2 px-6 py-14 text-center">
  <div
    class="flex h-12 w-12 items-center justify-center rounded-2xl text-ink-950 text-xl font-bold"
    style="background-image: linear-gradient(135deg,#22d3ee,#14b8a6 50%,#3b82f6);"
  >·</div>
  <h3 class="text-base font-semibold text-ink-100">{title}</h3>
  {#if description}<p class="max-w-md text-sm text-ink-400">{description}</p>{/if}
  <slot />
</div>
