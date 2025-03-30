plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"

    checkstyle
    id("org.sonarqube") version "6.0.1.5171"

    jacoco
}

group = "com.daniilmiskevich"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


checkstyle {
    toolVersion = "10.21.2"
}

sonar {
    properties {
        property("sonar.projectKey", "DaniilMiskevich_sem4-pnayavu-labs")
        property("sonar.organization", "daniilmiskevich")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    runtimeOnly("org.postgresql:postgresql")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    systemProperty("spring.config.additional-location", "classpath:/credentials.properties")
}

tasks.test {
    useJUnitPlatform()

    finalizedBy(tasks.jacocoTestReport)

    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }

    dependsOn(tasks.test)

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).apply {
            exclude(
                "**/com/daniilmiskevich/*/dev/**",
                "**/com/daniilmiskevich/*/docs/**",
                "**/com/daniilmiskevich/*/exceptions/**",
                "**/com/daniilmiskevich/*/*/controller/**",
                "**/com/daniilmiskevich/*/*/repository/**"
            )
        }
    }))
}