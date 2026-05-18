import type { TaskStatus } from '$lib/types/api';

export const STATUS_OPTIONS: { value: TaskStatus; label: string }[] = [
  { value: 0, label: '未开始' },
  { value: 1, label: '进行中' },
  { value: 2, label: '已完成' },
  { value: 3, label: '已关闭' }
];

export function statusLabel(status: TaskStatus): string {
  return STATUS_OPTIONS.find((o) => o.value === status)?.label ?? '未知';
}

/** Tailwind classes for the status badge. */
export function statusTone(status: TaskStatus): string {
  switch (status) {
    case 0:
      return 'border-amber-400/30 bg-amber-400/10 text-amber-300';
    case 1:
      return 'border-cyan-400/30 bg-cyan-400/10 text-cyan-300';
    case 2:
      return 'border-emerald-400/30 bg-emerald-400/10 text-emerald-300';
    case 3:
      return 'border-white/10 bg-white/5 text-ink-300';
    default:
      return 'border-white/10 bg-white/5 text-ink-300';
  }
}
