plugins {
    kotlin("jvm") version "2.1.10"
    application
}

application {
    mainClass.set("MainKt")  // For Main.kt file in default package
    // OR if Main.kt is in a package, use: mainClass.set("your.package.name.MainKt")
}

// Configure the run task to use standard input
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}