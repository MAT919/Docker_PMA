FROM postgres:15
COPY ./sql/schema.sql /docker-entrypoint-initdb.d/1-schema.sql
