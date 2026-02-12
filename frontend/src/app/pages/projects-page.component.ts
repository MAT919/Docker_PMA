import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProjectsService } from '../api/projects.service';
import { ProjectDto } from '../api/types';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  selector: 'app-projects-page',
  template: `
  <div class="container">
    <h1>Projects</h1>

    <div class="card">
      <h3>Create Project</h3>
      <label>Name</label>
      <input [(ngModel)]="name" name="name" />
      <label>Description</label>
      <input [(ngModel)]="description" name="description" />
      <label>Start Date</label>
      <input [(ngModel)]="startDate" name="startDate" type="date" />
      <label>Owner ID</label>
      <input [(ngModel)]="ownerId" name="ownerId" type="number" />
      <button (click)="create()">Create</button>
    </div>

    <div class="card">
      <h3>All Projects</h3>
      <button (click)="load()">Refresh</button>
      <div *ngIf="loading()">Loading...</div>
      <ul>
        <li *ngFor="let p of projects()">
          <a [routerLink]="['/projects', p.id]">{{ p.name }}</a>
          <small> — ownerId: {{ p.ownerId }}</small>
        </li>
      </ul>
    </div>
  </div>
  `,
  styles: [`
    .container{max-width:900px;margin:24px auto;font-family:Arial}
    .card{border:1px solid #ddd;border-radius:12px;padding:16px;margin:12px 0}
    input,button{display:block;margin:6px 0;padding:8px;width:100%}
    button{cursor:pointer}
  `]
})
export class ProjectsPageComponent {
  projects = signal<ProjectDto[]>([]);
  loading = signal(false);

  name = '';
  description = '';
  startDate = new Date().toISOString().slice(0,10);
  ownerId = 1;

  constructor(private api: ProjectsService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
  
    this.api.getAll().subscribe({
      next: data => {
        this.projects.set(data);
        this.loading.set(false);   // ← مهم
      },
      error: err => {
        console.error(err);
        this.loading.set(false);   // ← مهم
        alert('Failed to load projects');
      }
    });
  }


  create() {
    this.api.create({
      name: this.name,
      description: this.description,
      startDate: this.startDate,
      ownerId: Number(this.ownerId),
    }).subscribe({
      next: _ => { this.name=''; this.description=''; this.load(); },
      error: e => alert(e?.message ?? 'Error'),
    });
  }
}
