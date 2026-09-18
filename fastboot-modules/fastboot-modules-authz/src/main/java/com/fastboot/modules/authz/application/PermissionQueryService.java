package com.fastboot.modules.authz.application;

import com.fastboot.modules.authz.api.PermissionQueryPort;
import com.fastboot.modules.authz.infrastructure.mapper.AuthzRelationMapper;
import java.time.Duration;
import java.util.List;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PermissionQueryService implements PermissionQueryPort {

  private static final Logger log = LoggerFactory.getLogger(PermissionQueryService.class);
  private static final Duration TTL = Duration.ofMinutes(10);

  private final AuthzRelationMapper relationMapper;
  private final RedissonClient redissonClient;

  public PermissionQueryService(AuthzRelationMapper relationMapper, RedissonClient redissonClient) {
    this.relationMapper = relationMapper;
    this.redissonClient = redissonClient;
  }

  @Override
  public List<String> listByUserId(Long userId) {
    return cachedList("fb:perm:" + userId, () -> relationMapper.listPermissionCodesByUserId(userId));
  }

  @Override
  public List<String> listRolesByUserId(Long userId) {
    return cachedList("fb:role:" + userId, () -> relationMapper.listRoleCodesByUserId(userId));
  }

  @Override
  public void evict(Long userId) {
    redissonClient.getBucket("fb:perm:" + userId).delete();
    redissonClient.getBucket("fb:role:" + userId).delete();
  }

  private List<String> cachedList(String key, java.util.function.Supplier<List<String>> loader) {
    RBucket<List<String>> bucket = redissonClient.getBucket(key);
    try {
      List<String> cached = bucket.get();
      if (cached != null) {
        return cached;
      }
    } catch (RuntimeException ex) {
      log.warn("permission cache read failed key={}", key, ex);
      bucket.delete();
    }
    List<String> fresh = loader.get();
    if (fresh == null) {
      fresh = List.of();
    }
    try {
      bucket.set(fresh, TTL);
    } catch (RuntimeException ex) {
      log.warn("permission cache write failed key={}", key, ex);
    }
    return fresh;
  }
}
