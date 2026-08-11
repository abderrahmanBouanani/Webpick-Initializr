{
    "name": "webpick/${projectName}",
    "type": "project",
    "license": "proprietary",
    "minimum-stability": "stable",
    "prefer-stable": true,
    "require": {
        "php": ">=8.1",
        "ext-ctype": "*",
        "ext-iconv": "*",
        "symfony/console": "6.3.*",
        "symfony/dotenv": "6.3.*",
        "symfony/flex": "^2",
        "symfony/framework-bundle": "6.3.*",
        "symfony/runtime": "6.3.*",
        "symfony/yaml": "6.3.*"
        <#if database?? && (database == "POSTGRES" || database == "MYSQL")>
        ,"symfony/orm-pack": "^2.4"
        </#if>
        <#if dependencies??>
        <#if dependencies?seq_contains("security")>
        ,"symfony/security-bundle": "6.3.*"
        </#if>
        <#if dependencies?seq_contains("api")>
        ,"symfony/serializer": "6.3.*"
        ,"symfony/validator": "6.3.*"
        </#if>
        <#if dependencies?seq_contains("twig")>
        ,"symfony/twig-bundle": "6.3.*"
        </#if>
        </#if>
    },
    "require-dev": {
        "symfony/error-handler": "6.3.*"
        <#if dependencies?? && dependencies?seq_contains("maker")>
        ,"symfony/maker-bundle": "^1.50"
        </#if>
    },
    "config": {
        "allow-plugins": {
            "php-http/discovery": true,
            "symfony/flex": true,
            "symfony/runtime": true
        },
        "sort-packages": true
    },
    "autoload": {
        "psr-4": {
            "${namespaceJson}\\": "src/"
        }
    },
    "autoload-dev": {
        "psr-4": {
            "${namespaceJson}\\Tests\\": "tests/"
        }
    }
}
