@echo off
chcp 65001 >nul
title Horoscope Service

echo Запуск Horoscope Service...
echo.

set JAVA_HOME=C:\Users\pasha\.jdks\ms-21.0.11
set PATH=%JAVA_HOME%\bin

echo Используемая версия Java:
"%JAVA_HOME%\bin\java" -version
echo.

echo Запуск приложения...
"%JAVA_HOME%\bin\java" -jar target\Exam_work-1.0-SNAPSHOT.jar

pause