import kr.entree.spigradle.kotlin.spigot

plugins {
    java
    id("kr.entree.spigradle") version "2.4.2"
}

group = "com.aregcraft"
version = "1.3.0-SNAPSHOT"

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

tasks.register<Javadoc>("generateAbilities") {
    dependsOn(project("doclet").tasks["shadowJar"])
    source = sourceSets["main"].allJava
    title = null
    classpath = sourceSets["main"].compileClasspath
    options.docletpath = listOf(project(":doclet").tasks.getByName<Jar>("jar").archiveFile.get().asFile)
    options.doclet = "com.aregcraft.reforgingdoclet.ReforgingDoclet"
    options.destinationDirectory = file("abilities")
    outputs.upToDateWhen { false }
}
