plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.aregcraft"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral();
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")
}

tasks.shadowJar {
    archiveClassifier.set("")
}
