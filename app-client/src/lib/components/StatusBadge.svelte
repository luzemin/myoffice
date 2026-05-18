<script lang="ts">
  import type { TaskStatus } from '$lib/types/api';
  import { statusLabel, statusTone } from '$lib/utils/status';
  export let status: TaskStatus;
</script>

<span class="inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-[11px] font-medium {statusTone(status)}">
  <span class="h-1.5 w-1.5 rounded-full bg-current opacity-80"></span>
  {statusLabel(status)}
</span>
