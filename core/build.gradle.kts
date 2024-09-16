plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    `java-library`
    id("io.spring.dependency-management")
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j:3.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}