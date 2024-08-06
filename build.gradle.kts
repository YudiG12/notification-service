plugins {
    kotlin("jvm") version "2.0.0"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("info.solidsoft.pitest") version "1.15.0"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.modak"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("redis.clients:jedis:5.1.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.pitest:pitest-junit5-plugin:1.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.0")
    testImplementation("io.mockk:mockk:1.13.12")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.3")
}

tasks.test {
    useJUnitPlatform()
    dependsOn("pitest")
}

pitest {
    targetClasses.set(setOf("com.modak.domain.service.NotificationService*"))
    targetTests.set(setOf("com.modak.*"))
    avoidCallsTo.set(setOf("java.*", "javax.*", "org.slf4j.Logger", "org.junit.*", "org.mockito.*", "org.spockframework.*", "kotlin.jvm.internal", "kotlinx.coroutines"))
}

kotlin {
    jvmToolchain(21)
}