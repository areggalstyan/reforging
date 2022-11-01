import kr.entree.spigradle.kotlin.spigot
import com.google.gson.GsonBuilder
import com.google.gson.Gson

plugins {
    java
    id("kr.entree.spigradle") version "2.4.2"
}

group = "com.aregcraft"
version = "1.4.0-SNAPSHOT"

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
    dependsOn(project(":doclet").tasks["shadowJar"])
    source = sourceSets["main"].allJava
    title = null
    classpath = sourceSets["main"].compileClasspath
    options.docletpath = listOf(project(":doclet").tasks.getByName<Jar>("jar").archiveFile.get().asFile)
    options.doclet = "com.aregcraft.reforgingdoclet.ReforgingDoclet"
    options.destinationDirectory = file("abilities")
    outputs.upToDateWhen { false }
}

data class AbilityProperty(val type: String, val description: String)
data class Ability(val description: String, val properties: Map<String, AbilityProperty>)

tasks.register("updateReadMe") {
    dependsOn(tasks["generateAbilities"])
    val gson = Gson()
    val price = file("abilities/price.json").bufferedReader().use { gson.fromJson(it, Ability::class.java) }
    val file = file("README.md")
    val lines = file.readLines()
    var skip = false
    file.printWriter().use { writer ->
        lines.forEach { line ->
            if (line == "<!-- </screenshots> -->" || line == "<!-- </abilities> -->") {
                skip = false
            }
            if (skip) {
                return@forEach
            }
            writer.println(line)
            if (line == "<!-- <screenshots> -->") {
                skip = true
                writer.println()
                file("screenshots").walk().filter { it.isFile }.forEach { file ->
                    val alt = file.nameWithoutExtension.replace("_", " ").split(" ")
                        .joinToString(" ") { it.capitalize().replace("On", "on") }
                    val name = file.name
                    writer.println("![$alt](https://github.com/Aregcraft/reforging/blob/master/screenshots/$name)")
                }
                writer.println()
            }
            if (line == "<!-- <abilities> -->") {
                skip = true
                writer.println()
                file("abilities").walk().filter { it.isFile }.filter { it.name != "price.json" }.forEach { file ->
                    val name = file.nameWithoutExtension
                    val ability = file.bufferedReader().use { gson.fromJson(it, Ability::class.java) }
                    writer.println("#### $name: object")
                    writer.println()
                    writer.println(ability.description)
                    writer.println()
                    writer.println("##### price: object")
                    writer.println()
                    writer.println(price.description)
                    writer.println()
                    price.properties.forEach {
                        writer.println("###### ${it.key}: ${it.value.type.toLowerCase()}")
                        writer.println()
                        writer.println(it.value.description)
                        writer.println()
                    }
                    ability.properties.forEach {
                        writer.println("##### ${it.key}: ${it.value.type.toLowerCase()}")
                        writer.println()
                        writer.println(it.value.description)
                        writer.println()
                    }
                }
                writer.println("```json")
                writer.println(file("src/main/resources/abilities.json").readText())
                writer.println("```")
                writer.println()
            }
        }
    }
    outputs.upToDateWhen { false }
}

data class Manifest(val version: String, val abilities: List<String>, val screenshots: List<String>)

tasks.register("generateManifest") {
    dependsOn(tasks["generateAbilities"])
    val gson = GsonBuilder().setPrettyPrinting().create()
    val file = file("manifest.json")
    file.createNewFile()
    file.bufferedWriter().use { writer ->
        gson.toJson(Manifest(version as String,
            file("abilities").listFiles()?.map { it.name }?.filter { it != "price.json" } ?: listOf(),
            file("screenshots").listFiles()?.map { it.name } ?: listOf()), writer)
    }
    outputs.upToDateWhen { false }
}

tasks.register("prepareRelease") {
    dependsOn(tasks["updateReadMe"])
    dependsOn(tasks["generateManifest"])
    dependsOn(tasks["prepareSpigotPlugins"])
    outputs.upToDateWhen { false }
}
