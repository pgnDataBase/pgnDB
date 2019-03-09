REVOKE all privileges on database admin_base FROM admin_user;
DROP DATABASE IF EXISTS admin_base;
DROP ROLE IF EXISTS admin_user;

CREATE ROLE admin_user WITH PASSWORD 'admin';
ALTER ROLE admin_user WITH LOGIN;
CREATE DATABASE admin_base;
grant all privileges on database admin_base to admin_user;