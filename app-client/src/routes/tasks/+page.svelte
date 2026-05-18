<script lang="ts">
  import { onMount } from 'svelte';
  import AppShell from '$lib/components/AppShell.svelte';
  import TaskCard from '$lib/components/TaskCard.svelte';
  import TaskFormDialog from '$lib/components/TaskFormDialog.svelte';
  import EmptyState from '$lib/components/EmptyState.svelte';
  import Pagination from '$lib/components/Pagination.svelte';
  import type { IPage, TaskResponse, TaskSearchCriteria, TaskStatus } from '$lib/types/api';
  import { queryTask } from '$lib/api/task';
  import { buildEditorUrl, buildDownloadUrl } from '$lib/api/file';
  import { authStore } from '$lib/stores/auth';
  import { STATUS_OPTIONS } from '$lib/utils/status';

  let page: IPage<TaskResponse> | null = null;
  let loading = false;

  // filters
  let nameQ = '';
  let endDateQ = '';
  let statusQ: TaskStatus | '' = '';
  let scope: 'ALL' | 'OWNER' | 'ASSIGNEE' = 'ALL';

  // pagination
  let current = 1;
  const PAGE_SIZE = 6;

  // dialog
  let dialogOpen = false;
  let dialogTask: TaskResponse | null = null;

  function buildCriteria(): TaskSearchCriteria {
    const c: TaskSearchCriteria = { page: current, pageSize: PAGE_SIZE };
    if (nameQ.trim()) c.name = nameQ.trim();
    if (endDateQ) c.endDate = endDateQ;
    if (statusQ !== '') c.status = statusQ as TaskStatus;
    if (scope === 'OWNER') c.isOwner = true;
    else if (scope === 'ASSIGNEE') c.isAssignee = true;
    return c;
  }

  async function refresh() {
    loading = true;
    try {
      page = await queryTask(buildCriteria());
    } catch {
      /* toasted */
    } finally {
      loading = false;
    }
  }

  function applyFilters() {
    current = 1;
    void refresh();
  }

  function resetFilters() {
    nameQ = '';
    endDateQ = '';
    statusQ = '';
    scope = 'ALL';
    current = 1;
    void refresh();
  }

  function onPageChange(e: CustomEvent<{ page: number }>) {
    current = e.detail.page;
    void refresh();
  }

  function openCreate() {
    dialogTask = null;
    dialogOpen = true;
  }

  function openEdit(t: TaskResponse) {
    dialogTask = t;
    dialogOpen = true;
  }

  function openEditor(t: TaskResponse) {
    if (!t.template) return;
    window.open(buildEditorUrl(t.template), '_blank', 'noopener');
  }

  function downloadFile(t: TaskResponse) {
    if (!t.template) return;
    window.open(buildDownloadUrl(t.template), '_blank', 'noopener');
  }

  function canEditTask(t: TaskResponse): boolean {
    const me = $authStore?.userId;
    if (!me) return false;
    if (t.owner === me) return true;
    return t.assignee
      .split(',')
      .map((s) => Number(s.trim()))
      .includes(me);
  }

  onMount(refresh);
</script>

<AppShell>
  <div class="flex flex-wrap items-end justify-between gap-3">
    <div>
      <h1 class="text-2xl font-semibold tracking-tight text-ink-100">任务</h1>
      <p class="text-sm text-ink-400">管理你创建的与指派给你的文档协作任务</p>
    </div>
    <div class="flex items-center gap-2">
      <button class="btn-secondary" on:click={refresh} disabled={loading}>{loading ? '刷新中...' : '刷新'}</button>
      <button class="btn-primary" on:click={openCreate}>+ 新建任务</button>
    </div>
  </div>

  <section class="glass mt-6 p-4">
    <div class="grid grid-cols-1 gap-3 md:grid-cols-5">
      <div class="md:col-span-2">
        <label class="label" for="f-name">名称</label>
        <input id="f-name" class="input" bind:value={nameQ} placeholder="模糊搜索..." />
      </div>
      <div>
        <label class="label" for="f-end">截止日期 ≤</label>
        <input id="f-end" type="date" class="input" bind:value={endDateQ} />
      </div>
      <div>
        <label class="label" for="f-status">状态</label>
        <select id="f-status" class="input" bind:value={statusQ}>
          <option value="">全部</option>
          {#each STATUS_OPTIONS as opt}
            <option value={opt.value}>{opt.label}</option>
          {/each}
        </select>
      </div>
      <div>
        <label class="label" for="f-scope">范围</label>
        <select id="f-scope" class="input" bind:value={scope}>
          <option value="ALL">全部相关</option>
          <option value="OWNER">我创建的</option>
          <option value="ASSIGNEE">指派给我</option>
        </select>
      </div>
    </div>
    <div class="mt-3 flex justify-end gap-2">
      <button class="btn-ghost text-xs" on:click={resetFilters}>重置</button>
      <button class="btn-primary text-xs" on:click={applyFilters}>应用筛选</button>
    </div>
  </section>

  <section class="mt-6">
    {#if loading && !page}
      <div class="glass p-8 text-center text-sm text-ink-400">加载中...</div>
    {:else if page && page.records.length > 0}
      <div class="grid grid-cols-1 gap-4 lg:grid-cols-2">
        {#each page.records as t (t.id)}
          <TaskCard
            task={t}
            canEdit={canEditTask(t)}
            on:open={(e) => openEditor(e.detail.task)}
            on:download={(e) => downloadFile(e.detail.task)}
            on:edit={(e) => openEdit(e.detail.task)}
          />
        {/each}
      </div>
      <div class="mt-6">
        <Pagination
          current={page.current}
          pages={page.pages}
          total={page.total}
          pageSize={page.size}
          on:change={onPageChange}
        />
      </div>
    {:else}
      <EmptyState title="暂无任务" description="点击右上角按钮创建第一个任务">
        <button class="btn-primary mt-3" on:click={openCreate}>+ 新建任务</button>
      </EmptyState>
    {/if}
  </section>
</AppShell>

<TaskFormDialog
  open={dialogOpen}
  task={dialogTask}
  on:close={() => (dialogOpen = false)}
  on:saved={refresh}
/>
