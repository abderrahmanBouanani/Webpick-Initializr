Django>=4.2.0,<5.0.0
<#if database?? && database == "POSTGRES">
psycopg2-binary>=2.9.0
</#if>
<#if database?? && database == "MYSQL">
mysqlclient>=2.1.0
</#if>
