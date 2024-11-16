plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    `java-library`
    id("io.spring.dependency-management")
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("org.modelmapper:modelmapper:3.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j:3.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
/* TODO
    testImplementation("org.testcontainers:neo4j")
*/

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(mapOf("path" to ":filestorage")))

}