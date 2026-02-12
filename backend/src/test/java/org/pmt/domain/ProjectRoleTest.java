package org.pmt.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectRoleTest {

    @Test
    void shouldContainAllRoles() {
        assertNotNull(ProjectRole.ADMIN);
        assertNotNull(ProjectRole.MEMBER);
        assertNotNull(ProjectRole.VIEWER);
        assertEquals(3, ProjectRole.values().length);
    }
}
