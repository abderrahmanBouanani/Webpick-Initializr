spring:
  application:
    name: ${projectName}
<#if dependencies?seq_contains("jpa")>
  datasource:
    url: jdbc:postgresql://localhost:5432/${projectName?replace("-", "_")}
    username: webpick
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
</#if>
