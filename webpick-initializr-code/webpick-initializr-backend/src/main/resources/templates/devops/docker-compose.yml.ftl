version: '3.8'

services:
  api:
    build: .
    ports:
      <#if backendFramework?? && backendFramework == "SPRING">
      - "8080:8080"
      <#elseif backendFramework?? && backendFramework == "EXPRESS">
      - "3000:3000"
      <#else>
      - "8000:8000"
      </#if>
<#if databaseType == "POSTGRES">
    environment:
      <#if backendFramework?? && backendFramework == "SPRING">
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${projectName?replace("-", "_")}
      <#elseif backendFramework?? && backendFramework == "SYMFONY">
      - DATABASE_URL=postgresql://postgres:password@db:5432/${projectName?replace("-", "_")}
      <#else>
      - DATABASE_URL=postgresql://webpick:password@db:5432/${projectName?replace("-", "_")}
      </#if>
    depends_on:
      - db

  db:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=${projectName?replace("-", "_")}
      - POSTGRES_USER=webpick
      - POSTGRES_PASSWORD=password
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
<#elseif databaseType == "MYSQL">
    environment:
      <#if backendFramework?? && backendFramework == "SPRING">
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/${projectName?replace("-", "_")}
      <#else>
      - DATABASE_URL=mysql://root:password@db:3306/${projectName?replace("-", "_")}
      </#if>
    depends_on:
      - db

  db:
    image: mysql:8
    ports:
      - "3306:3306"
    environment:
      - MYSQL_DATABASE=${projectName?replace("-", "_")}
      - MYSQL_ROOT_PASSWORD=password
    volumes:
      - mysqldata:/var/lib/mysql

volumes:
  mysqldata:
<#elseif databaseType == "MONGODB">
    environment:
      - DATABASE_URL=mongodb://db:27017/${projectName?replace("-", "_")}
    depends_on:
      - db

  db:
    image: mongo:6
    ports:
      - "27017:27017"
    volumes:
      - mongodata:/data/db

volumes:
  mongodata:
</#if>
