plugins {
    kotlin("jvm")
    `java-library`
}

kotlin {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
}

// Explicitly align the Java compilation target with Kotlin's JVM 17 bytecode.
// Without this block, Java inherits the IDE's JDK (often 21), which causes
// Kotlin Gradle plugin's strict JVM target validation to fail.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api("com.badlogicgames.gdx:gdx:1.14.2")
    testImplementation("junit:junit:4.13.2")
}

tasks.test { useJUnit() }
