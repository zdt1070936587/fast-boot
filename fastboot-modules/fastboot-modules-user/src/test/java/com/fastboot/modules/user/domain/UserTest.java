package com.fastboot.modules.user.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import org.junit.jupiter.api.Test;

class UserTest {

    private final PasswordHasher hasher =
            new PasswordHasher() {
                @Override
                public String hash(String raw) {
                    return "HASH:" + raw;
                }

                @Override
                public boolean matches(String raw, String hash) {
                    return hash.equals("HASH:" + raw);
                }
            };

    @Test
    void assertCanLoginFailsWhenDisabled() {
        User user = User.restore(1L, "u", "n", "HASH:p", false, null, 0L);
        BizException ex =
                assertThrows(BizException.class, () -> user.assertCanLogin("p", hasher));
        assertEquals(ErrorCode.ACCOUNT_DISABLED.code(), ex.getCode());
    }

    @Test
    void assertCanLoginFailsWhenPasswordMissing() {
        User user = User.restore(1L, "u", "n", null, true, null, 0L);
        BizException ex =
                assertThrows(BizException.class, () -> user.assertCanLogin("p", hasher));
        assertEquals(ErrorCode.PASSWORD_NOT_INITIALIZED.code(), ex.getCode());
    }

    @Test
    void assertCanLoginFailsWhenPasswordWrong() {
        User user = User.restore(1L, "u", "n", "HASH:p", true, null, 0L);
        BizException ex =
                assertThrows(BizException.class, () -> user.assertCanLogin("wrong", hasher));
        assertEquals(ErrorCode.USER_OR_PASSWORD.code(), ex.getCode());
    }

    @Test
    void initializePasswordRejectsWhenAlreadySet() {
        User user = User.restore(1L, "u", "n", "HASH:p", true, null, 0L);
        BizException ex =
                assertThrows(BizException.class, () -> user.initializePassword("HASH:x"));
        assertEquals(ErrorCode.CONFLICT.code(), ex.getCode());
    }

    @Test
    void initializePasswordSetsHash() {
        User user = User.restore(1L, "u", "n", null, true, null, 0L);
        assertDoesNotThrow(() -> user.initializePassword("HASH:p"));
        assertTrue(user.hasPassword());
        assertEquals("HASH:p", user.getPasswordHash());
    }
}
