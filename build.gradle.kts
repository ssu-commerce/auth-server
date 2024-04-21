import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.ssu.commerce.plugin.github-registry")
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

tasks {
    jar {
        enabled = false
    }
    bootJar {
        enabled = false
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "com.ssu.commerce.plugin.github-registry")

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
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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
}
