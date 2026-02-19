# Project Management Tool (PMT)

## Project Overview

PMT is a full-stack web application designed to manage collaborative
projects.\
It allows users to create projects, manage members, assign tasks, track
task history, and receive notifications.


------------------------------------------------------------------------

## Architecture

The application follows a full-stack architecture:

-   Frontend: Angular
-   Backend: Spring Boot (Java)
-   Database: PostgreSQL
-   Containerization: Docker & Docker Compose
-   CI/CD: GitLab CI/CD
-   Code Coverage: JaCoCo (backend) & Istanbul (frontend)

------------------------------------------------------------------------

## Database Design

The database follows a normalized relational model (3NF).

Main entities:

-   USERS
-   PROJECTS
-   PROJECT_MEMBERS
-   TASKS
-   TASK_ASSIGNMENTS
-   TASK_HISTORY
-   INVITATIONS
-   NOTIFICATIONS

Many-to-many relationships are handled through junction tables: - USERS
<-> PROJECTS via PROJECT_MEMBERS - USERS <-> TASKS via TASK_ASSIGNMENTS

The ERD is available in: Schema_Base_de_Donnees_PMT.pdf

------------------------------------------------------------------------

## Running the Application

### Option 1 — Using Docker

```bash
docker-compose up --build
```


Application URLs: - Frontend: http://localhost:4200 - Backend:
http://localhost:8080

------------------------------------------------------------------------

### Option 2 --- Run Manually

#### Backend

```bash
cd backend\
mvn clean install\
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend\
npm install\
ng serve
```

------------------------------------------------------------------------

## Running Tests

### Backend

```bash
mvn test
```

Coverage report generated at:\
backend/target/site/jacoco/index.html

### Frontend

```bash
ng test --coverage
```

Coverage report generated at:\
frontend/coverage/index.html

------------------------------------------------------------------------

## Code Coverage

### Backend (JaCoCo)

-   Instruction Coverage: 96%
-   Branch Coverage: 83%

### Frontend (Angular)

-   Statements: 100%
-   Branches: 91.66%
-   Functions: 100%
-   Lines: 100%

Coverage reports are available in coverage-reports/

------------------------------------------------------------------------

## CI/CD Pipeline

The CI/CD pipeline:
1. Runs backend tests
2. Runs frontend tests
3. Builds backend
4. Builds frontend


Pipeline configuration is available in:
.gitlab-ci.yml

------------------------------------------------------------------------

## Docker

The project provides:

- backend/Dockerfile
- frontend/Dockerfile
- docker-compose.yml

Docker Compose starts:
- PostgreSQL
- Spring Boot backend
- Angular frontend

------------------------------------------------------------------------

## Project Structure

pmt/\
├── backend/\
├── frontend/\
├── docker-compose.yml\
├── coverage-reports/\
├── pmt-screenshots/\
└── README.md

------------------------------------------------------------------------
