plugins {
    kotlin("jvm")
    application
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

application { mainClass.set("com.neonrush.desktop.DesktopLauncherKt") }

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.14.2")
    runtimeOnly("com.badlogicgames.gdx:gdx-platform:1.14.2:natives-desktop")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.file("assets")
    if (System.getProperty("os.name").lowercase().contains("mac")) {
        jvmArgs("-XstartOnFirstThread")
    }
}
