package com.fastboot.ddd.infrastructure.aop;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SpelSupportTest {

    @Test
    void evalResolvesMethodParameters() throws Exception {
        Method method = Sample.class.getDeclaredMethod("login", String.class, String.class, String.class);
        ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(pjp.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(pjp.getArgs()).thenReturn(new Object[] {"admin", "secret", "127.0.0.1"});

        assertEquals("admin", SpelSupport.eval(pjp, "#username"));
        assertEquals("127.0.0.1", SpelSupport.eval(pjp, "#clientIp"));
        Object[] values = SpelSupport.evalAll(pjp, new String[] {"#username", "#clientIp"});
        assertArrayEquals(new Object[] {"admin", "127.0.0.1"}, values);
    }

    @Test
    void evalAllDoesNotExposePasswordWhenNotListed() throws Exception {
        Method method = Sample.class.getDeclaredMethod("login", String.class, String.class, String.class);
        ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(pjp.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(pjp.getArgs()).thenReturn(new Object[] {"admin", "secret", "127.0.0.1"});

        Object[] values = SpelSupport.evalAll(pjp, new String[] {"#username", "#clientIp"});
        for (Object value : values) {
            assertEquals(false, String.valueOf(value).contains("secret"));
        }
    }

    static class Sample {
        @SuppressWarnings("unused")
        void login(String username, String password, String clientIp) {}
    }
}
