version: '3.8'

services:
  api:
    build: .
    ports:
      - "8080:8080"
<#if databaseType == "POSTGRES">
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${projectName?replace("-", "_")}
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
</#if>
