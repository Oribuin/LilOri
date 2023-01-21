import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version ("7.1.0")
}

group = "xyz.oribuin"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType(JavaCompile::class.java) {
    this.options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.2") {
        exclude(module = "opus-java")
    }

    // Utility
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.google.guava:guava:31.1-jre")

    // SQL Libraries
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.xerial:sqlite-jdbc:3.40.0.0")

}

application {
    this.mainClass.set("xyz.oribuin.lilori.LilOri")
}

tasks.withType(ShadowJar::class.java) {
    this.mergeServiceFiles()
}

tasks.withType(GradleBuild::class.java) {
    this.dependsOn("shadowJar")
    this.dependsOn("copy")
}