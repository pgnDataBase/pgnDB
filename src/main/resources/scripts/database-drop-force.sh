#!/usr/bin/env bash
# Script used to drop database
# param1  - database name

if [[ $# -eq 1 ]] ; then
  databaseName=$1
else
  echo "Wrong number of arguments!"
  exit 1
fi

docker exec pgndb-database psql -q -d "$databaseName" -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pid <> pg_backend_pid() AND state = 'idle' AND datname != 'maindb';"
docker exec pgndb-database dropdb "$databaseName"