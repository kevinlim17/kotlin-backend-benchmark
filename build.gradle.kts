plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.kotlin-backend-benchmark"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}