<script lang="ts">
  import { createEventDispatcher, onMount } from 'svelte';
  import type { TaskRequest, TaskResponse, UserBrief, TemplateFormat, TemplateSource, TaskStatus } from '$lib/types/api';
  import { listUsers } from '$lib/api/user';
  import { uploadFile } from '$lib/api/file';
  import { createTask, editTask } from '$lib/api/task';
  import { authStore } from '$lib/stores/auth';
  import { toast } from '$lib/stores/toast';
  import { STATUS_OPTIONS } from '$lib/utils/status';
  import { dateOnly, toDateString } from '$lib/utils/date';

  export let open = false;
  /** When provided, dialog is in edit mode. */
  export let task: TaskResponse | null = null;

  const dispatch = createEventDispatcher<{ close: void; saved: void }>();

  let users: UserBrief[] = [];
  let loadingUsers = false;

  let name = '';
  let description = '';
  let endDate = '';
  let templateSource: TemplateSource = 'BLANK';
  let templateFormat: TemplateFormat = 'docx';
  let templateFileId = '';
  let templateName = '';
  let uploadFileObj: File | null = null;
  let assigneeIds: number[] = [];
  let status: TaskStatus = 0;
  let submitting = false;

  $: isEdit = !!task;

  function reset() {
    name = '';
    description = '';
    endDate = toDateString(new Date(Date.now() + 7 * 86400000));
    templateSource = 'BLANK';
    templateFormat = 'docx';
    templateFileId = '';
    templateName = '';
    uploadFileObj = null;
    assigneeIds = [];
    status = 0;
  }

  function loadFromTask(t: TaskResponse) {
    name = t.name ?? '';
    description = t.description ?? '';
    endDate = dateOnly(t.endDate);
    templateSource = (t.templateSource as TemplateSource) ?? 'SELECT';
    templateFormat = (t.templateFormat as TemplateFormat) ?? 'docx';
    templateFileId = t.template ?? '';
    templateName = t.templateName ?? '';
    uploadFileObj = null;
    assigneeIds = (t.assignee ?? '')
      .split(',')
      .map((s) => Number(s.trim()))
      .filter((n) => !isNaN(n) && n > 0);
    status = t.status;
  }

  $: if (open) {
    if (task) loadFromTask(task);
    else reset();
    void ensureUsers();
  }

  async function ensureUsers() {
    if (users.length || loadingUsers) return;
    loadingUsers = true;
    try {
      const role = $authStore?.role ?? 'USER';
      users = await listUsers(role === 'ADMIN');
    } catch {
      /* error toasted by client */
    } finally {
      loadingUsers = false;
    }
  }

  function toggleAssignee(id: number) {
    assigneeIds = assigneeIds.includes(id)
      ? assigneeIds.filter((x) => x !== id)
      : [...assigneeIds, id];
  }

  function onFileChange(e: Event) {
    const input = e.target as HTMLInputElement;
    uploadFileObj = input.files?.[0] ?? null;
  }

  async function submit() {
    if (!name.trim()) return toast.error('请填写任务名称');
    if (!endDate) return toast.error('请选择截止日期');
    if (assigneeIds.length === 0) return toast.error('请至少指派一个用户');

    const me = $authStore?.userId;
    if (!me) return toast.error('当前用户信息缺失，请重新登录');

    let template = templateFileId;
    let tplName = templateName;
    let tplFormat: TemplateFormat | undefined = templateFormat;

    submitting = true;
    try {
      if (templateSource === 'UPLOAD') {
        if (!uploadFileObj && !isEdit) return toast.error('请选择要上传的文件');
        if (uploadFileObj) {
          template = await uploadFile(uploadFileObj);
          tplName = uploadFileObj.name;
          const ext = uploadFileObj.name.split('.').pop()?.toLowerCase();
          if (ext === 'docx' || ext === 'xlsx' || ext === 'pptx') tplFormat = ext;
        }
      } else if (templateSource === 'SELECT') {
        if (!template.trim()) return toast.error('请填写已有文件的 fileId');
      } else {
        // BLANK: backend will create file based on templateFormat
        template = '';
        tplName = name;
      }

      const payload: TaskRequest = {
        name: name.trim(),
        description: description.trim() || undefined,
        endDate,
        owner: isEdit && task ? task.owner : me,
        assignee: assigneeIds.join(','),
        templateSource,
        templateFormat: tplFormat,
        template: template || undefined,
        templateName: tplName || undefined,
        status: isEdit ? status : 0
      };

      if (isEdit && task) {
        payload.id = task.id;
        await editTask(payload);
        toast.success('任务已更新');
      } else {
        await createTask(payload);
        toast.success('任务已创建');
      }
      dispatch('saved');
      dispatch('close');
    } catch {
      /* error toasted in client */
    } finally {
      submitting = false;
    }
  }

  function handleBackdrop(e: MouseEvent) {
    if (e.target === e.currentTarget) dispatch('close');
  }

  const SOURCE_OPTIONS: { v: TemplateSource; t: string }[] = [
    { v: 'BLANK', t: '空白模板' },
    { v: 'UPLOAD', t: '上传文件' },
    { v: 'SELECT', t: '已有 fileId' }
  ];
</script>

{#if open}
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-ink-950/70 px-4 py-8 backdrop-blur-sm animate-fade-in"
    on:click={handleBackdrop}
    role="presentation"
  >
    <div class="glass-strong relative max-h-[90vh] w-full max-w-2xl overflow-y-auto p-6 animate-slide-up">
      <header class="mb-4 flex items-start justify-between gap-4">
        <div>
          <h2 class="text-lg font-semibold text-ink-100">{isEdit ? '编辑任务' : '新建任务'}</h2>
          <p class="text-xs text-ink-400">填写任务信息并选择文档模板来源</p>
        </div>
        <button class="btn-ghost px-2 py-1 text-xs" on:click={() => dispatch('close')}>关闭</button>
      </header>

      <div class="grid grid-cols-1 gap-4 md:grid-cols-2">
        <div class="md:col-span-2">
          <label class="label" for="t-name">任务名称</label>
          <input id="t-name" class="input" bind:value={name} placeholder="例如：Q2 季度汇报" />
        </div>

        <div class="md:col-span-2">
          <label class="label" for="t-desc">描述（可选）</label>
          <textarea id="t-desc" class="input min-h-[72px] resize-y" bind:value={description} placeholder="任务说明..."></textarea>
        </div>

        <div>
          <label class="label" for="t-end">截止日期</label>
          <input id="t-end" type="date" class="input" bind:value={endDate} />
        </div>

        {#if isEdit}
          <div>
            <label class="label" for="t-status">状态</label>
            <select id="t-status" class="input" bind:value={status}>
              {#each STATUS_OPTIONS as opt}
                <option value={opt.value}>{opt.label}</option>
              {/each}
            </select>
          </div>
        {/if}

        <div class="md:col-span-2">
          <label class="label">模板来源</label>
          <div class="grid grid-cols-3 gap-2">
            {#each SOURCE_OPTIONS as opt}
              <button
                type="button"
                class="rounded-lg border px-3 py-2 text-sm transition"
                class:border-accent-cyan={templateSource === opt.v}
                class:bg-accent-cyan={templateSource === opt.v}
                class:text-ink-950={templateSource === opt.v}
                class:border-white={templateSource !== opt.v}
                class:border-opacity-10={templateSource !== opt.v}
                class:bg-ink-900={templateSource !== opt.v}
                class:text-ink-200={templateSource !== opt.v}
                on:click={() => (templateSource = opt.v)}
              >{opt.t}</button>
            {/each}
          </div>
        </div>

        {#if templateSource === 'BLANK'}
          <div>
            <label class="label" for="t-fmt">文档格式</label>
            <select id="t-fmt" class="input" bind:value={templateFormat}>
              <option value="docx">Word (.docx)</option>
              <option value="xlsx">Excel (.xlsx)</option>
              <option value="pptx">PowerPoint (.pptx)</option>
            </select>
          </div>
        {:else if templateSource === 'UPLOAD'}
          <div class="md:col-span-2">
            <label class="label" for="t-file">上传文件</label>
            <input id="t-file" type="file" class="input" accept=".docx,.xlsx,.pptx" on:change={onFileChange} />
            {#if uploadFileObj}
              <p class="mt-1 text-xs text-ink-300">已选择：{uploadFileObj.name}</p>
            {/if}
          </div>
        {:else}
          <div>
            <label class="label" for="t-fid">fileId</label>
            <input id="t-fid" class="input" bind:value={templateFileId} placeholder="后端文件 ID" />
          </div>
          <div>
            <label class="label" for="t-tname">展示名</label>
            <input id="t-tname" class="input" bind:value={templateName} placeholder="文件展示名称" />
          </div>
        {/if}

        <div class="md:col-span-2">
          <label class="label">指派给</label>
          {#if loadingUsers}
            <p class="text-xs text-ink-400">加载用户中...</p>
          {:else if users.length === 0}
            <p class="text-xs text-ink-400">暂无用户可选</p>
          {:else}
            <div class="flex flex-wrap gap-2">
              {#each users as u}
                <button
                  type="button"
                  class="rounded-full border px-3 py-1 text-xs transition"
                  class:border-accent-cyan={assigneeIds.includes(u.id)}
                  class:text-accent-cyan={assigneeIds.includes(u.id)}
                  class:bg-cyan-400={assigneeIds.includes(u.id)}
                  class:bg-opacity-10={assigneeIds.includes(u.id)}
                  class:border-white={!assigneeIds.includes(u.id)}
                  class:border-opacity-10={!assigneeIds.includes(u.id)}
                  class:text-ink-200={!assigneeIds.includes(u.id)}
                  on:click={() => toggleAssignee(u.id)}
                >{u.username}</button>
              {/each}
            </div>
          {/if}
        </div>
      </div>

      <div class="divider my-5"></div>
      <div class="flex justify-end gap-2">
        <button class="btn-secondary" on:click={() => dispatch('close')} disabled={submitting}>取消</button>
        <button class="btn-primary" on:click={submit} disabled={submitting}>
          {submitting ? '提交中...' : isEdit ? '保存' : '创建任务'}
        </button>
      </div>
    </div>
  </div>
{/if}
