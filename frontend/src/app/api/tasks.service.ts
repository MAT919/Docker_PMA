import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Page, TaskDto, TaskPriority, TaskStatus } from './types';

@Injectable({ providedIn: 'root' })
export class TasksService {
  constructor(private http: HttpClient) {}

  list(projectId: number, opts?: { page?: number; size?: number; status?: TaskStatus }) {
    const url = `${environment.apiBaseUrl}/api/projects/${projectId}/tasks`;
    let params = new HttpParams();
    if (opts?.page !== undefined) params = params.set('page', String(opts.page));
    if (opts?.size !== undefined) params = params.set('size', String(opts.size));
    if (opts?.status) params = params.set('status', opts.status);
    return this.http.get<Page<TaskDto>>(url, { params });
  }

  create(projectId: number, creatorId: number, payload: {
    title: string;
    description: string;
    dueDate: string | null;
    endDate: string | null;
    status: TaskStatus;
    priority: TaskPriority;
  }) {
    const url = `${environment.apiBaseUrl}/api/projects/${projectId}/tasks`;
    const params = new HttpParams().set('creatorId', String(creatorId));
    return this.http.post<TaskDto>(url, payload, { params });
  }

  update(projectId: number, taskId: number, actorId: number, payload: {
    title?: string | null;
    description?: string | null;
    status?: TaskStatus | null;
    priority?: TaskPriority | null;
    assigneeId?: number | null;
    dueDate?: string | null;
  }) {
    const url = `${environment.apiBaseUrl}/api/projects/${projectId}/tasks/${taskId}`;
    const params = new HttpParams().set('actorId', String(actorId));
    return this.http.put<TaskDto>(url, payload, { params });
  }
}
