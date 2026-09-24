package com.fastboot.modules.org.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

class OrgUnitTest {

    @Test
    void assignIdToPathBuildsMaterializedPath() {
        OrgUnit unit = OrgUnit.create(null, "root", "");
        unit.setId(10L);
        unit.assignIdToPath();
        assertEquals("10/", unit.getPath());
    }

    @Test
    void moveToRejectsSelf() {
        OrgUnit unit = OrgUnit.restore(1L, null, "a", "1/", 0L);
        OrgUnit same = OrgUnit.restore(1L, null, "a", "1/", 0L);
        BizException ex = assertThrows(BizException.class, () -> unit.moveTo(same));
        assertEquals(ErrorCode.CONFLICT.code(), ex.getCode());
    }

    @Test
    void moveToRejectsDescendant() {
        OrgUnit unit = OrgUnit.restore(1L, null, "a", "1/", 0L);
        OrgUnit child = OrgUnit.restore(2L, 1L, "b", "1/2/", 0L);
        BizException ex = assertThrows(BizException.class, () -> unit.moveTo(child));
        assertEquals(ErrorCode.CONFLICT.code(), ex.getCode());
    }

    @Test
    void moveToUpdatesParentAndPath() {
        OrgUnit unit = OrgUnit.restore(2L, 1L, "b", "1/2/", 0L);
        OrgUnit parent = OrgUnit.restore(3L, null, "c", "3/", 0L);
        unit.moveTo(parent);
        assertEquals(3L, unit.getParentId());
        assertEquals("3/2/", unit.getPath());
    }
}
