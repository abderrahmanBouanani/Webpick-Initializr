import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InitializrService } from './services/initializr.service';
import { ProjectRequest } from './models/project-request.model';

interface SelectionOption {
  value: string;
  name: string;
  desc: string;
  icon?: string;
}

interface DependencyOption {
  value: string;
  name: string;
  desc: string;
  icon?: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private initializrService = inject(InitializrService);

  // Form State
  projectName = 'demo';
  basePackage = 'com.example.demo';
  includeGit = false;
  gitToken = '';
  selectedBackend = 'SPRING';
  selectedFrontend = 'NONE';
  selectedDatabase = 'POSTGRES';
  
  // Spring Metadata
  groupId = 'com.example';
  artifactId = 'demo';
  javaVersion = '17';
  buildTool = 'Maven';

  // Selected arrays
  selectedDeps: string[] = [];
  selectedDevops: string[] = [];

  // Search state
  searchQuery = '';
  showSearchResults = false;

  // UI state
  loading = false;
  errorMsg = '';
  successMsg = '';

  // Options lists
  backends: SelectionOption[] = [
    { value: 'SPRING', name: 'Spring Boot', desc: 'Enterprise Java framework using Maven or Gradle.', icon: 'devicon-spring-original colored' },
    { value: 'EXPRESS', name: 'Express.js', desc: 'Minimalist Node.js web application framework.', icon: 'devicon-express-original' },
    { value: 'DJANGO', name: 'Django', desc: 'High-level Python web framework for rapid development.', icon: 'devicon-django-plain colored' },
    { value: 'SYMFONY', name: 'Symfony', desc: 'Robust PHP framework for web applications.', icon: 'devicon-symfony-original' }
  ];

  frontends: SelectionOption[] = [
    { value: 'NONE', name: 'None', desc: 'Backend service only.', icon: 'devicon-code-plain' },
    { value: 'ANGULAR', name: 'Angular', desc: 'Modern TypeScript SPA framework.', icon: 'devicon-angular-plain colored' },
    { value: 'REACT', name: 'React', desc: 'Declarative component-based UI library.', icon: 'devicon-react-original colored' }
  ];

  databases: SelectionOption[] = [
    { value: 'POSTGRES', name: 'PostgreSQL', desc: 'Advanced relational open-source database.', icon: 'devicon-postgresql-plain colored' },
    { value: 'MYSQL', name: 'MySQL', desc: 'Highly reliable relational database management system.', icon: 'devicon-mysql-plain colored' },
    { value: 'MONGODB', name: 'MongoDB', desc: 'NoSQL document-based database for scalable apps.', icon: 'devicon-mongodb-plain colored' }
  ];

  devopsToolsList = [
    { value: 'docker', name: 'Docker', desc: 'Containerize your app with Dockerfile & Compose.', icon: 'devicon-docker-plain colored' },
    { value: 'jenkins', name: 'Jenkins', desc: 'Continuous integration automation pipeline.', icon: 'devicon-jenkins-line colored' },
    { value: 'k8s', name: 'Kubernetes', desc: 'Kubernetes deployment & service manifests.', icon: 'devicon-kubernetes-plain colored' }
  ];

  // Dependencies by framework
  dependenciesMap: { [key: string]: DependencyOption[] } = {
    SPRING: [
      { value: 'jpa', name: 'Spring Data JPA', desc: 'Persist data in SQL stores with Hibernate.', icon: 'devicon-spring-original colored' },
      { value: 'spring-security', name: 'Spring Security', desc: 'Authentication and access-control framework.', icon: 'devicon-spring-original colored' },
      { value: 'lombok', name: 'Lombok', desc: 'Java library to reduce boilerplate code (Getters/Setters).', icon: 'devicon-spring-original colored' }
    ],
    SYMFONY: [
      { value: 'security', name: 'Symfony Security', desc: 'Authentication and authorization security bundle.', icon: 'devicon-symfony-original colored' },
      { value: 'maker', name: 'Symfony Maker Bundle', desc: 'CLI generator for controllers, entities, and migrations.', icon: 'devicon-symfony-original colored' },
      { value: 'api', name: 'API Serializer & Validator', desc: 'Serializer and validator components for REST APIs.', icon: 'devicon-symfony-original colored' },
      { value: 'twig', name: 'Twig Templates', desc: 'Server-side HTML rendering template engine.', icon: 'devicon-symfony-original colored' }
    ],
    EXPRESS: [
      { value: 'mongoose', name: 'Mongoose', desc: 'MongoDB object modeling for Node.js.', icon: 'devicon-express-original colored' },
      { value: 'prisma', name: 'Prisma ORM', desc: 'Next-generation Node.js and TypeScript ORM.', icon: 'devicon-express-original colored' },
      { value: 'cors', name: 'CORS', desc: 'Enable Cross-Origin Resource Sharing middleware.', icon: 'devicon-express-original' },
      { value: 'jwt', name: 'JWT Auth (jsonwebtoken)', desc: 'JSON Web Token implementation for authentication.', icon: 'devicon-express-original' }
    ],
    DJANGO: [
      { value: 'drf', name: 'Django REST Framework', desc: 'Powerful and flexible toolkit for building Web APIs.', icon: 'devicon-django-plain colored' },
      { value: 'cors', name: 'Django CORS Headers', desc: 'CORS headers handling for external requests.', icon: 'devicon-django-plain colored' },
      { value: 'pillow', name: 'Pillow', desc: 'Python Imaging Library for uploading and handling images.', icon: 'devicon-django-plain colored' }
    ]
  };

  getCurrentDependencies(): DependencyOption[] {
    return this.dependenciesMap[this.selectedBackend] || [];
  }

  get filteredDependencies(): DependencyOption[] {
    const current = this.getCurrentDependencies();
    if (!this.searchQuery.trim()) {
      return current.filter(d => !this.selectedDeps.includes(d.value));
    }
    const query = this.searchQuery.toLowerCase();
    return current.filter(d => 
      !this.selectedDeps.includes(d.value) &&
      (d.name.toLowerCase().includes(query) || d.desc.toLowerCase().includes(query))
    );
  }

  getDependencyName(value: string): string {
    const current = this.getCurrentDependencies();
    const dep = current.find(d => d.value === value);
    return dep ? dep.name : value;
  }

  getDependencyIcon(value: string): string {
    const current = this.getCurrentDependencies();
    const dep = current.find(d => d.value === value);
    return dep && dep.icon ? dep.icon : 'devicon-code-plain';
  }

  selectBackend(value: string) {
    this.selectedBackend = value;
    // Clear dependencies if switching backend
    this.selectedDeps = [];
  }

  selectFrontend(value: string) {
    this.selectedFrontend = value;
  }

  selectDatabase(value: string) {
    this.selectedDatabase = value;
  }

  toggleDevops(value: string) {
    const idx = this.selectedDevops.indexOf(value);
    if (idx > -1) {
      this.selectedDevops.splice(idx, 1);
    } else {
      this.selectedDevops.push(value);
    }
  }

  isDevopsSelected(value: string): boolean {
    return this.selectedDevops.includes(value);
  }

  addDependency(value: string) {
    if (!this.selectedDeps.includes(value)) {
      this.selectedDeps.push(value);
    }
    this.searchQuery = '';
  }

  removeDependency(value: string) {
    this.selectedDeps = this.selectedDeps.filter(d => d !== value);
  }

  generate() {
    this.errorMsg = '';
    this.successMsg = '';

    // Validation
    if (!this.projectName.trim()) {
      this.errorMsg = 'Project name is required.';
      return;
    }

    if (!/^[a-zA-Z_][a-zA-Z0-9_]*(\.[a-zA-Z_][a-zA-Z0-9_]*)*$/.test(this.basePackage)) {
      this.errorMsg = 'Base package format is invalid (e.g. com.example.demo).';
      return;
    }

    if (this.includeGit && !this.gitToken.trim()) {
      this.errorMsg = 'Git token is required when Git integration is enabled.';
      return;
    }

    this.loading = true;

    // Assemble Metadata map
    const metadata: { [key: string]: string } = {};
    if (this.selectedBackend === 'SPRING') {
      metadata['groupId'] = this.groupId;
      metadata['artifactId'] = this.artifactId;
      metadata['javaVersion'] = this.javaVersion;
      metadata['buildTool'] = this.buildTool;
    }

    const requestPayload: ProjectRequest = {
      projectName: this.projectName.trim(),
      basePackage: this.basePackage.trim(),
      includeGit: this.includeGit,
      gitToken: this.includeGit ? this.gitToken.trim() : '',
      backend: this.selectedBackend,
      frontend: this.selectedFrontend,
      db: this.selectedDatabase,
      deps: this.selectedDeps,
      devops: this.selectedDevops,
      metadata: metadata
    };

    this.initializrService.generateProject(requestPayload).subscribe({
      next: (blob) => {
        const filename = `${this.projectName}.zip`;
        this.initializrService.downloadBlob(blob, filename);
        this.loading = false;
        this.successMsg = `Project ${this.projectName}.zip generated successfully!`;
        setTimeout(() => {
          this.successMsg = '';
        }, 5000);
      },
      error: (err) => {
        this.loading = false;
        // Check if error is returned as text/JSON inside blob
        if (err.error instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            this.errorMsg = reader.result as string || 'An error occurred during project generation.';
          };
          reader.readAsText(err.error);
        } else {
          this.errorMsg = 'Connection to the backend failed. Make sure the Spring Boot application is running on port 8081.';
        }
      }
    });
  }

  // File Preview State & Logic
  activeFilePath = '';

  get cliCommand(): string {
    const backend = this.selectedBackend.toLowerCase();
    const frontend = this.selectedFrontend.toLowerCase();
    const db = this.selectedDatabase.toLowerCase();
    const activeDeps = this.selectedDeps.join(',');
    const activeDevops = this.selectedDevops.join(',');
    
    let cmd = `npx @webpick/cli init --backend ${backend} --frontend ${frontend} --db ${db}`;
    if (activeDeps) cmd += ` --deps ${activeDeps}`;
    if (activeDevops) cmd += ` --devops ${activeDevops}`;
    return cmd;
  }

  get virtualFiles(): { path: string; content: string }[] {
    const files: { path: string; content: string }[] = [];
    const backend = this.selectedBackend;
    const frontend = this.selectedFrontend;
    const db = this.selectedDatabase;
    const projName = this.projectName || 'demo';
    const pkg = this.basePackage || 'com.example.demo';
    const pkgPath = pkg.replace(/\./g, '/');

    // 1. Backend files
    if (backend === 'SPRING') {
      const jVersion = this.javaVersion || '17';
      const bTool = this.buildTool || 'Maven';
      
      if (bTool === 'Maven') {
        let depsXml = '';
        this.selectedDeps.forEach(dep => {
          depsXml += `\n        <dependency>\n            <groupId>org.springframework.boot</groupId>\n            <artifactId>spring-boot-starter-${dep === 'jpa' ? 'data-jpa' : dep}</artifactId>\n        </dependency>`;
        });
        files.push({
          path: 'pom.xml',
          content: `<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
        <relativePath/>
    </parent>
    <groupId>${this.groupId || 'com.example'}</groupId>
    <artifactId>${this.artifactId || 'demo'}</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>${projName}</name>
    
    <properties>
        <java.version>${jVersion}</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>${depsXml}
    </dependencies>
</project>`
        });
      } else {
        let depsGradle = '';
        this.selectedDeps.forEach(dep => {
          depsGradle += `\n    implementation 'org.springframework.boot:spring-boot-starter-${dep === 'jpa' ? 'data-jpa' : dep}'`;
        });
        files.push({
          path: 'build.gradle',
          content: `plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.3'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = '${this.groupId || 'com.example'}'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(${jVersion})
    }
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'${depsGradle}
}`
        });
      }

      files.push({
        path: `src/main/java/${pkgPath}/Application.java`,
        content: `package ${pkg};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}`
      });

      files.push({
        path: 'src/main/resources/application.yml',
        content: `spring:
  application:
    name: ${projName}
  datasource:
    url: jdbc:${db === 'MYSQL' ? 'mysql' : 'postgresql'}://localhost:${db === 'MYSQL' ? '3306' : '5432'}/${projName.replace(/-/g, '_')}
    username: webpick
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true`
      });

      files.push({
        path: '.gitignore',
        content: `/target/\n/.idea/\n/help/\n/build/\n.gradle/\nlocal.properties`
      });
    } else if (backend === 'SYMFONY') {
      let namespacePhp = 'App';
      const parts = pkg.split('.');
      const sb: string[] = [];
      for (const part of parts) {
        if (part) {
          sb.push(part.charAt(0).toUpperCase() + part.slice(1));
        }
      }
      if (sb.length > 0) {
        namespacePhp = sb.join('\\');
      }
      const namespaceJson = namespacePhp.replace(/\\/g, '\\\\');

      let depsComposer = '';
      if (db === 'POSTGRES' || db === 'MYSQL') {
        depsComposer += `\n        "symfony/orm-pack": "^2.4",`;
      }
      this.selectedDeps.forEach(dep => {
        if (dep === 'security') depsComposer += `\n        "symfony/security-bundle": "6.3.*",`;
        if (dep === 'api') depsComposer += `\n        "symfony/serializer": "6.3.*",\n        "symfony/validator": "6.3.*",`;
        if (dep === 'twig') depsComposer += `\n        "symfony/twig-bundle": "6.3.*",`;
      });

      files.push({
        path: 'composer.json',
        content: `{
    "name": "webpick/${projName}",
    "type": "project",
    "require": {
        "php": ">=8.1",
        "symfony/console": "6.3.*",
        "symfony/dotenv": "6.3.*",
        "symfony/flex": "^2",
        "symfony/framework-bundle": "6.3.*",${depsComposer}
        "symfony/runtime": "6.3.*",
        "symfony/yaml": "6.3.*"
    },
    "autoload": {
        "psr-4": {
            "${namespaceJson}\\\\": "src/"
        }
    }
}`
      });

      files.push({
        path: 'public/index.php',
        content: `<?php\n\nuse ${namespacePhp}\\Kernel;\n\nrequire_once dirname(__DIR__).'/vendor/autoload_runtime.php';\n\nreturn function (array $context) {\n    return new Kernel($context['APP_ENV'], (bool) $context['APP_DEBUG']);\n};`
      });

      files.push({
        path: '.env',
        content: `APP_ENV=dev\nAPP_SECRET=a82bf0d6fdebc9202685c490ffdb7f6b\nDATABASE_URL="${db === 'POSTGRES' ? 'postgresql://postgres:password@127.0.0.1:5432/' : db === 'MYSQL' ? 'mysql://root:password@127.0.0.1:3306/' : 'sqlite:///%kernel.project_dir%/var/data.db'}${projName}"`
      });

      files.push({
        path: 'src/Kernel.php',
        content: `<?php\n\nnamespace ${namespacePhp};\n\nuse Symfony\\Bundle\\FrameworkBundle\\Kernel\\MicroKernelTrait;\nuse Symfony\\Component\\HttpKernel\\Kernel as BaseKernel;\n\nclass Kernel extends BaseKernel\n{\n    use MicroKernelTrait;\n}`
      });

      files.push({
        path: 'src/Controller/HomeController.php',
        content: `<?php\n\nnamespace ${namespacePhp}\\Controller;\n\nuse Symfony\\Bundle\\FrameworkBundle\\Controller\\AbstractController;\nuse Symfony\\Component\\HttpFoundation\\JsonResponse;\nuse Symfony\\Component\\Routing\\Annotation\\Route;\n\nclass HomeController extends AbstractController\n{\n    #[Route('/api', name: 'api_home')]\n    public function index(): JsonResponse\n    {\n        return new JsonResponse([\n            'project' => '${projName}',\n            'status' => 'online',\n            'database' => '${db}'\n        ]);\n    }\n}`
      });

      let registeredBundles = `    Symfony\\Bundle\\FrameworkBundle\\FrameworkBundle::class => ['all' => true],`;
      if (db === 'POSTGRES' || db === 'MYSQL') {
        registeredBundles += `\n    Doctrine\\Bundle\\DoctrineBundle\\DoctrineBundle::class => ['all' => true],\n    Doctrine\\Bundle\\MigrationsBundle\\DoctrineMigrationsBundle::class => ['all' => true],`;
      }
      this.selectedDeps.forEach(dep => {
        if (dep === 'security') registeredBundles += `\n    Symfony\\Bundle\\SecurityBundle\\SecurityBundle::class => ['all' => true],`;
        if (dep === 'twig') registeredBundles += `\n    Symfony\\Bundle\\TwigBundle\\TwigBundle::class => ['all' => true],`;
        if (dep === 'maker') registeredBundles += `\n    Symfony\\Bundle\\MakerBundle\\MakerBundle::class => ['dev' => true],`;
      });

      files.push({
        path: 'config/bundles.php',
        content: `<?php\n\nreturn [\n${registeredBundles}\n];`
      });

      files.push({
        path: 'config/routes.yaml',
        content: `controllers:\n    resource:\n        path: ../src/Controller/\n        namespace: ${namespacePhp}\\Controller\n    type: attribute`
      });

      if (db === 'POSTGRES' || db === 'MYSQL') {
        files.push({
          path: 'config/packages/doctrine.yaml',
          content: `doctrine:\n    dbal:\n        url: '%env(resolve:DATABASE_URL)%'\n    orm:\n        auto_generate_proxy_classes: true\n        auto_mapping: true\n        mappings:\n            App:\n                type: attribute\n                dir: '%kernel.project_dir%/src/Entity'\n                prefix: '${namespacePhp}\\Entity'`
        });
      }

      files.push({
        path: '.gitignore',
        content: `/vendor/\n/var/\n.env.local\n.env.local.php\n*.log`
      });
    } else if (backend === 'EXPRESS') {
      let depsJson = '';
      this.selectedDeps.forEach(dep => {
        depsJson += `\n    "${dep}": "latest",`;
      });

      files.push({
        path: 'package.json',
        content: `{
  "name": "${projName}",
  "version": "1.0.0",
  "main": "src/index.js",
  "scripts": {
    "start": "node src/index.js",
    "dev": "nodemon src/index.js"
  },
  "dependencies": {
    "express": "^4.19.0",${depsJson}
    "cors": "^2.8.5"
  }
}`
      });

      files.push({
        path: 'src/index.js',
        content: `const express = require('express');\nconst cors = require('cors');\nconst app = express();\nconst PORT = process.env.PORT || 3000;\n\napp.use(cors());\napp.use(express.json());\n\napp.get('/api/health', (req, res) => {\n  res.json({ status: 'UP', database: '${db}' });\n});\n\napp.listen(PORT, () => {\n  console.log('Server running on port ' + PORT);\n});`
      });

      files.push({
        path: '.gitignore',
        content: `node_modules/\n.env\ndist/`
      });
    } else if (backend === 'DJANGO') {
      const djangoProjClean = projName.replace(/-/g, '_');
      
      files.push({
        path: 'manage.py',
        content: `#!/usr/bin/env python\nimport os\nimport sys\n\ndef main():\n    os.environ.setdefault('DJANGO_SETTINGS_MODULE', '${djangoProjClean}.settings')\n    try:\n        from django.core.management import execute_from_command_line\n    except ImportError as exc:\n        raise ImportError("Couldn't import Django.") from exc\n    execute_from_command_line(sys.argv)\n\nif __name__ == '__main__':\n    main()`
      });

      let reqsText = 'Django>=4.2\n';
      if (db === 'POSTGRES') reqsText += 'psycopg2-binary>=2.9\n';
      else if (db === 'MYSQL') reqsText += 'mysqlclient>=2.2\n';
      this.selectedDeps.forEach(dep => {
        reqsText += `${dep}==latest\n`;
      });

      files.push({
        path: 'requirements.txt',
        content: reqsText
      });

      files.push({
        path: `${djangoProjClean}/settings.py`,
        content: `import os\nfrom pathlib import Path\n\nBASE_DIR = Path(__file__).resolve().parent.parent\nSECRET_KEY = 'django-insecure-key-webpick'\nDEBUG = True\nALLOWED_HOSTS = []\n\nINSTALLED_APPS = [\n    'django.contrib.admin',\n    'django.contrib.auth',\n    'django.contrib.contenttypes',\n    'django.contrib.sessions',\n    'django.contrib.messages',\n    'django.contrib.staticfiles',\n]\n\nDATABASES = {\n    'default': {\n        'ENGINE': 'django.db.backends.${db === 'POSTGRES' ? 'postgresql' : db === 'MYSQL' ? 'mysql' : 'sqlite3'}',\n        'NAME': '${djangoProjClean}',\n        'USER': 'postgres',\n        'PASSWORD': 'password',\n        'HOST': '127.0.0.1',\n        'PORT': '${db === 'POSTGRES' ? '5432' : '3306'}',\n    }\n}`
      });

      files.push({
        path: `${djangoProjClean}/urls.py`,
        content: `from django.contrib import admin\nfrom django.urls import path\n\nurlpatterns = [\n    path('admin/', admin.site.urls),\n]`
      });

      files.push({
        path: `${djangoProjClean}/__init__.py`,
        content: ``
      });

      files.push({
        path: '.gitignore',
        content: `*.pyc\n__pycache__/\ndb.sqlite3\n.env\n/static/`
      });
    }

    // 2. Frontend files
    if (frontend === 'ANGULAR') {
      files.push({
        path: 'frontend/package.json',
        content: `{\n  "name": "frontend-app",\n  "version": "0.0.0",\n  "scripts": {\n    "start": "ng serve",\n    "build": "ng build"\n  },\n  "dependencies": {\n    "@angular/core": "^17.0.0"\n  }\n}`
      });
      files.push({
        path: 'frontend/src/app/app.component.ts',
        content: `import { Component } from '@angular/core';\n\n@Component({\n  selector: 'app-root',\n  standalone: true,\n  template: '<h1>Welcome to ${projName}</h1>',\n  styles: []\n})\nexport class AppComponent {}`
      });
    } else if (frontend === 'REACT') {
      files.push({
        path: 'frontend/package.json',
        content: `{\n  "name": "frontend-app",\n  "version": "0.0.0",\n  "scripts": {\n    "start": "vite",\n    "build": "vite build"\n  },\n  "dependencies": {\n    "react": "^18.2.0"\n  }\n}`
      });
      files.push({
        path: 'frontend/src/App.jsx',
        content: `import React from 'react';\n\nexport default function App() {\n  return (\n    <div>\n      <h1>Welcome to ${projName}</h1>\n    </div>\n  );\n}`
      });
    }

    // 3. DevOps files
    if (this.selectedDevops.includes('docker')) {
      files.push({
        path: 'docker-compose.yml',
        content: `version: '3.8'\n\nservices:\n  api:\n    build: .\n    ports:\n      - "8080:8080"\n    environment:\n      - DATABASE_URL=jdbc:postgresql://db:5432/webpick\n    depends_on:\n      - db\n\n  db:\n    image: postgres:15-alpine\n    ports:\n      - "5432:5432"\n    environment:\n      - POSTGRES_DB=webpick\n      - POSTGRES_USER=webpick\n      - POSTGRES_PASSWORD=password`
      });

      let dfContent = 'FROM node:18-alpine\nWORKDIR /app\nCOPY package.json ./\nRUN npm install\nCOPY . .\nEXPOSE 3000\nCMD ["npm", "start"]';
      if (backend === 'SPRING') {
        dfContent = 'FROM openjdk:17-jdk-alpine\nVOLUME /tmp\nARG JAR_FILE=target/*.jar\nCOPY ${JAR_FILE} app.jar\nENTRYPOINT ["java","-jar","/app.jar"]';
      } else if (backend === 'SYMFONY') {
        dfContent = 'FROM php:8.2-cli-alpine\nRUN apk add --no-cache git unzip bash postgresql-dev mysql-client && \\\n    docker-php-ext-install pdo pdo_mysql pdo_pgsql\nCOPY --from=composer:latest /usr/bin/composer /usr/bin/composer\nWORKDIR /app\nCOPY composer.json ./\nRUN composer install --no-scripts --no-interaction --no-autoloader\nCOPY . .\nRUN composer dump-autoload --optimize\nEXPOSE 8000\nCMD ["php", "-S", "0.0.0.0:8000", "-t", "public/"]';
      } else if (backend === 'DJANGO') {
        dfContent = 'FROM python:3.11-slim\nENV PYTHONUNBUFFERED=1\nWORKDIR /app\nCOPY requirements.txt ./\nRUN pip install -r requirements.txt\nCOPY . .\nEXPOSE 8000\nCMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]';
      }

      files.push({
        path: 'Dockerfile',
        content: dfContent
      });
    }

    if (this.selectedDevops.includes('jenkins')) {
      files.push({
        path: 'Jenkinsfile',
        content: `pipeline {\n    agent any\n    stages {\n        stage('Build') {\n            steps {\n                echo 'Building ${projName}...'\n            }\n        }\n    }\n}`
      });
    }

    if (this.selectedDevops.includes('k8s')) {
      files.push({
        path: 'k8s/deployment.yml',
        content: `apiVersion: apps/v1\nkind: Deployment\nmetadata:\n  name: ${projName}-deployment\nspec:\n  replicas: 2\n  selector:\n    matchLabels:\n      app: ${projName}\n  template:\n    metadata:\n      labels:\n        app: ${projName}\n    spec:\n      containers:\n      - name: ${projName}\n        image: registry.webpick.com/${projName}:latest`
      });
    }

    return files;
  }

  selectFile(path: string) {
    this.activeFilePath = path;
  }

  getActiveFileContent(): string {
    const files = this.virtualFiles;
    if (files.length === 0) {
      return 'No files generated.';
    }
    
    // Ensure activeFilePath is valid
    const activeFile = files.find(f => f.path === this.activeFilePath);
    if (activeFile) {
      return activeFile.content;
    }
    
    // If not found, default to first file
    this.activeFilePath = files[0].path;
    return files[0].content;
  }

  clearAlerts() {
    this.errorMsg = '';
    this.successMsg = '';
  }
}
