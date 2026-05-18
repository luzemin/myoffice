import { get } from 'svelte/store';
import { api } from './client';
import { authStore } from '$lib/stores/auth';

export function uploadFile(file: File): Promise<string> {
  const fd = new FormData();
  fd.append('file', file);
  return api.post<string>('/api/file/upload', fd);
}

/**
 * Build URL to the backend OnlyOffice editor page.
 * Backend /editor is a Thymeleaf-rendered page, JwtAuthenticationFilter accepts
 * `?token=` query param so we can open it directly in a new tab.
 */
export function buildEditorUrl(fileId: string): string {
  const auth = get(authStore);
  const token = auth?.token ?? '';
  const base = `/editor?fileId=${encodeURIComponent(fileId)}`;
  return token ? `${base}&token=${encodeURIComponent(token)}` : base;
}

export function buildDownloadUrl(fileId: string): string {
  const auth = get(authStore);
  const token = auth?.token ?? '';
  const base = `/api/file/download?fileId=${encodeURIComponent(fileId)}`;
  return token ? `${base}&token=${encodeURIComponent(token)}` : base;
}
