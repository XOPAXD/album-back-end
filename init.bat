@echo off
setlocal
echo ======================================================
echo           SISTEMA DE INICIALIZACAO - PROJETO ALBUM
echo ======================================================

:: --- NOVO BLOCO: INICIALIZAR DOCKER ---
echo [0/6] Verificando se o Docker Engine esta rodando...
docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [AVISO] Docker não detectado. Iniciando Docker Desktop...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    
    echo Aguardando o Docker estabilizar...
    :wait_docker
    docker info >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        timeout /t 5 /nobreak >nul
        goto :wait_docker
    )
    echo [OK] Docker pronto!
)
:: ---------------------------------------

echo [1/6] Verificando e baixando imagens Docker...
docker-compose pull
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao baixar imagens. Verifique sua conexao.
    goto :error
)

echo [2/6] Subindo Infraestrutura (Docker)...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao subir os containers.
    goto :error
)

echo [3/6] Aguardando MySQL estabilizar (30s)...
timeout /t 30 /nobreak

echo [4/6] Limpando e preparando o banco (Flyway Clean)...
call mvn flyway:clean -Dflyway.cleanDisabled=false
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Erro ao limpar o banco.
    goto :error
)

echo [5/6] Aplicando migracoes (Flyway Migrate)...
call mvn flyway:migrate
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao aplicar os scripts SQL.
    goto :error
)

echo [6/6] Iniciando a Aplicacao Spring Boot...
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