#!/usr/bin/env bash
# Script used to drop database
# param1  - database name

if [[ $# -eq 1 ]] ; then
  databaseName=$1
else
  echo "Wrong number of arguments!"
  exit 1
fi

docker exec pgndb-database dropdb "$databaseName"