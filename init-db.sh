#!/bin/bash
# Crear múltiples bases de datos en PostgreSQL
set -e

DBS="auth_db mascotas_db usuarios_db organizaciones_db coincidencias_db geolocalizacion_db notificaciones_db"

for db in $DBS; do
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE $db;
EOSQL
  echo "Base de datos '$db' creada"
done

echo "Todas las bases de datos creadas exitosamente"
