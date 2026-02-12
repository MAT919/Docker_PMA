export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface ProjectDto {
  id: number;
  name: string;
  description: string;
  startDate: string;
  ownerId: number;
  createdAt: string;
}

export interface TaskDto {
  id: number;
  projectId: number;
  title: string;
  description: string;
  dueDate: string | null;
  endDate: string | null;
  status: TaskStatus;
  priority: TaskPriority;
  createdBy: number | null;
  createdAt: string;
  updatedAt: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
