# Project Management Tool (PMT)

Welcome to the **Project Management Tool (PMT)**, a complete, modern, and collaborative full-stack web application designed to help teams plan, organize, and track their work seamlessly. 

Whether you are a developer looking to coordinate tasks or a project manager seeking visibility into team progress, PMT provides a centralized hub to manage your projects, members, and workflows.

---

## What this Project is About

At its core, PMT is built to streamline collaborative workflows. It addresses the common challenges of project organization, task allocation, and progress tracking through several key modules:

### 1. Collaborative Workspaces (Projects)
Users can create distinct projects representing individual workspaces. Each project has a dedicated space where administrators can invite other registered users, manage project memberships, and coordinate team activities. 

### 2. Task Management & Lifecycle Tracking
Within each project, users can create detailed tasks. A task has a title, description, priority level, due date, and an assigned member. Tasks move through a defined lifecycle (e.g., from *To Do* to *In Progress* and *Done*). Any action taken on a task—such as creation, status changes, or re-assignment—is tracked and logged in a detailed audit history, allowing teams to see exactly how work progressed.

### 3. Real-time Notifications & Alerts
To ensure team members stay aligned without constant manual follow-ups, the application includes a notification system. Users receive alerts when they are invited to join a project, when a new task is assigned to them, or when important status updates occur.

### 4. Normalized Relational Database Model
The application database uses a highly structured relational model (Third Normal Form - 3NF) to ensure data integrity and query efficiency. The core entities include Users, Projects, Project Members, Tasks, Task Assignments, Task History, Invitations, and Notifications. Many-to-many relationships (like Users participating in multiple Projects, or Tasks assigned to multiple Users) are elegantly managed via junction tables to maintain optimal database design.

---

## Application Architecture

The system is constructed as a decoupled full-stack application leveraging modern development frameworks:

*   **Frontend (Angular)**: A responsive, component-driven Single Page Application (SPA) that provides a polished, interactive interface for users to interact with their boards, projects, and notifications.
*   **Backend (Spring Boot / Java 21)**: A secure, RESTful API that handles user authentication, business logic rules, data validation, and database operations.
*   **Database (PostgreSQL 15)**: A robust relational database system hosting the structured project and user data.
*   **Containerization (Docker & Docker Compose)**: Orchestrates the entire multi-service stack so that developers can build, link, and run all components with a single command.
*   **CI/CD Pipeline (GitLab CI/CD)**: Automates quality assurance by running unit tests, checking test coverage, and building production artifacts for both the frontend and backend on every commit.

---

## How to Run the Project

You can run the Project Management Tool in two different ways depending on your needs: using **Docker Compose** (recommended for a quick run) or running each component **Manually** (recommended for local development and debugging).

### Prerequisites
Before starting, make sure you have the following installed on your machine:
*   [Docker](https://www.docker.com/products/docker-desktop/) (for the containerized setup)
*   [Java JDK 21](https://adoptium.net/temurin/releases/) (for manual backend execution)
*   [Apache Maven](https://maven.apache.org/) (for building the backend)
*   [Node.js (v18 or higher)](https://nodejs.org/) & [Angular CLI](https://angular.io/cli) (for manual frontend execution)
*   [PostgreSQL 15](https://www.postgresql.org/) (for manual database setup)

---

### Option 1: Running Containerized with Docker (Recommended)

This is the easiest option because Docker manages all dependencies, database configurations, and service connections for you. Under the hood, Docker Compose reads the `docker-compose.yml` file, builds custom Dockerfiles for each service, connects them to a shared virtual network, and boots them in the correct dependency order.

#### Step-by-Step Execution:
1. Open your terminal in the project root directory.
2. Build the images and start the containerized services by running:
   ```bash
   docker-compose up --build
   ```
   *What this does:*
   *   It starts the **PostgreSQL database container (`pmt-db`)** and executes the `./sql/schema.sql` script to automatically initialize the table structure.
   *   It compiles the Java codebase inside the **Spring Boot backend container (`pmt-backend`)**, packages the application JAR, and exposes the REST API on port `8080`.
   *   It installs the Node dependencies inside the **Angular frontend container (`pmt-frontend`)**, compiles the application, and serves it on port `4200` (internally mapped to port `4000`).

3. Once the terminal shows that all containers are running, open your web browser and navigate to:
   *   **Frontend Interface**: `http://localhost:4200`
   *   **Backend API Endpoint**: `http://localhost:8080`

To stop the application, press `Ctrl + C` in the running terminal, or run:
```bash
docker-compose down
```

---

### Option 2: Running Manually (For Local Development)

If you are modifying code, you may want to run the backend and frontend locally on your host operating system so you can see live reload changes and debug more easily.

#### Step 1: Initialize the Database
1. Open your local PostgreSQL database administrator tool (such as pgAdmin or the `psql` command line).
2. Create a new database named `pmt_db`.
3. Create a schema inside this database named `pmt`.
4. Open the SQL query tool and execute the script located at `./sql/schema.sql` to generate all the tables, foreign keys, and indexes.

#### Step 2: Configure & Run the Backend API
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Verify the database connection properties in `src/main/resources/application.yml`. Ensure the username and password match your local PostgreSQL configuration.
3. Build the application and download dependencies:
   ```bash
   mvn clean install
   ```
4. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   The backend API will start up and listen for requests at `http://localhost:8080`.

#### Step 3: Configure & Run the Frontend UI
1. Open a new terminal window and navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install the necessary Node package dependencies:
   ```bash
   npm install
   ```
3. Launch the Angular local development server:
   ```bash
   ng serve
   ```
4. Once compiling is complete, open your browser to `http://localhost:4200` to view and interact with the application.

---

## Running the Quality Assurance Suite

Quality and reliability are maintained through automated testing on both sides of the application stack.

### Testing the Backend (Java/Spring Boot)
We use JUnit and Mockito for unit and integration testing. Code coverage metrics are compiled using **JaCoCo**.
*   To run the tests, navigate to the `backend/` directory and execute:
    ```bash
    mvn test
    ```
*   Once finished, a detailed HTML coverage report is generated. You can open and view it in your browser at:
    `backend/target/site/jacoco/index.html`

### Testing the Frontend (Angular)
We use Jasmine and Karma to verify Angular components, services, and routing.
*   To run the frontend tests, navigate to the `frontend/` directory and execute:
    ```bash
    ng test --coverage
    ```
*   This will run the test suite and output an HTML coverage report located at:
    `frontend/coverage/index.html`

---

## Continuous Integration & Deployment (CI/CD)

The project includes an automated pipeline defined in `.gitlab-ci.yml`. On every commit pushed to the repository, the pipeline executes the following stages to ensure code health:
1.  **Backend Quality**: Runs the backend test suite and generates code coverage metrics.
2.  **Frontend Quality**: Installs frontend dependencies and executes unit tests.
3.  **Compilation & Build**: Compiles both modules into production-ready deployment packages.
