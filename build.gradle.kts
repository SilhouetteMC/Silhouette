plugins {
    kotlin("jvm") version "1.9.21"

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.2"
}

group = "com.github.silhouettemc"
version = "1.0"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.github.jershell:kbson:0.5.0")

    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("org.mongodb:mongodb-driver-kotlin-sync:4.11.0")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
}

tasks.runServer {
    minecraftVersion("1.20.4")
}

tasks.compileKotlin {
    kotlinOptions.javaParameters = true
}

bukkit {
    name = "Silhouette"
    version = "${project.version}"
    authors = listOf("Aroze", "Eva")
    main = "com.github.silhouettemc.Silhouette"
    apiVersion = "1.20"
    libraries = listOf("com.h2database:h2:2.2.224")
}
