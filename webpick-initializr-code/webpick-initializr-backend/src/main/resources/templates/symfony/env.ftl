# In all environments, the following files are loaded if they exist,
# the latter taking precedence over the former:
#
#  * .env                contains default values for the environment variables needed by the app
#  * .env.local          uncommitted user-specific overrides
#
# Real environment variables win over .env files.

###> symfony/framework-bundle ###
APP_ENV=dev
APP_SECRET=a82bf0d6fdebc9202685c490ffdb7f6b
###< symfony/framework-bundle ###

###> doctrine/doctrine-bundle ###
# Format described at https://www.doctrine-project.org/projects/doctrine-dbal/en/latest/reference/configuration.html#connecting-using-a-url
# IMPORTANT: You MUST configure your server version, either here or in config/packages/doctrine.yaml
#
<#if database?? && database == "POSTGRES">
DATABASE_URL="postgresql://postgres:password@127.0.0.1:5432/${projectName}?serverVersion=15&charset=utf8"
<#elseif database?? && database == "MYSQL">
DATABASE_URL="mysql://root:password@127.0.0.1:3306/${projectName}?serverVersion=8.0.32&charset=utf8mb4"
<#else>
DATABASE_URL="sqlite:///%kernel.project_dir%/var/data.db"
</#if>
###< doctrine/doctrine-bundle ###
