package com.fastboot.modules.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 用户查询端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface UserQueryPort {

    Optional<UserSnapshot> find(Long id);

    List<UserSnapshot> findByIds(Collection<Long> ids);
}
