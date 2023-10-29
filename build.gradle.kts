import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.ssu.commerce.plugin.github-registry")
    id("com.ssu.commerce.plugin.docker-publish")
    id("com.ssu.commerce.plugin.maven-publish")
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}
group = "com.ssu.commerce"
version = System.getenv("VERSION") ?: "NoVersion"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("com.ssu.commerce:ssu-commerce-core-web:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-security:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-jpa:${properties["ssu-commerce-core"]}")

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
