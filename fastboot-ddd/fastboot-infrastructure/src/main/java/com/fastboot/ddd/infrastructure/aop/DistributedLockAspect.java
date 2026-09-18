package com.fastboot.ddd.infrastructure.aop;

import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.common.lock.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * {@link DistributedLock} 注解切面，基于 Redisson 实现方法级分布式锁。
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Aspect
@Component
public class DistributedLockAspect {

  /** Redisson 客户端 */
  private final RedissonClient redissonClient;

  public DistributedLockAspect(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  /** 加锁执行目标方法，失败时抛出业务冲突异常 */
  @Around("@annotation(distributedLock)")
  public Object around(ProceedingJoinPoint pjp, DistributedLock distributedLock) throws Throwable {
    String key = "fb:lock:" + eval(pjp, distributedLock.key());
    RLock lock = redissonClient.getLock(key);
    boolean locked =
        lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.unit());
    if (!locked) {
      throw new BizException(ErrorCode.CONFLICT, "获取分布式锁失败");
    }
    try {
      return pjp.proceed();
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  /**
   * 解析锁键：无 SpEL 时原样返回，否则按方法参数求值。
   *
   * @param pjp 连接点
   * @param expression 键表达式
   * @return 解析后的键字符串
   */
  static String eval(ProceedingJoinPoint pjp, String expression) {
    if (!expression.contains("#")) {
      return expression;
    }
    MethodSignature signature = (MethodSignature) pjp.getSignature();
    String[] names = new DefaultParameterNameDiscoverer().getParameterNames(signature.getMethod());
    StandardEvaluationContext context = new StandardEvaluationContext();
    Object[] args = pjp.getArgs();
    if (names != null) {
      for (int i = 0; i < names.length; i++) {
        context.setVariable(names[i], args[i]);
      }
    }
    Object value = new SpelExpressionParser().parseExpression(expression).getValue(context);
    return value == null ? "" : String.valueOf(value);
  }
}
