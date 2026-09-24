package com.fastboot.ddd.infrastructure.aop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * AOP SpEL 求值工具：解析器与表达式缓存复用。
 *
 * @author zhaodt
 * @since 2026-09-24
 */
public final class SpelSupport {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final Map<String, Expression> CACHE = new ConcurrentHashMap<>();

    private SpelSupport() {}

    public static String eval(ProceedingJoinPoint pjp, String expression) {
        Object value = evalObject(pjp, expression);
        return value == null ? "" : String.valueOf(value);
    }

    public static Object[] evalAll(JoinPoint joinPoint, String[] expressions) {
        if (expressions == null || expressions.length == 0) {
            return new Object[0];
        }
        Object[] values = new Object[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            values[i] = evalObject(joinPoint, expressions[i]);
        }
        return values;
    }

    private static Object evalObject(JoinPoint joinPoint, String expression) {
        if (expression == null || expression.isBlank()) {
            return null;
        }
        if (!expression.contains("#")) {
            return expression;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] names = DISCOVERER.getParameterNames(signature.getMethod());
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        if (names != null) {
            for (int i = 0; i < names.length; i++) {
                context.setVariable(names[i], args[i]);
            }
        }
        Expression parsed =
                CACHE.computeIfAbsent(expression, expr -> PARSER.parseExpression(expr));
        return parsed.getValue(context);
    }
}
