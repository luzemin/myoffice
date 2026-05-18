/** Generic backend response wrapper, mirrors com.myoffice.app.common.R */
export interface R<T> {
  code: number;
  message: string;
  type: string;
  success: boolean;
  data: T;
}

export interface AdminResponse {
  id: number | null;
  username: string;
  token: string;
}

export interface UserResponse {
  id: number | null;
  username: string;
  token: string;
}

export type TaskStatus = 0 | 1 | 2 | 3;
export type TemplateSource = 'SELECT' | 'UPLOAD' | 'BLANK';
export type TemplateFormat = 'docx' | 'xlsx' | 'pptx';

export interface Task {
  id: number;
  name: string;
  description?: string | null;
  template?: string | null;
  templateName?: string | null;
  templateSource?: TemplateSource | null;
  templateFormat?: TemplateFormat | null;
  startDate: string;
  endDate: string;
  owner: number;
  assignee: string;
  status: TaskStatus;
}

export interface TaskResponse extends Task {
  ownerName: string;
  assigneeNames: string;
}

export interface TaskRequest {
  id?: number;
  name: string;
  description?: string;
  template?: string;
  templateName?: string;
  templateSource?: TemplateSource;
  templateFormat?: TemplateFormat;
  startDate?: string;
  endDate: string;
  owner: number;
  assignee: string;
  status?: TaskStatus;
}

export interface TaskSearchCriteria {
  name?: string;
  endDate?: string;
  isOwner?: boolean;
  isAssignee?: boolean;
  status?: TaskStatus;
  page?: number;
  pageSize?: number;
}

/** MyBatis-Plus IPage shape */
export interface IPage<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface UserBrief {
  id: number;
  username: string;
}
