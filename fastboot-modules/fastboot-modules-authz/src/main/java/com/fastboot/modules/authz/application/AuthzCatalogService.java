package com.fastboot.modules.authz.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fastboot.common.api.PageData;
import com.fastboot.common.error.BizException;
import com.fastboot.common.error.ErrorCode;
import com.fastboot.modules.authz.infrastructure.mapper.SysMenuMapper;
import com.fastboot.modules.authz.infrastructure.mapper.SysPermissionMapper;
import com.fastboot.modules.authz.infrastructure.mapper.SysRoleMapper;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysMenuEntity;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysPermissionEntity;
import com.fastboot.modules.authz.infrastructure.persistence.entity.SysRoleEntity;
import com.fastboot.modules.authz.interfaces.dto.MenuView;
import com.fastboot.modules.authz.interfaces.dto.PermissionView;
import com.fastboot.modules.authz.interfaces.dto.RoleView;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色、权限点与菜单目录维护服务
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Service
public class AuthzCatalogService {

    /**
     * 角色表映射器
     */
    private final SysRoleMapper roleMapper;
    /**
     * 权限点表映射器
     */
    private final SysPermissionMapper permissionMapper;
    /**
     * 菜单表映射器
     */
    private final SysMenuMapper menuMapper;

    public AuthzCatalogService(
        SysRoleMapper roleMapper, SysPermissionMapper permissionMapper, SysMenuMapper menuMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.menuMapper = menuMapper;
    }

    public PageData<RoleView> listRoles() {
        List<RoleView> items =
            roleMapper.selectList(Wrappers.emptyWrapper()).stream()
                .map(r -> new RoleView(r.getId(), r.getCode(), r.getName()))
                .toList();
        return PageData.of(items, items.size(), 1, items.size());
    }

    @Transactional
    public Long createRole(String code, String name) {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setCode(code);
        entity.setName(name);
        roleMapper.insert(entity);
        return entity.getId();
    }

    public PageData<PermissionView> listPermissions() {
        List<PermissionView> items =
            permissionMapper.selectList(Wrappers.emptyWrapper()).stream()
                .map(p -> new PermissionView(p.getId(), p.getPermType(), p.getPermCode(), p.getName()))
                .toList();
        return PageData.of(items, items.size(), 1, items.size());
    }

    @Transactional
    public Long createPermission(String type, String code, String name) {
        SysPermissionEntity entity = new SysPermissionEntity();
        entity.setPermType(type);
        entity.setPermCode(code);
        entity.setName(name);
        permissionMapper.insert(entity);
        return entity.getId();
    }

    public List<MenuView> listMenus() {
        return menuMapper.selectList(Wrappers.emptyWrapper()).stream()
            .map(
                m ->
                    new MenuView(
                        m.getId(),
                        m.getParentId(),
                        m.getName(),
                        m.getPath(),
                        m.getComponent(),
                        m.getPermCode()))
            .toList();
    }

    @Transactional
    public Long createMenu(MenuView view) {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setParentId(view.parentId());
        entity.setName(view.name());
        entity.setPath(view.path());
        entity.setComponent(view.component());
        entity.setPermCode(view.permCode());
        menuMapper.insert(entity);
        return entity.getId();
    }

    public RoleView requireRole(Long id) {
        SysRoleEntity entity = roleMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.NOT_FOUND);
        }
        return new RoleView(entity.getId(), entity.getCode(), entity.getName());
    }
}
