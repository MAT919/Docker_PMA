package org.pmt.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldSetAndGetFields() {
        Task task = new Task();
        task.setTitle("Test");
        task.setPriority(TaskPriority.HIGH);

        assertEquals("Test", task.getTitle());
        assertEquals(TaskPriority.HIGH, task.getPriority());
    }

    @Test
    void jsonHelpers_shouldReturnNull_whenNoRelations() {
        Task task = new Task();
        assertNull(task.getProjectId());
        assertNull(task.getCreatedById());
    }

    @Test
    void jsonHelpers_shouldSetIds_andExposeThem() {
        Task task = new Task();

        task.setProjectId(10L);
        task.setCreatedById(5L);

        assertEquals(10L, task.getProjectId());
        assertEquals(5L, task.getCreatedById());
    }
}
