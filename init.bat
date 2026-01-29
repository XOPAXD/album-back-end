@echo off
setlocal
echo ======================================================
echo           SISTEMA DE INICIALIZACAO - PROJETO ALBUM
echo ======================================================

echo [1/7] Iniciando Docker...
:: Tenta iniciar o Docker Desktop (caminho padrão)
start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"

:wait_docker
docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Aguardando o motor do Docker carregar...
    timeout /t 5 /nobreak >nul
    goto :wait_docker
)
echo Docker pronto!

echo [2/7] Verificando e baixando imagens Docker...
docker-compose pull
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao baixar imagens. Verifique sua conexao.
    goto :error
)

echo [3/7] Subindo Infraestrutura (Docker)...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao subir os containers.
    goto :error
)

echo [4/7] Aguardando MySQL estabilizar (30s)...
echo Por favor, aguarde. O MySQL 8.0 precisa desse tempo na primeira execucao.
timeout /t 30 /nobreak

echo [5/7] Limpando e preparando o banco (Flyway Clean)...
call mvn flyway:clean -Dflyway.cleanDisabled=false
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Erro ao limpar o banco. Verifique se o banco 'album' existe.
    goto :error
)

echo [6/7] Aplicando migracoes (Flyway Migrate)...
call mvn flyway:migrate
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao aplicar os scripts SQL.
    goto :error
)

echo [7/7] Iniciando a Aplicacao Spring Boot...
echo API disponivel em: http://localhost:8081/projeto-album
echo Swagger: http://localhost:8081/projeto-album/swagger-ui/index.html
echo ------------------------------------------------------
call mvn spring-boot:run
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] A aplicacao parou inesperadamente.
    goto :error
)

pause
exit /b 0

:error
echo.
echo ******************************************************
echo   ❌ FALHA NO PROCESSO. Verifique as mensagens acima.
echo ******************************************************
pause
exit /b 1