import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

///////////////////////////////////////////////////////////////////////////
// Versions
///////////////////////////////////////////////////////////////////////////

val mockk = "1.12.2"
val hoplite = "1.4.16"
val koin = "3.1.5"
val kotlinCoroutines = "1.6.0"
val kotlinxJson = "1.3.2"
val kotest = "5.1.0"

///////////////////////////////////////////////////////////////////////////
// Settings
///////////////////////////////////////////////////////////////////////////

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.7.0"
}

group = "site.vie10"
version = "1.2.0"

repositories {
    mavenCentral()
}

dependencies {
    // Asynchronous
    implementation(kotlinCoroutines("core"))

    // Config
    implementation(hoplite("yaml"))

    // Dependency injection
    implementation(koin("core"))

    // Serialization
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", kotlinxJson)

    // Testing
    testImplementation(kotlinCoroutines("test"))
    testImplementation(koin("test-junit5"))
    testImplementation(kotest("runner-junit5-jvm"))
    testImplementation(kotest("assertions-core-jvm"))
    testImplementation(kotest("property-jvm"))
    testImplementation("io.mockk", "mockk", mockk)
}

subprojects {
    plugins.whenJavaPluginAdded {
        dependencies {
            compileOnly(koin("core"))
            compileOnly(kotlinCoroutines("core"))
            implementation(rootProject)
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Tasks
///////////////////////////////////////////////////////////////////////////

tasks.create<Copy>("spigotJars") {
    group = "build"

    destinationDir = buildDir.libsDir.spigotDir
    val projects = subprojects.filter { it.name.startsWith("spigot") }

    projects.forEach { dependsOn("${it.name}:shadowJar") }
    val projectLibs = projects.map { it.buildDir.libsDir }
    from(projectLibs)
}

tasks.create("allJars") {
    group = "build"

    dependsOn("spigotJars")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED
        )
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.wrapper {
    version = "7.4.2"
}

///////////////////////////////////////////////////////////////////////////
// Helpers for beautify previous code
///////////////////////////////////////////////////////////////////////////

val File.spigotDir: File
    get() = resolve("spigot")

val File.libsDir: File
    get() = resolve("libs")

fun PluginContainer.whenJavaPluginAdded(block: () -> Unit) {
    whenPluginAdded {
        if (this is JavaPlugin) block()
    }
}

fun kotest(part: String) = "io.kotest:kotest-$part:$kotest"

fun kotlinCoroutines(part: String) = "org.jetbrains.kotlinx:kotlinx-coroutines-$part:$kotlinCoroutines"

fun hoplite(part: String) = "com.sksamuel.hoplite:hoplite-$part:$hoplite"

fun koin(part: String) = "io.insert-koin:koin-$part:$koin"
