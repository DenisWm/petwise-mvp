@echo off
REM Quick start script for PetWise documentation site (Windows)

echo Starting PetWise Documentation Site...
echo.

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not running.
    echo Please start Docker and try again.
    exit /b 1
)

echo Docker is running
echo.

REM Build and start the container
echo Building Jekyll container...
docker-compose build

echo.
echo Starting Jekyll server...
docker-compose up -d

echo.
echo Documentation site is running!
echo.
echo Access the site at:
echo    http://localhost:4000
echo.
echo LiveReload is enabled - changes will auto-refresh
echo.
echo To stop the server:
echo    docker-compose down
echo.
echo To view logs:
echo    docker-compose logs -f
echo.

pause

