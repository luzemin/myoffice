<script lang="ts">
  import { createEventDispatcher } from 'svelte';
  import StatusBadge from './StatusBadge.svelte';
  import type { TaskResponse } from '$lib/types/api';
  import { dateOnly } from '$lib/utils/date';

  export let task: TaskResponse;
  export let canEdit = false;

  const dispatch = createEventDispatcher<{
    open: { task: TaskResponse };
    download: { task: TaskResponse };
    edit: { task: TaskResponse };
  }>();

  $: hasFile = !!task.template;
  $: formatLabel = (task.templateFormat ?? '').toUpperCase();
  $: sourceLabel =
    task.templateSource === 'BLANK'
      ? '空白模板'
      : task.templateSource === 'UPLOAD'
        ? '上传模板'
        : task.templateSource === 'SELECT'
          ? '已有文件'
          : '未指定';
</script>

<article class="glass group relative flex flex-col gap-4 p-5 transition hover:border-white/15 hover:shadow-glow">
  <div class="flex items-start justify-between gap-3">
    <div class="min-w-0 flex-1">
      <div class="mb-1 flex flex-wrap items-center gap-2">
        <StatusBadge status={task.status} />
        {#if formatLabel}
          <span class="chip">{formatLabel}</span>
        {/if}
        <span class="chip">{sourceLabel}</span>
      </div>
      <h3 class="truncate text-base font-semibold text-ink-100">{task.name}</h3>
      {#if task.description}
        <p class="mt-1 line-clamp-2 text-sm text-ink-300">{task.description}</p>
      {/if}
    </div>
  </div>

  <div class="grid grid-cols-2 gap-3 text-xs sm:grid-cols-4">
    <div>
      <div class="text-ink-400">创建者</div>
      <div class="mt-0.5 truncate text-ink-100">{task.ownerName ?? '-'}</div>
    </div>
    <div>
      <div class="text-ink-400">指派给</div>
      <div class="mt-0.5 truncate text-ink-100" title={task.assigneeNames}>{task.assigneeNames ?? '-'}</div>
    </div>
    <div>
      <div class="text-ink-400">开始</div>
      <div class="mt-0.5 text-ink-100">{dateOnly(task.startDate)}</div>
    </div>
    <div>
      <div class="text-ink-400">截止</div>
      <div class="mt-0.5 text-ink-100">{dateOnly(task.endDate)}</div>
    </div>
  </div>

  <div class="flex flex-wrap items-center gap-2 pt-1">
    <button
      class="btn-primary px-3 py-1.5 text-xs"
      disabled={!hasFile}
      on:click={() => dispatch('open', { task })}
    >打开编辑器</button>
    <button
      class="btn-secondary px-3 py-1.5 text-xs"
      disabled={!hasFile}
      on:click={() => dispatch('download', { task })}
    >下载</button>
    {#if canEdit}
      <button class="btn-ghost px-3 py-1.5 text-xs" on:click={() => dispatch('edit', { task })}>编辑</button>
    {/if}
    <span class="ml-auto text-[11px] text-ink-400">#{task.id}</span>
  </div>
</article>
