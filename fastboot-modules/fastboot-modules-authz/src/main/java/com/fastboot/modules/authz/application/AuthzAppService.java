package com.fastboot.modules.authz.application;

import com.fastboot.modules.authz.domain.event.PermissionsChanged;
import com.fastboot.modules.authz.infrastructure.mapper.AuthzRelationMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RBAC 授权关系与数据范围应用服务
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Service
public class AuthzAppService {

    /**
     * 用户-角色-权限关联映射器
     */
    private final AuthzRelationMapper relationMapper;
    /**
     * 权限查询与缓存服务
     */
    private final PermissionQueryService permissionQueryService;
    /**
     * 领域事件发布器
     */
    private final ApplicationEventPublisher events;

    public AuthzAppService(
        AuthzRelationMapper relationMapper,
        PermissionQueryService permissionQueryService,
        ApplicationEventPublisher events) {
        this.relationMapper = relationMapper;
        this.permissionQueryService = permissionQueryService;
        this.events = events;
    }

    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        relationMapper.deleteUserRoles(userId);
        for (Long roleId : roleIds) {
            relationMapper.insertUserRole(userId, roleId);
        }
        permissionQueryService.evict(userId);
        events.publishEvent(new PermissionsChanged(userId));
    }

    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        relationMapper.deleteRolePermissions(roleId);
        for (Long permissionId : permissionIds) {
            relationMapper.insertRolePermission(roleId, permissionId);
        }
        evictRoleUsers(roleId);
    }

    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        relationMapper.deleteRoleMenus(roleId);
        for (Long menuId : menuIds) {
            relationMapper.insertRoleMenu(roleId, menuId);
        }
        evictRoleUsers(roleId);
    }

    /**
     * 空列表表示全部组织可见（无行级限制）；非空表示仅可见列出的组织 ID。
     */
    public List<Long> visibleOrgIds(Long userId, Long selfOrgId) {
        List<String> types = relationMapper.listScopeTypes(userId);
        if (types == null || types.isEmpty() || types.contains("ALL")) {
            return List.of();
        }
        Set<Long> ids = new HashSet<>();
        if (types.contains("SELF_ORG") && selfOrgId != null) {
            ids.add(selfOrgId);
        }
        if (types.contains("CUSTOM")) {
            ids.addAll(relationMapper.listCustomOrgIds(userId));
        }
        return List.copyOf(ids);
    }

    private void evictRoleUsers(Long roleId) {
        for (Long userId : relationMapper.listUserIdsByRoleId(roleId)) {
            permissionQueryService.evict(userId);
            events.publishEvent(new PermissionsChanged(userId));
        }
    }
}
