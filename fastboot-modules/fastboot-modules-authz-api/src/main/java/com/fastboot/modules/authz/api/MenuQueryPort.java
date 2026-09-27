package com.fastboot.modules.authz.api;

import java.util.List;

/**
 * 当前用户可见菜单查询端口。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
public interface MenuQueryPort {

    List<MenuSnapshot> listByUserId(Long userId);
}
