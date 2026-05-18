import { writable, type Writable } from 'svelte/store';

export type ToastKind = 'success' | 'error' | 'info' | 'warning';

export interface ToastItem {
  id: number;
  kind: ToastKind;
  message: string;
}

let seq = 1;
export const toasts: Writable<ToastItem[]> = writable<ToastItem[]>([]);

function push(kind: ToastKind, message: string, duration = 3200): void {
  const id = seq++;
  toasts.update((list) => [...list, { id, kind, message }]);
  if (typeof window !== 'undefined') {
    setTimeout(() => {
      toasts.update((list) => list.filter((t) => t.id !== id));
    }, duration);
  }
}

export const toast = {
  success: (msg: string) => push('success', msg),
  error: (msg: string) => push('error', msg, 4500),
  info: (msg: string) => push('info', msg),
  warning: (msg: string) => push('warning', msg, 4000)
};
