#!/usr/bin/env sh
cd ../common-dependency/ || exit
mvn clean install clean
cd ../common-util/ || exit
mvn clean install clean
cd ../common-dynamic-ds/ || exit
mvn clean install clean
cd ../common-service/ || exit
mvn clean install clean
cd ../common-rest/ || exit
mvn clean install clean
cd ../common-code-gen/ || exit
mvn clean install clean