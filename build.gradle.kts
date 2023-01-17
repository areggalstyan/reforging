plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.aregcraft.delta.plugin") version "1.0.0"
}

group = "com.aregcraft"
version = "4.0.0"

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(project(":meta"))
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
    implementation("com.aregcraft.delta:api:1.0.0")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.0.7")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

pluginDescription {
    main.set("$group.reforging.Reforging")
    apiVersion.set("1.19")
    load.set("STARTUP")
    commands {
        create("reforge") {
            description.set("Reforges the weapon in the main hand of the player.")
            usage.set("Usage: /<command> <reforgeName>")
            permission.set("reforging.command.reforge")
        }
        create("reloadreforging") {
            aliases.add("rr")
            description.set("Reloads the configuration files.")
            usage.set("Usage: /<command>")
            permission.set("reforging.command.reloadreforging")
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
