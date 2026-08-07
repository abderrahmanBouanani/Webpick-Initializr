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
            "App\\": "src/"
        }
    },
    "autoload-dev": {
        "psr-4": {
            "App\\Tests\\": "tests/"
        }
    }
}
