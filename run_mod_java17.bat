@echo off
rem Java 17 neste script:
set "JAVA_HOME=C:\Program Files\Java\jdk-17.0.17+10"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Usando Java:
java -version

rem Executa o forge:
gradlew runClient

pause
