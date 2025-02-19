plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"

    // sonarqube
    id("org.sonarqube") version "6.0.1.5171"
}

group = "com.daniilmiskevich"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
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
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// testImplementation("org.springframework.boot:spring-boot-starter-test")
	// testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
