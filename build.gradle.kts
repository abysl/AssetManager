import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "com.abysl"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val exposedVersion: String by project
val okioVersion: String by project
val koinVersion: String by project
val ktorVersion: String by project

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.squareup.okio:okio:$okioVersion")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.1")
    implementation("net.lingala.zip4j:zip4j:2.10.0")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    implementation("com.soywiz.korlibs.korau:korau:2.7.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "com.abysl.assetmanager.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AssetManager"
            packageVersion = "1.0.0"
        }
    }
}