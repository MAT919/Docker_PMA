package org.pmt.repository;

import org.pmt.domain.Task;
import org.pmt.domain.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);

    Page<Task> findByProject_IdAndStatus(Long projectId, TaskStatus status, Pageable pageable);
}
