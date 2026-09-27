-- OAuth2 客户端与用户绑定、WebAuthn 凭证

create table if not exists sys_oauth2_client (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    client_id varchar(128) not null,
    client_secret varchar(256),
    provider varchar(64) not null,
    redirect_uri varchar(512),
    scopes varchar(512),
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique (tenant_id, client_id)
);

create table if not exists sys_oauth2_user_bind (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint not null,
    provider varchar(64) not null,
    open_id varchar(256) not null,
    union_id varchar(256),
    created_at timestamptz not null default now(),
    unique (tenant_id, provider, open_id)
);

create table if not exists sys_webauthn_credential (
    id bigserial primary key,
    tenant_id bigint not null default 1,
    user_id bigint not null,
    credential_id varchar(512) not null,
    public_key_cose text not null,
    sign_count bigint not null default 0,
    aaguid varchar(64),
    nickname varchar(128),
    created_at timestamptz not null default now(),
    last_used_at timestamptz,
    unique (tenant_id, credential_id)
);

create index if not exists idx_webauthn_user on sys_webauthn_credential (tenant_id, user_id);
