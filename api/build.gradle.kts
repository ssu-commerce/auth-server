plugins {
    id("com.ssu.commerce.plugin.docker-publish")
    kotlin("kapt")
}

dependencies {
    implementation("com.ssu.commerce:ssu-commerce-core-web:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-security:${properties["ssu-commerce-core"]}")
    implementation("com.ssu.commerce:ssu-commerce-core-jpa:${properties["ssu-commerce-core"]}")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
