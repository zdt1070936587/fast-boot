-- 补齐平台内置权限点，并全部授予 ADMIN 角色（幂等）

insert into sys_permission (perm_type, perm_code, name)
select 'API', v.code, v.name
from (values
    ('sys:user:read',        '用户-查看'),
    ('sys:user:write',       '用户-维护'),
    ('sys:org:read',         '组织-查看'),
    ('sys:org:write',        '组织-维护'),
    ('sys:authz:read',       '角色/权限/菜单-查看'),
    ('sys:authz:write',      '角色/权限/菜单-维护'),
    ('sys:post:read',        '岗位-查看'),
    ('sys:post:write',       '岗位-维护'),
    ('sys:tenant:read',      '租户-查看'),
    ('sys:tenant:write',     '租户-维护'),
    ('sys:dict:read',        '字典-查看'),
    ('sys:dict:write',       '字典-维护'),
    ('sys:config:read',      '参数-查看'),
    ('sys:config:write',     '参数-维护'),
    ('sys:notice:read',      '通知-查看'),
    ('sys:notice:write',     '通知-维护'),
    ('sys:error-code:read',  '错误码-查看'),
    ('sys:error-code:write', '错误码-维护'),
    ('sys:audit:read',       '审计日志-查看'),
    ('sys:online:read',      '在线用户-查看'),
    ('sys:online:write',     '在线用户-踢出'),
    ('sys:file:read',        '文件-查看'),
    ('sys:file:write',       '文件-维护'),
    ('sys:job:read',         '定时任务-查看'),
    ('sys:job:write',        '定时任务-维护'),
    ('sys:inbox:read',       '站内信-查看'),
    ('sys:inbox:write',      '站内信-维护'),
    ('sys:notify:write',     '通知发送')
) as v(code, name)
where not exists (select 1 from sys_permission p where p.perm_code = v.code);

insert into rel_role_permission (role_id, permission_id)
select r.id, p.id
from sys_role r
cross join sys_permission p
where r.code = 'ADMIN'
    and p.perm_code like 'sys:%'
    and not exists (
        select 1 from rel_role_permission rp
        where rp.role_id = r.id and rp.permission_id = p.id
    );
