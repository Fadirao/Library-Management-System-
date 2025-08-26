#!/usr/bin/env bash
set -e
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/schema.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/data.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/procedures.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/views.sql
mysql -h 127.0.0.1 -P 3306 -u root -proot lms < sql/triggers.sql
echo "Seeded database."
