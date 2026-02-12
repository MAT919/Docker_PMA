import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ProjectDto } from './types';

@Injectable({ providedIn: 'root' })
export class ProjectsService {
  private base = `${environment.apiBaseUrl}/api/projects`;
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<ProjectDto[]>(this.base);
  }

  getById(id: number) {
    return this.http.get<ProjectDto>(`${this.base}/${id}`);
  }

  create(payload: { name: string; description: string; startDate: string; ownerId: number }) {
    return this.http.post<ProjectDto>(this.base, payload);
  }
}
