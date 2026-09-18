create schema if not exists fb_user;
create schema if not exists fb_authz;
create schema if not exists fb_org;

comment on schema fb_user is 'Reserved for experimental per-service schema split';
comment on schema fb_authz is 'Reserved for experimental per-service schema split';
comment on schema fb_org is 'Reserved for experimental per-service schema split';
