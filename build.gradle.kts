import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.ssu.commerce.plugin.github-registry") version "REFACTOR_TEST"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "com.ssu.commerce"
version = "0.0.1-SNAPSHOT"
val coreVersion = "REFACTOR_TEST13"
java.sourceCompatibility = JavaVersion.VERSION_11

tasks {
    jar {
        enabled = false
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("com.ssu.commerce:ssu-commerce-core-web:$coreVersion")
    implementation("com.ssu.commerce:ssu-commerce-core-security:$coreVersion")
    implementation("com.ssu.commerce:ssu-commerce-core-jpa:$coreVersion")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
