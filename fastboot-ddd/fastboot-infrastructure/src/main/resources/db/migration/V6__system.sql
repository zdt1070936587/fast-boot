-- system / infra 基础平台表

create table if not exists sys_dict_type (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    code varchar(64) not null,
    name varchar(128) not null,
    remark varchar(256),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0,
    unique (tenant_id, code)
);

create table if not exists sys_dict_data (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    type_code varchar(64) not null,
    label varchar(128) not null,
    value varchar(128) not null,
    sort_no int not null default 0,
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0
);

create table if not exists sys_config (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    config_key varchar(128) not null,
    config_value text,
    remark varchar(256),
    refreshable boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0,
    unique (tenant_id, config_key)
);

create table if not exists sys_notice (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    title varchar(256) not null,
    content text,
    status varchar(16) not null default 'DRAFT',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0
);

create table if not exists sys_error_code (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    code int not null,
    locale varchar(16) not null default 'zh_CN',
    message varchar(512) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0,
    unique (tenant_id, code, locale)
);

create table if not exists sys_operation_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint,
    action varchar(128),
    method varchar(256),
    args text,
    result text,
    duration_ms bigint,
    trace_id varchar(64),
    client_ip varchar(64),
    user_agent varchar(512),
    success boolean not null default true,
    error_code int,
    created_at timestamptz not null default now(),
    created_by bigint
);

create table if not exists sys_login_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint,
    username varchar(64),
    event_type varchar(32) not null,
    client_ip varchar(64),
    user_agent varchar(512),
    success boolean not null default true,
    message varchar(256),
    created_at timestamptz not null default now()
);

create table if not exists sys_post (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    code varchar(64) not null,
    name varchar(128) not null,
    sort_no int not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0,
    unique (tenant_id, code)
);

create table if not exists sys_api_access_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint,
    method varchar(16),
    path varchar(512),
    query_string varchar(1024),
    status int,
    duration_ms bigint,
    trace_id varchar(64),
    client_ip varchar(64),
    created_at timestamptz not null default now()
);

create table if not exists sys_api_error_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint,
    method varchar(16),
    path varchar(512),
    status int,
    exception_name varchar(256),
    message text,
    stack_trace text,
    trace_id varchar(64),
    client_ip varchar(64),
    created_at timestamptz not null default now()
);

create table if not exists sys_file_config (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    name varchar(128) not null,
    storage varchar(32) not null,
    master boolean not null default false,
    config jsonb not null default '{}'::jsonb,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0
);

create table if not exists sys_file (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    config_id bigint,
    name varchar(256) not null,
    path varchar(1024) not null,
    url varchar(1024),
    size_bytes bigint,
    content_type varchar(128),
    created_at timestamptz not null default now(),
    created_by bigint
);

create table if not exists sys_notify_template (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    code varchar(64) not null,
    channel varchar(32) not null,
    title varchar(256),
    content text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0,
    unique (tenant_id, code, channel)
);

create table if not exists sys_notify_message (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    template_code varchar(64),
    channel varchar(32) not null,
    receiver varchar(256) not null,
    title varchar(256),
    content text,
    status varchar(16) not null default 'PENDING',
    created_at timestamptz not null default now()
);

create table if not exists sys_inbox (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint not null,
    title varchar(256),
    content text,
    read_flag boolean not null default false,
    created_at timestamptz not null default now()
);

create table if not exists sys_job (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    name varchar(128) not null,
    handler varchar(256) not null,
    cron_expression varchar(128),
    params text,
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    created_by bigint,
    updated_by bigint,
    version bigint not null default 0
);

create table if not exists sys_job_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    job_id bigint not null,
    success boolean not null default true,
    message text,
    duration_ms bigint,
    created_at timestamptz not null default now()
);

create table if not exists sys_data_change_log (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    aggregate_type varchar(128) not null,
    aggregate_id varchar(64) not null,
    before_json jsonb,
    after_json jsonb,
    created_by bigint,
    created_at timestamptz not null default now()
);

create table if not exists scheduled_tasks (
    task_name text not null,
    task_instance text not null,
    task_data bytea,
    execution_time timestamptz not null,
    picked boolean not null default false,
    picked_by text,
    last_success timestamptz,
    last_failure timestamptz,
    consecutive_failures int,
    last_heartbeat timestamptz,
    version bigint not null,
    priority smallint,
    primary key (task_name, task_instance)
);

create index if not exists idx_sys_operation_log_tenant on sys_operation_log (tenant_id, created_at desc);
create index if not exists idx_sys_login_log_tenant on sys_login_log (tenant_id, created_at desc);
create index if not exists idx_sys_inbox_user on sys_inbox (tenant_id, user_id, read_flag);
