@echo off
setlocal
echo ======================================================
echo           SISTEMA DE INICIALIZACAO - PROJETO ALBUM
echo ======================================================

@echo off
echo [1/6] Verificando e baixando imagens Docker...
docker-compose pull
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Falha ao baixar imagens. Verifique sua conexao.
    goto :error
)

echo [2/6] Subindo Infraestrutura (Docker)...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] O Docker nao parece estar rodando.
    goto :error
)

echo [3/6] Aguardando MySQL estabilizar (30s)...
echo Por favor, aguarde. O MySQL 8.0 precisa desse tempo na primeira execucao.
timeout /t 30 /nobreak

echo [4/6] Limpando e preparando o banco (Flyway Clean)...
call mvn flyway:clean -Dflyway.cleanDisabled=false
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Erro ao limpar o banco. Verifique se o banco 'album' existe.
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
echo  ❌ FALHA NO PROCESSO. Verifique as mensagens acima.
echo ******************************************************
pause
exit /b 1