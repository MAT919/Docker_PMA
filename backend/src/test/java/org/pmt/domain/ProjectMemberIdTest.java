package org.pmt.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMemberIdTest {

    @Test
    void equals_shouldReturnTrue_forSameValues() {
        ProjectMemberId id1 = new ProjectMemberId(1L, 2L);
        ProjectMemberId id2 = new ProjectMemberId(1L, 2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equals_shouldReturnFalse_forDifferentValues() {
        ProjectMemberId id1 = new ProjectMemberId(1L, 2L);
        ProjectMemberId id2 = new ProjectMemberId(3L, 4L);

        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }
}
