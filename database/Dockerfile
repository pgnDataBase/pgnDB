FROM ubuntu:14.04

RUN echo "ipv6" >> /etc/modules

RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8
 
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list
 
RUN apt-get update && apt-get install -y ptop && apt-get install -y python-software-properties software-properties-common postgresql-9.5 postgresql-client-9.5 postgresql-contrib-9.5
 
RUN mkdir -p scripts
COPY scripts /scripts/

USER postgres
 
RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER admin WITH LOGIN SUPERUSER PASSWORD 'admin';" &&\
    createdb -O admin maindb  &&\
    PGOPTIONS='--client-min-messages=error' psql -d maindb -f scripts/create_maindb_schema.sql

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.5/main/pg_hba.conf
 
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.5/main/postgresql.conf
RUN echo "max_connections = 300" >> /etc/postgresql/9.5/main/postgresql.conf
RUN echo "shared_buffers = 100MB" >> /etc/postgresql/9.5/main/postgresql.conf

EXPOSE 5432

CMD ["/usr/lib/postgresql/9.5/bin/postgres", "-D", "/var/lib/postgresql/9.5/main", "-c", "config_file=/etc/postgresql/9.5/main/postgresql.conf"]
