import { api } from './client';
import type { IPage, TaskRequest, TaskResponse, TaskSearchCriteria } from '$lib/types/api';

export function queryTask(criteria: TaskSearchCriteria): Promise<IPage<TaskResponse>> {
  return api.get<IPage<TaskResponse>>('/api/task/query', { ...criteria });
}

export function createTask(req: TaskRequest): Promise<null> {
  return api.post<null>('/api/task/create', req);
}

export function editTask(req: TaskRequest): Promise<null> {
  return api.post<null>('/api/task/edit', req);
}
