import { get } from 'svelte/store';
import { goto } from '$app/navigation';
import { browser } from '$app/environment';
import { authStore, logout } from '$lib/stores/auth';
import { toast } from '$lib/stores/toast';
import type { R } from '$lib/types/api';

export class ApiError extends Error {
  status: number;
  code: number;
  constructor(message: string, status = 0, code = 0) {
    super(message);
    this.status = status;
    this.code = code;
  }
}

interface RequestOptions {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  body?: unknown;
  query?: Record<string, string | number | boolean | undefined | null>;
  silent?: boolean;
}

function buildUrl(path: string, query?: RequestOptions['query']): string {
  if (!query) return path;
  const usp = new URLSearchParams();
  for (const [k, v] of Object.entries(query)) {
    if (v === undefined || v === null || v === '') continue;
    usp.set(k, String(v));
  }
  const qs = usp.toString();
  return qs ? `${path}?${qs}` : path;
}

export async function request<T>(path: string, opts: RequestOptions = {}): Promise<T> {
  const { method = 'GET', body, query, silent = false } = opts;
  const auth = get(authStore);
  const headers = new Headers();
  if (auth?.token) headers.set('Authorization', `Bearer ${auth.token}`);

  let bodyInit: BodyInit | undefined;
  if (body instanceof FormData) {
    bodyInit = body;
  } else if (body !== undefined) {
    headers.set('Content-Type', 'application/json');
    bodyInit = JSON.stringify(body);
  }

  let res: Response;
  try {
    res = await fetch(buildUrl(path, query), { method, headers, body: bodyInit });
  } catch (e) {
    if (!silent) toast.error('网络异常，请稍后重试');
    throw new ApiError('network error');
  }

  if (res.status === 401 || res.status === 403) {
    logout();
    if (!silent) toast.error('登录状态已失效，请重新登录');
    if (browser) goto('/login');
    throw new ApiError('unauthorized', res.status);
  }

  let payload: R<T> | null = null;
  try {
    payload = (await res.json()) as R<T>;
  } catch {
    if (!silent) toast.error(`服务异常 (${res.status})`);
    throw new ApiError('invalid response', res.status);
  }

  if (!payload || payload.success === false) {
    const msg = payload?.message || `请求失败 (${res.status})`;
    if (!silent) toast.error(msg);
    throw new ApiError(msg, res.status, payload?.code ?? 0);
  }

  return payload.data;
}

export const api = {
  get: <T>(path: string, query?: RequestOptions['query'], opts: Partial<RequestOptions> = {}) =>
    request<T>(path, { ...opts, method: 'GET', query }),
  post: <T>(path: string, body?: unknown, opts: Partial<RequestOptions> = {}) =>
    request<T>(path, { ...opts, method: 'POST', body })
};
