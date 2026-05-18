import { api } from './client';
import type { AdminResponse, UserResponse } from '$lib/types/api';

export function adminLogin(username: string, password: string): Promise<AdminResponse> {
  return api.post<AdminResponse>('/api/auth/admin/login', { username, password });
}

export function userLogin(username: string, password: string): Promise<UserResponse> {
  return api.post<UserResponse>('/api/auth/user/login', { username, password });
}
