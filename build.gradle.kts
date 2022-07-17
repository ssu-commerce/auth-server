import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "com.ssu.commerce"
version = "0.0.1-SNAPSHOT"
val coreVersion = "beta-2022.07.34"
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

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/ssu-commerce/ssu-commerce-core")
        credentials {
            username = findUserName()
            password = findToken()
        }
    }
}

fun findUserName() = (project.findProperty("gpr.user") as String?).nullWhenEmpty() ?: System.getenv("USERNAME")
fun findToken() = (project.findProperty("gpr.key") as String?).nullWhenEmpty() ?: System.getenv("TOKEN")

fun String?.nullWhenEmpty() = if (this.isNullOrEmpty()) null else this

dependencies {
    implementation("com.ssu.commerce:ssu-commerce-core:$coreVersion")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")

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
