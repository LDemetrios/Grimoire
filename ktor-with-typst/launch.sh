#!/bin/bash

set -x

gradle shadowJar
java -cp build/libs/ktor-with-typst-1.0-SNAPSHOT-all.jar org.ldemetrios.MainKt 8081 &
sleep 5
curl http://127.0.0.1:8081/main.typ
curl http://127.0.0.1:8081/main.typ?name=Dmitry
curl http://127.0.0.1:8081/shutdown
