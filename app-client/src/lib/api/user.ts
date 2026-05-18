import { api } from './client';
import type { UserBrief } from '$lib/types/api';

/**
 * Lists all users. Admin uses /api/admin/user/all, regular users use /api/user/all.
 * Backend distinguishes role by URL path, so we route accordingly.
 */
export function listUsers(asAdmin: boolean): Promise<UserBrief[]> {
  return api.get<UserBrief[]>(asAdmin ? '/api/admin/user/all' : '/api/user/all');
}

export function createUser(username: string, password?: string): Promise<null> {
  const body: Record<string, string> = { username };
  if (password && password.length > 0) body.password = password;
  return api.post<null>('/api/admin/user/create', body);
}
