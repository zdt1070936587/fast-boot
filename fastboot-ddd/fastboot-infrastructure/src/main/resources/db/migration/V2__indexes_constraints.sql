alter table sys_org_unit add column if not exists version bigint not null default 0;
alter table sys_user add column if not exists version bigint not null default 0;
alter table sys_user add column if not exists org_unit_id bigint;
alter table sys_role add column if not exists version bigint not null default 0;
alter table sys_permission add column if not exists version bigint not null default 0;
alter table sys_menu add column if not exists version bigint not null default 0;

alter table sys_third_party_identity drop column if exists access_token;

alter table sys_org_unit
  drop constraint if exists fk_org_parent;
alter table sys_org_unit
  add constraint fk_org_parent foreign key (parent_id) references sys_org_unit (id);

alter table sys_user
  drop constraint if exists fk_user_org;
alter table sys_user
  add constraint fk_user_org foreign key (org_unit_id) references sys_org_unit (id);

alter table rel_user_role
  drop constraint if exists fk_ur_user;
alter table rel_user_role
  add constraint fk_ur_user foreign key (user_id) references sys_user (id);
alter table rel_user_role
  drop constraint if exists fk_ur_role;
alter table rel_user_role
  add constraint fk_ur_role foreign key (role_id) references sys_role (id);

alter table rel_role_permission
  drop constraint if exists fk_rp_role;
alter table rel_role_permission
  add constraint fk_rp_role foreign key (role_id) references sys_role (id);
alter table rel_role_permission
  drop constraint if exists fk_rp_perm;
alter table rel_role_permission
  add constraint fk_rp_perm foreign key (permission_id) references sys_permission (id);

alter table rel_role_menu
  drop constraint if exists fk_rm_role;
alter table rel_role_menu
  add constraint fk_rm_role foreign key (role_id) references sys_role (id);
alter table rel_role_menu
  drop constraint if exists fk_rm_menu;
alter table rel_role_menu
  add constraint fk_rm_menu foreign key (menu_id) references sys_menu (id);

alter table sys_third_party_identity
  drop constraint if exists fk_tpi_user;
alter table sys_third_party_identity
  add constraint fk_tpi_user foreign key (user_id) references sys_user (id);

alter table sys_role_data_scope
  drop constraint if exists fk_rds_role;
alter table sys_role_data_scope
  add constraint fk_rds_role foreign key (role_id) references sys_role (id);

alter table rel_role_data_scope_org
  drop constraint if exists fk_rdso_role;
alter table rel_role_data_scope_org
  add constraint fk_rdso_role foreign key (role_id) references sys_role (id);
alter table rel_role_data_scope_org
  drop constraint if exists fk_rdso_org;
alter table rel_role_data_scope_org
  add constraint fk_rdso_org foreign key (org_unit_id) references sys_org_unit (id);

create index if not exists idx_org_parent on sys_org_unit (parent_id);
create index if not exists idx_org_path on sys_org_unit (path);
create index if not exists idx_user_org on sys_user (org_unit_id);
create index if not exists idx_rel_ur_role on rel_user_role (role_id);
create index if not exists idx_rel_rp_perm on rel_role_permission (permission_id);
create index if not exists idx_rel_rm_menu on rel_role_menu (menu_id);
create index if not exists idx_menu_parent on sys_menu (parent_id);

insert into sys_permission (perm_type, perm_code, name)
select 'API', 'sys:org:write', '组织写'
where not exists (select 1 from sys_permission where perm_code = 'sys:org:write');
insert into sys_permission (perm_type, perm_code, name)
select 'API', 'sys:org:read', '组织读'
where not exists (select 1 from sys_permission where perm_code = 'sys:org:read');
insert into sys_permission (perm_type, perm_code, name)
select 'API', 'sys:authz:write', '权限写'
where not exists (select 1 from sys_permission where perm_code = 'sys:authz:write');
insert into sys_permission (perm_type, perm_code, name)
select 'API', 'sys:authz:read', '权限读'
where not exists (select 1 from sys_permission where perm_code = 'sys:authz:read');

insert into rel_role_permission (role_id, permission_id)
select r.id, p.id
from sys_role r
cross join sys_permission p
where r.code = 'ADMIN'
  and p.perm_code in ('sys:org:write', 'sys:org:read', 'sys:authz:write', 'sys:authz:read', 'sys:demo:read')
  and not exists (
    select 1 from rel_role_permission rp
    where rp.role_id = r.id and rp.permission_id = p.id
  );
