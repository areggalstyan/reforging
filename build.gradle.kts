import kr.entree.spigradle.kotlin.spigot

plugins {
    java
    id("kr.entree.spigradle") version "2.4.2"
}

group = "com.aregcraft"
version = "2.5.0-SNAPSHOT"

dependencies {
    compileOnly(spigot("1.19.2"))
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.0.7")
    implementation("org.apache.commons:commons-rng-sampling:1.5")
}

spigot {
    name = "Reforging"
    apiVersion = "1.16"
    authors = listOf("Aregcraft")
    commands {
        create("reforge") {
            description = "Reforges the item in the main hand of the player."
            usage = "Usage: /<command> <reforgeName>"
            permission = "reforging.command.reforge"
        }
        create("reloadreforging") {
            aliases = listOf("rr")
            description = "Reloads the configuration files."
            usage = "Usage: /<command>"
            permission = "reforging.command.reloadreforging"
        }
    }
}

tasks.register("debugPlugin") {
    dependsOn(tasks["prepareSpigotPlugins"])
    dependsOn(tasks["runSpigot"])
    file("build/libs").deleteRecursively()
    file("debug/spigot/plugins").walk().filter { it.extension == "jar" }.forEach { it.delete() }
    file("debug/spigot/plugins/${spigot.name}").deleteRecursively()
}

tasks.register<Javadoc>("release") {
    dependsOn(project(":doclet").tasks["shadowJar"])
    source = sourceSets["main"].allJava
    title = null
    classpath = sourceSets["main"].compileClasspath
    options {
        this as StandardJavadocDocletOptions
        docletpath = listOf(project(":doclet").tasks.getByName<Jar>("jar").archiveFile.get().asFile)
        doclet = "com.aregcraft.reforgingdoclet.ReforgingDoclet"
        destinationDirectory = projectDir
        addStringOption("version", version as String)
    }
    outputs.upToDateWhen { false }
}
