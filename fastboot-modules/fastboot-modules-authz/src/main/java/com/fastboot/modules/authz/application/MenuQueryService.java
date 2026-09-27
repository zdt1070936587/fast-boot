package com.fastboot.modules.authz.application;

import com.fastboot.modules.authz.api.MenuQueryPort;
import com.fastboot.modules.authz.api.MenuSnapshot;
import com.fastboot.modules.authz.infrastructure.mapper.AuthzRelationMapper;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysMenuEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户菜单查询服务。
 *
 * @author zhaodt
 * @since 2026-09-25
 */
@Service
public class MenuQueryService implements MenuQueryPort {

    private final AuthzRelationMapper relationMapper;

    public MenuQueryService(AuthzRelationMapper relationMapper) {
        this.relationMapper = relationMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuSnapshot> listByUserId(Long userId) {
        return relationMapper.listMenusByUserId(userId).stream().map(this::toSnapshot).toList();
    }

    private MenuSnapshot toSnapshot(SysMenuEntity entity) {
        return new MenuSnapshot(
                entity.getId(),
                entity.getParentId(),
                entity.getName(),
                entity.getPath(),
                entity.getComponent(),
                entity.getPermCode());
    }
}
