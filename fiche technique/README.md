# Fiche Technique - Webpick Initializr

Ce projet a été réorganisé pour séparer proprement les fichiers sources, les fichiers de compilation (PDF) et les fichiers de logs/auxiliaires.

## Structure du Projet

```text
fiche technique/
├── tex/                       # Fichiers sources LaTeX
│   ├── Fiche_Technique_...tex # Fichier source principal
│   └── figures/               # Images et illustrations associees
│       ├── ENSAA_logo.png
│       └── webpick_logo.png
├── pdf/                       # PDF final genere
│   └── Fiche_Technique_...pdf
├── logs/                      # Fichiers de log et de compilation auxiliaires (.aux, .log, etc.)
└── build.bat                  # Script de compilation automatique (Windows)
```

## Compilation

Pour compiler le document et générer automatiquement le PDF dans le dossier `pdf/` tout en envoyant tous les fichiers temporaires et les logs dans le dossier `logs/` :

Double-cliquez simplement sur le fichier **`build.bat`** ou exécutez-le dans votre terminal :
```cmd
build.bat
```

Le script détectera automatiquement si `latexmk` ou `pdflatex` est installé sur votre système et compilera le document de façon optimisée.
