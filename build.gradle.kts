import kr.entree.spigradle.kotlin.spigot

plugins {
    java
    id("kr.entree.spigradle") version "2.4.2"
}

group = "com.aregcraft"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(spigot("1.19.2"))
}

spigot {
    name = "Reforging"
    apiVersion = "1.16"
    authors = listOf("Aregcraft")
}

tasks.register("debugPlugin") {
    dependsOn(tasks["prepareSpigotPlugins"])
    dependsOn(tasks["runSpigot"])
}
