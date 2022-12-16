plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.aregcraft.reforging"
version = "3.2.0-SNAPSHOT"

allprojects {
    group = project.group
    version = project.version
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project("core"))
    implementation(project("plugin"))
}

tasks.shadowJar {
    relocate("org.bstats", "${project.group}.plugin")
    archiveClassifier.set("")
}

tasks.register<Javadoc>("release") {
    dependsOn(gradle.includedBuild("doclet").task(":shadowJar"))
    val main = project("plugin").sourceSets["main"]
    source(main.allJava)
    classpath = main.compileClasspath
    options {
        this as StandardJavadocDocletOptions
        docletpath(file("doclet/build/libs/doclet-$version.jar"))
        doclet("${project.group}.doclet.ReforgingDoclet")
        destinationDirectory(projectDir)
        addStringOption("version", version as String)
    }
    outputs.upToDateWhen { false }
}

tasks.register("generatePluginDescription") {
    File(sourceSets["main"].output.resourcesDir, "plugin.yml").writeText("""
        name: Reforging
        main: ${project.group}.plugin.Reforging
        version: $version
        api-version: 1.19
        author: Aregcraft
        website: https://reforging.vercel.app/
        commands:
            reforge:
                description: Reforges the item in the main hand of the player.
                usage: "Usage: /<command> <reforgeName>"
                permission: reforging.command.reforge
            reloadreforging:
                aliases: rr
                description: Reloads the configuration files.
                usage: "Usage: /<command>"
                permission: reforging.command.reloadreforging
    """.trimIndent())
}

tasks.register("prepareDebug") {
    file("build/libs").walk().filter { it.extension == "jar" }.forEach { it.delete() }
    file("debug/spigot/plugins").walk().filter { it.extension == "jar" }.forEach { it.delete() }
    file("debug/spigot/plugins/Reforging").deleteRecursively()
}

tasks.register<Copy>("copyPlugin") {
    dependsOn(tasks["prepareDebug"])
    dependsOn(tasks["generatePluginDescription"])
    from(tasks["shadowJar"])
    destinationDir = file("debug/spigot/plugins")
}

tasks.register<JavaExec>("debugPlugin") {
    dependsOn(tasks["copyPlugin"])
    workingDir("debug/spigot")
    classpath(files("debug/spigot/server.jar"))
    args("nogui")
    standardInput = System.`in`
}
