@echo off
chcp 65001 >nul
title Horoscope Service

echo Запуск Horoscope Service...
echo.

set JAVA_HOME=C:\Users\pasha\.jdks\corretto-24.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Используемая версия Java:
java -version
echo.

echo Запуск приложения...
java -jar target\Exam_work-1.0-SNAPSHOT.jar

pause