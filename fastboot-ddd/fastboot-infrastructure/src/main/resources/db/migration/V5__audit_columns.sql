-- 审计列 created_by / updated_by

alter table sys_user add column if not exists created_by bigint;
alter table sys_user add column if not exists updated_by bigint;
alter table sys_org_unit add column if not exists created_by bigint;
alter table sys_org_unit add column if not exists updated_by bigint;
alter table sys_role add column if not exists created_by bigint;
alter table sys_role add column if not exists updated_by bigint;
alter table sys_permission add column if not exists created_by bigint;
alter table sys_permission add column if not exists updated_by bigint;
alter table sys_menu add column if not exists created_by bigint;
alter table sys_menu add column if not exists updated_by bigint;
alter table sys_tenant add column if not exists created_by bigint;
alter table sys_tenant add column if not exists updated_by bigint;
alter table sys_tenant_package add column if not exists created_by bigint;
alter table sys_tenant_package add column if not exists updated_by bigint;
