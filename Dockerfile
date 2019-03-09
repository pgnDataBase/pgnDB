FROM openjdk:8-jdk-alpine

COPY ./config.json /config.json
COPY deployment/build/pgndb.jar /libs/pgndb.jar
COPY database/scripts/create_pgndb_schema.sql /src/main/resources/create_pgndb_schema.sql
COPY src/main/resources/docker/scripts /src/main/resources/scripts/

ENV PGPASSWORD admin
ENV PGHOST 172.18.0.2
ENV PGPORT 5432
ENV PGUSER admin

EXPOSE 9095

RUN echo "ipv6" >> /etc/modules
RUN apk --update add --no-cache bash postgresql-client vim

RUN sed -i 's/\r$//' /src/main/resources/scripts/database-create.sh
RUN sed -i 's/\r$//' /src/main/resources/scripts/database-drop.sh
RUN sed -i 's/\r$//' /src/main/resources/scripts/database-drop-force.sh

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/libs/pgndb.jar"]