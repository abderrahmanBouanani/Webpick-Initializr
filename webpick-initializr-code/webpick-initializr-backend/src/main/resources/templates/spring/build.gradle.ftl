plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.3'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = '${groupId!"com.webpick"}'
version = '0.0.1-SNAPSHOT'

java {
    sourceCompatibility = '${javaVersion!"21"}'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    
    <#if (database?? && (database == "POSTGRES" || database == "MYSQL")) || dependencies?seq_contains("jpa")>
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    <#if database?? && database == "MYSQL">
    runtimeOnly 'com.mysql:mysql-connector-j'
    <#else>
    runtimeOnly 'org.postgresql:postgresql'
    </#if>
    </#if>

    <#if database?? && database == "MONGODB">
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
    </#if>

    <#if dependencies?seq_contains("spring-security")>
    implementation 'org.springframework.boot:spring-boot-starter-security'
    </#if>

    <#if dependencies?seq_contains("lombok")>
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    </#if>

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
