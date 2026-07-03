@echo off
setlocal enabledelayedexpansion

echo =======================================================
echo     Compilation de la Fiche Technique (LaTeX)
echo =======================================================
echo.

REM Se deplacer dans le dossier tex
cd /d "%~dp0tex"

REM Verification de latexmk
where latexmk >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo [INFO] Utilisation de latexmk...
    latexmk -pdf -auxdir=..\logs -outdir=..\pdf Fiche_Technique_Webpick_Initializr.tex
) else (
    where pdflatex >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        echo [INFO] latexmk non trouve, utilisation de pdflatex...
        if not exist "..\logs" mkdir "..\logs"
        if not exist "..\pdf" mkdir "..\pdf"
        pdflatex -aux-directory=..\logs -output-directory=..\pdf -interaction=nonstopmode Fiche_Technique_Webpick_Initializr.tex
        pdflatex -aux-directory=..\logs -output-directory=..\pdf -interaction=nonstopmode Fiche_Technique_Webpick_Initializr.tex
    ) else (
        echo [ERREUR] Aucun compilateur LaTeX ^(latexmk ou pdflatex^) n a ete trouve dans le PATH.
        echo Veuillez installer MiKTeX ou TeX Live.
        goto end
    )
)

echo.
echo =======================================================
echo [SUCCES] Compilation terminee.
echo - PDF : fiche technique/pdf/Fiche_Technique_Webpick_Initializr.pdf
echo - Logs/Auxiliaires : fiche technique/logs/
echo =======================================================

:end
pause
