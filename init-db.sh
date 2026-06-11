#!/bin/bash
set -e

echo "Creando bases de datos..."

for db in auth_db mascotas_db usuarios_db organizaciones_db coincidencias_db geolocalizacion_db notificaciones_db; do
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname postgres -c "CREATE DATABASE $db;" 2>/dev/null || echo "Base de datos '$db' ya existe o error"
  echo "Base de datos '$db' lista"
done

echo "Todas las bases de datos creadas exitosamente"
