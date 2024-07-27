plugins {
    id("com.ssu.commerce.plugin.docker-publish")
    id("org.sonarqube") version "3.5.0.2730"
}

dependencies {
    implementation("com.ssu.commerce:ssu-commerce-core-web:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-security:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-jpa:${properties["ssu-commerce-core"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
