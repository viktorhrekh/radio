import de.undercouch.gradle.tasks.download.Download

///////////////////////////////////////////////////////////////////////////
// Versions
///////////////////////////////////////////////////////////////////////////

val bstats = "3.0.0"
val spigot = "1.12.2-R0.1-SNAPSHOT"

///////////////////////////////////////////////////////////////////////////
// Settings
///////////////////////////////////////////////////////////////////////////

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "site.vie10"
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    // Metrics
    implementation("org.bstats", "bstats-bukkit", bstats)

    // Spigot
    compileOnly("org.spigotmc", "spigot-api", spigot)
}

///////////////////////////////////////////////////////////////////////////
// Tasks
///////////////////////////////////////////////////////////////////////////

tasks.create<Exec>("run") {
    dependsOn("prepareServer")
    dependsOn("copyJarToServerPlugins")
    group = "build"
    workingDir = projectDir.serverDir
    commandLine("java", "-jar", workingDir.serverJar.name)
}

tasks.create<Copy>("copyJarToServerPlugins") {
    dependsOn("shadowJar")
    destinationDir = projectDir.serverDir.pluginsDir

    from(buildDir.libsDir)
}

tasks.create("prepareServer") {
    dependsOn("downloadServerJar")
    dependsOn("copyServerDefaults")
}

tasks.create<Download>("downloadServerJar") {
    overwrite(false)

    src("https://cdn.getbukkit.org/spigot/spigot-1.12.2.jar")
    dest(projectDir.serverDir.serverJar)
}

tasks.create<Copy>("copyServerDefaults") {
    destinationDir = projectDir.serverDir

    from(rootProject.projectDir.serverDefaultsDir)
}

tasks.shadowJar {
    relocate("org.bstats", "site.vie10")
    archiveFileName.set("${project.name}-${rootProject.name}-${rootProject.version}.jar")
}

tasks.processResources {
    val properties = linkedMapOf(
        "version" to version.toString()
    )

    filesMatching(
        setOf("plugin.yml", "radio.properties")
    ) {
        expand(properties)
    }
}

///////////////////////////////////////////////////////////////////////////
// Helpers for beautify previous code
///////////////////////////////////////////////////////////////////////////

val File.serverJar: File
    get() = resolve("server.jar")

val File.libsDir: File
    get() = resolve("libs")

val File.pluginsDir: File
    get() = resolve("plugins")

val File.serverDefaultsDir: File
    get() = resolve("server-defaults")

val File.serverDir: File
    get() = resolve(".server")
