plugins {
    java
    id("com.github.johnrengelman.shadow")
}

group = "${rootProject.group}.reforging"
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)
}
