
CREATE SCHEMA IF NOT EXISTS pmt;
SET search_path TO pmt;

CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TYPE project_role AS ENUM ('ADMIN','MEMBER','VIEWER');

CREATE TABLE IF NOT EXISTS projects (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  description TEXT,
  start_date DATE NOT NULL,
  owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS project_members (
  project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role project_role NOT NULL,
  added_at TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (project_id, user_id)
);

CREATE TYPE task_status AS ENUM ('TODO','IN_PROGRESS','DONE','BLOCKED');
CREATE TYPE task_priority AS ENUM ('LOW','MEDIUM','HIGH','CRITICAL');

CREATE TABLE IF NOT EXISTS tasks (
  id BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  title VARCHAR(160) NOT NULL,
  description TEXT,
  due_date DATE,
  end_date DATE,
  status task_status NOT NULL DEFAULT 'TODO',
  priority task_priority NOT NULL DEFAULT 'MEDIUM',
  created_by BIGINT NOT NULL REFERENCES users(id),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS task_assignments (
  task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  assigned_at TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (task_id, user_id)
);

CREATE TABLE IF NOT EXISTS task_history (
  id BIGSERIAL PRIMARY KEY,
  task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
  actor_id BIGINT NOT NULL REFERENCES users(id),
  field VARCHAR(64) NOT NULL,
  old_value TEXT,
  new_value TEXT,
  changed_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS invitations (
  id BIGSERIAL PRIMARY KEY,
  project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
  email VARCHAR(255) NOT NULL,
  role project_role NOT NULL DEFAULT 'VIEWER',
  token VARCHAR(64) NOT NULL UNIQUE,
  status VARCHAR(16) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  expires_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS notifications (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  type VARCHAR(40) NOT NULL,
  payload JSONB,
  is_read BOOLEAN NOT NULL DEFAULT false,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- seed
INSERT INTO users (username, email, password_hash) VALUES
('alice','alice@example.com','{noop}pass'),
('bob','bob@example.com','{noop}pass'),
('carol','carol@example.com','{noop}pass')
ON CONFLICT DO NOTHING;

INSERT INTO projects (name, description, start_date, owner_id) VALUES
('PMT Core','Plateforme PMT','2025-10-01',1);

INSERT INTO project_members (project_id, user_id, role) VALUES
(1,1,'ADMIN'),(1,2,'MEMBER'),(1,3,'VIEWER');

INSERT INTO tasks (project_id, title, description, due_date, status, priority, created_by) VALUES
(1,'Configurer CI','Mettre en place GitHub Actions','2025-10-20','IN_PROGRESS','HIGH',1),
(1,'Modèle DB','Créer schéma + SQL','2025-10-18','TODO','CRITICAL',1);
