plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.aregcraft.delta.plugin") version "1.0.0"
}

group = "com.aregcraft"
version = "5.7.0"

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("com.aregcraft.delta:meta:1.0.0")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation("com.aregcraft.delta:api:1.0.0")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.bstats:bstats-bukkit:3.0.1")
}

pluginDescription {
    main.set("$group.reforging.Reforging")
    apiVersion.set("1.19")
    load.set("STARTUP")
    commands {
        create("reforge") {
            description.set("Reforges the weapon in the main hand of the player.")
            usage.set("Usage: /<command> <id>")
            permission.set("reforging.command.reforge")
        }
        create("reloadreforging") {
            aliases.add("rr")
            description.set("Reloads the configuration files.")
            usage.set("Usage: /<command>")
            permission.set("reforging.command.reloadreforging")
        }
        create("reforginginfo") {
            aliases.add("ri")
            description.set("Shows information about available reforges, stats, and crafting recipes.")
            permission.set("reforging.command.reforginginfo")
        }
        create("reforginggive") {
            description.set("Gives you or the player a reforge stone or reforging anvil.")
            usage.set("Usage: /<command> <id> [player]")
            permission.set("reforging.command.reforginggive")
        }
        create("updatereforging") {
            description.set("Updates the plugin.")
            usage.set("Usage: /<command>")
            permission.set("reforging.command.updatereforging")
        }
        create("reforginglang") {
            description.set("Changes the language of the plugin.")
            usage.set("Usage: /<command> <language>")
            permission.set("reforging.command.reforginglang")
        }
    }
    permissions {
        create("reforging.command.reforge") {
            description.set("Allows you to use the command /reforge")
        }
        create("reforging.command.reloadreforging") {
            description.set("Allows you to use the command /reloadreforging")
        }
        create("reforging.command.reforginginfo") {
            description.set("Allows you to use the command /reforginginfo")
            default.set("true")
        }
        create("reforging.command.reforginggive") {
            description.set("Allows you to use the command /reforginggive")
        }
        create("reforging.command.updatereforging") {
            description.set("Allows you to use the command /updatereforging")
        }
        create("reforging.command.reforginglang") {
            description.set("Allows you to use the command /reforginglang")
        }
    }
}

tasks.shadowJar {
    relocate("org.bstats", "${project.group}.reforging")
    relocate("com.aregcraft.delta", "${project.group}.reforging")
}

tasks.register<Javadoc>("generateMeta") {
    dependsOn(project(":meta").tasks["shadowJar"])
    source = sourceSets.main.get().allJava
    classpath = sourceSets.main.get().compileClasspath
    options.destinationDirectory(projectDir)
    options.docletpath(project(":meta").tasks.getByName<Jar>("shadowJar").archiveFile.get().asFile)
    options.doclet("${project.group}.reforging.meta.ReforgingMeta")
    (options as StandardJavadocDocletOptions).addStringOption("version", version.toString())
    outputs.upToDateWhen { false }
}
