plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "${rootProject.group}.reforging"
version = rootProject.version

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.aregcraft.delta:meta:1.0.0")
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)
}
