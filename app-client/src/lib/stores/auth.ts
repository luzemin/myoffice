import { writable, type Writable } from 'svelte/store';
import { browser } from '$app/environment';

export type Role = 'USER' | 'ADMIN';

export interface AuthState {
  token: string;
  role: Role;
  userId: number | null;
  username: string;
}

const STORAGE_KEY = 'myoffice.auth';

function readInitial(): AuthState | null {
  if (!browser) return null;
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    return JSON.parse(raw) as AuthState;
  } catch {
    return null;
  }
}

export const authStore: Writable<AuthState | null> = writable<AuthState | null>(readInitial());

if (browser) {
  authStore.subscribe((value) => {
    try {
      if (value) localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
      else localStorage.removeItem(STORAGE_KEY);
    } catch {
      /* ignore storage errors */
    }
  });
}

export function logout(): void {
  authStore.set(null);
}
