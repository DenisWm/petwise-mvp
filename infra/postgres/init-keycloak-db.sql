-- ---------------------------------------------------------------------------
-- Creates the "keycloak" database inside the shared PostgreSQL instance.
-- This script runs automatically on first container start via
-- docker-entrypoint-initdb.d.
--
-- The default "petwise" database is already created by POSTGRES_DB env var.
-- ---------------------------------------------------------------------------

SELECT 'CREATE DATABASE keycloak'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')\gexec

