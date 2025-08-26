#!/usr/bin/env bash
set -e
mvn -q -DskipTests package
java -jar target/library-management-system-1.0.0.jar
