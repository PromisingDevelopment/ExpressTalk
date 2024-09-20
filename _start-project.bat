@echo off

:: Docker backend контейнера 
cd backend
docker-compose up -d

:: Запуск frontend
start http://localhost:3000/
cd ../frontend
yarn start
