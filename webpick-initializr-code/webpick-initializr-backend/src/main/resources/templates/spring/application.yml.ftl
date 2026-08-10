spring:
  application:
    name: ${projectName}
<#if (database?? && (database == "POSTGRES" || database == "MYSQL")) || (!database?? && dependencies?seq_contains("jpa"))>
  datasource:
    <#if database?? && database == "MYSQL">
    url: jdbc:mysql://localhost:3306/${projectName?replace("-", "_")}
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
    <#else>
    url: jdbc:postgresql://localhost:5432/${projectName?replace("-", "_")}
    username: webpick
    password: password
    driver-class-name: org.postgresql.Driver
    </#if>
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
<#elseif database?? && database == "MONGODB">
  data:
    mongodb:
      uri: mongodb://localhost:27017/${projectName?replace("-", "_")}
</#if>
