import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProjectsService } from '../api/projects.service';
import { TasksService } from '../api/tasks.service';
import { ProjectDto, TaskDto, TaskPriority, TaskStatus } from '../api/types';

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  selector: 'app-project-board-page',
  template: `
  <div class="container" *ngIf="project() as p">
    <a routerLink="/">← Back</a>
    <h1>{{ p.name }}</h1>
    <p>{{ p.description }}</p>

    <div class="card">
      <h3>Create Task</h3>
      <label>Creator ID</label>
      <input [(ngModel)]="creatorId" type="number" />

      <label>Title</label>
      <input [(ngModel)]="newTitle" />
      <label>Description</label>
      <input [(ngModel)]="newDesc" />

      <label>Due Date</label>
      <input [(ngModel)]="newDueDate" type="date" />

      <label>Status</label>
      <select [(ngModel)]="newStatus">
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>

      <label>Priority</label>
      <select [(ngModel)]="newPriority">
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
        <option value="CRITICAL">CRITICAL</option>
      </select>

      <button (click)="createTask()">Create</button>
    </div>

    <div class="board">
      <div class="col">
        <h3>TODO</h3>
        <div class="task" *ngFor="let t of todo()">
          <b>{{t.title}}</b>
          <p>{{t.description}}</p>
          <small>priority: {{t.priority}}</small>
          <button (click)="setStatus(t,'IN_PROGRESS')">→ IN_PROGRESS</button>
        </div>
      </div>

      <div class="col">
        <h3>IN_PROGRESS</h3>
        <div class="task" *ngFor="let t of inProgress()">
          <b>{{t.title}}</b>
          <p>{{t.description}}</p>
          <small>due: {{t.dueDate || '-'}}</small>
          <div class="row">
            <button (click)="setStatus(t,'TODO')">← TODO</button>
            <button (click)="setStatus(t,'DONE')">→ DONE</button>
          </div>
        </div>
      </div>

      <div class="col">
        <h3>DONE</h3>
        <div class="task" *ngFor="let t of done()">
          <b>{{t.title}}</b>
          <p>{{t.description}}</p>
          <button (click)="setStatus(t,'IN_PROGRESS')">← IN_PROGRESS</button>
        </div>
      </div>
    </div>
  </div>
  `,
  styles: [`
    .container{max-width:1100px;margin:24px auto;font-family:Arial}
    .card{border:1px solid #ddd;border-radius:12px;padding:16px;margin:12px 0}
    input,select,button{display:block;margin:6px 0;padding:8px;width:100%}
    .board{display:grid;grid-template-columns:1fr 1fr 1fr;gap:12px;margin-top:12px}
    .col{border:1px solid #eee;border-radius:12px;padding:12px;min-height:300px}
    .task{border:1px solid #ddd;border-radius:10px;padding:10px;margin:10px 0}
    .row{display:flex;gap:8px}
    .row button{width:auto}
  `]
})
export class ProjectBoardPageComponent {
  projectId!: number;
  project = signal<ProjectDto | null>(null);

  todo = signal<TaskDto[]>([]);
  inProgress = signal<TaskDto[]>([]);
  done = signal<TaskDto[]>([]);

  creatorId = 1;
  actorId = 1;

  newTitle = '';
  newDesc = '';
  newDueDate = '';
  newStatus: TaskStatus = 'TODO';
  newPriority: TaskPriority = 'MEDIUM';

  constructor(
    private route: ActivatedRoute,
    private projectsApi: ProjectsService,
    private tasksApi: TasksService
  ) {}

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.projectsApi.getById(this.projectId).subscribe(p => this.project.set(p));
    this.loadAll();
  }

  loadAll() {
    this.tasksApi.list(this.projectId, { page: 0, size: 200 }).subscribe(page => {
      const all = page.content || [];
      this.todo.set(all.filter(t => t.status === 'TODO'));
      this.inProgress.set(all.filter(t => t.status === 'IN_PROGRESS'));
      this.done.set(all.filter(t => t.status === 'DONE'));
    });
  }

  createTask() {
    this.tasksApi.create(this.projectId, Number(this.creatorId), {
      title: this.newTitle,
      description: this.newDesc,
      dueDate: this.newDueDate || null,
      endDate: null,
      status: this.newStatus,
      priority: this.newPriority
    }).subscribe({
      next: _ => { this.newTitle=''; this.newDesc=''; this.newDueDate=''; this.loadAll(); },
      error: e => alert(e?.message ?? 'Error')
    });
  }

  setStatus(task: TaskDto, status: TaskStatus) {
    this.tasksApi.update(this.projectId, task.id, Number(this.actorId), { status })
      .subscribe({ next: _ => this.loadAll(), error: e => alert(e?.message ?? 'Error') });
  }
}
