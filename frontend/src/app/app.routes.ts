import { Routes } from '@angular/router';
import { ProjectsPageComponent } from './pages/projects-page.component';
import { ProjectBoardPageComponent } from './pages/project-board-page.component';

export const routes: Routes = [
  { path: '', component: ProjectsPageComponent },
  { path: 'projects/:id', component: ProjectBoardPageComponent },
  { path: '**', redirectTo: '' }
];
