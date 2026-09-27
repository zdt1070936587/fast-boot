-- 多租户：租户/套餐表，并为业务表增加 tenant_id

create table if not exists sys_tenant_package (
    id bigserial primary key,
    name varchar(128) not null,
    menu_ids jsonb not null default '[]'::jsonb,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    version bigint not null default 0
);

create table if not exists sys_tenant (
    id bigserial primary key,
    code varchar(64) not null unique,
    name varchar(128) not null,
    package_id bigint,
    status varchar(16) not null default 'ENABLED',
    expire_at timestamptz,
    contact varchar(128),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    version bigint not null default 0
);

insert into sys_tenant_package (id, name, menu_ids)
select 1, '默认套餐', '[]'::jsonb
where not exists (select 1 from sys_tenant_package where id = 1);

insert into sys_tenant (id, code, name, package_id, status)
select 1, 'default', '默认租户', 1, 'ENABLED'
where not exists (select 1 from sys_tenant where id = 1);

alter table sys_org_unit add column if not exists tenant_id bigint not null default 1;
alter table sys_user add column if not exists tenant_id bigint not null default 1;
alter table sys_role add column if not exists tenant_id bigint not null default 1;
alter table sys_permission add column if not exists tenant_id bigint not null default 1;
alter table sys_menu add column if not exists tenant_id bigint not null default 1;
alter table rel_user_role add column if not exists tenant_id bigint not null default 1;
alter table rel_role_permission add column if not exists tenant_id bigint not null default 1;
alter table rel_role_menu add column if not exists tenant_id bigint not null default 1;
alter table sys_third_party_identity add column if not exists tenant_id bigint not null default 1;
alter table sys_role_data_scope add column if not exists tenant_id bigint not null default 1;
alter table rel_role_data_scope_org add column if not exists tenant_id bigint not null default 1;

alter table sys_user drop constraint if exists sys_user_username_key;
create unique index if not exists uk_sys_user_tenant_username on sys_user (tenant_id, username);

alter table sys_role drop constraint if exists sys_role_code_key;
create unique index if not exists uk_sys_role_tenant_code on sys_role (tenant_id, code);

alter table sys_permission drop constraint if exists sys_permission_perm_code_key;
create unique index if not exists uk_sys_permission_tenant_code on sys_permission (tenant_id, perm_code);

create index if not exists idx_sys_user_tenant on sys_user (tenant_id);
create index if not exists idx_sys_org_unit_tenant on sys_org_unit (tenant_id);
