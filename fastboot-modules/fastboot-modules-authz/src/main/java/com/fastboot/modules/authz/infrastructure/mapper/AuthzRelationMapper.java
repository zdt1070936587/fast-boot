package com.fastboot.modules.authz.infrastructure.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * RBAC 关联关系与数据权限范围查询映射器
 *
 * @author zhaodt
 * @since 2026-09-19
 */
@Mapper
public interface AuthzRelationMapper {

  @Select(
      """
      select distinct p.perm_code
      from sys_permission p
      inner join rel_role_permission rp on p.id = rp.permission_id
      inner join rel_user_role ur on rp.role_id = ur.role_id
      where ur.user_id = #{userId}
      """)
  List<String> listPermissionCodesByUserId(@Param("userId") Long userId);

  @Select(
      """
      select distinct r.code
      from sys_role r
      inner join rel_user_role ur on r.id = ur.role_id
      where ur.user_id = #{userId}
      """)
  List<String> listRoleCodesByUserId(@Param("userId") Long userId);

  @Select("select user_id from rel_user_role where role_id = #{roleId}")
  List<Long> listUserIdsByRoleId(@Param("roleId") Long roleId);

  @Delete("delete from rel_user_role where user_id = #{userId}")
  int deleteUserRoles(@Param("userId") Long userId);

  @Insert("insert into rel_user_role(user_id, role_id) values (#{userId}, #{roleId})")
  int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

  @Delete("delete from rel_role_permission where role_id = #{roleId}")
  int deleteRolePermissions(@Param("roleId") Long roleId);

  @Insert("insert into rel_role_permission(role_id, permission_id) values (#{roleId}, #{permissionId})")
  int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

  @Delete("delete from rel_role_menu where role_id = #{roleId}")
  int deleteRoleMenus(@Param("roleId") Long roleId);

  @Insert("insert into rel_role_menu(role_id, menu_id) values (#{roleId}, #{menuId})")
  int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

  @Select(
      """
      select distinct o.org_unit_id
      from rel_role_data_scope_org o
      inner join rel_user_role ur on ur.role_id = o.role_id
      where ur.user_id = #{userId}
      """)
  List<Long> listCustomOrgIds(@Param("userId") Long userId);

  @Select(
      """
      select s.scope_type
      from sys_role_data_scope s
      inner join rel_user_role ur on ur.role_id = s.role_id
      where ur.user_id = #{userId}
      """)
  List<String> listScopeTypes(@Param("userId") Long userId);
}
