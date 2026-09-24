-- 基础表（最小可跑骨架，后续可扩展字段与索引）

create table if not exists sys_org_unit (
    id bigserial primary key,
    parent_id bigint,
    name varchar(128) not null,
    path varchar(512),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists sys_user (
    id bigserial primary key,
    username varchar(64) not null unique,
    display_name varchar(128),
    password_hash varchar(128),
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists sys_role (
    id bigserial primary key,
    code varchar(64) not null unique,
    name varchar(128) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists sys_permission (
    id bigserial primary key,
    perm_type varchar(16) not null,
    perm_code varchar(128) not null unique,
    name varchar(128),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists sys_menu (
    id bigserial primary key,
    parent_id bigint,
    name varchar(128) not null,
    path varchar(256),
    component varchar(256),
    perm_code varchar(128),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists rel_user_role (
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
);

create table if not exists rel_role_permission (
    role_id bigint not null,
    permission_id bigint not null,
    primary key (role_id, permission_id)
);

create table if not exists rel_role_menu (
    role_id bigint not null,
    menu_id bigint not null,
    primary key (role_id, menu_id)
);

create table if not exists sys_third_party_identity (
    id bigserial primary key,
    user_id bigint not null,
    provider varchar(32) not null,
    open_id varchar(128) not null,
    union_id varchar(128),
    access_token text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(provider, open_id)
);

create table if not exists sys_role_data_scope (
    role_id bigint not null,
    scope_type varchar(32) not null,
    primary key (role_id)
);

create table if not exists rel_role_data_scope_org (
    role_id bigint not null,
    org_unit_id bigint not null,
    primary key (role_id, org_unit_id)
);

insert into sys_user (username, display_name, enabled)
select 'admin', '管理员', true
where not exists (select 1 from sys_user where username = 'admin');

insert into sys_role (code, name)
select 'ADMIN', '管理员'
where not exists (select 1 from sys_role where code = 'ADMIN');

insert into rel_user_role (user_id, role_id)
select u.id, r.id
from sys_user u
cross join sys_role r
where u.username = 'admin' and r.code = 'ADMIN'
    and not exists (
        select 1 from rel_user_role ur
        where ur.user_id = u.id and ur.role_id = r.id
    );

insert into sys_permission (perm_type, perm_code, name)
select 'API', 'sys:demo:read', '示例只读接口'
where not exists (select 1 from sys_permission where perm_code = 'sys:demo:read');

insert into rel_role_permission (role_id, permission_id)
select r.id, p.id
from sys_role r
cross join sys_permission p
where r.code = 'ADMIN' and p.perm_code = 'sys:demo:read'
    and not exists (
        select 1 from rel_role_permission rp
        where rp.role_id = r.id and rp.permission_id = p.id
    );
