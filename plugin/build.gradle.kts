plugins {
    java
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(project(":core"))
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.0.7")
    implementation("org.apache.commons:commons-rng-sampling:1.5")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}
