plugins {
    id("com.android.application")
    kotlin("android")
}

if (file("google-services.json").exists()) { apply(plugin = "com.google.gms.google-services") }

android {
    namespace = "com.neonrush.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.neonrush.game"
        minSdk = 23
        targetSdk = 36
        versionCode = 5
        versionName = "0.5.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    // Java source/target compatibility must match Kotlin's JVM target below.
    sourceSets.getByName("main") {
        assets.srcDirs("../assets")
        jniLibs.srcDirs("src/main/jniLibs")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            // Android Studio's signing wizard must sign release builds.
        }
    }
    packaging {
        resources.excludes.addAll(listOf("META-INF/DEPENDENCIES", "META-INF/LICENSE.md", "META-INF/LICENSE-notice.md"))
    }
}

kotlin {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
}

val natives by configurations.creating

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:1.14.2")
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64").forEach { abi ->
        add("natives", "com.badlogicgames.gdx:gdx-platform:1.14.2:natives-$abi")
    }
}

val copyAndroidNatives = tasks.register("copyAndroidNatives") {
    val output = layout.projectDirectory.dir("src/main/jniLibs")
    outputs.dir(output)
    doLast {
        natives.resolve().forEach { jar ->
            val abi = listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                .firstOrNull { jar.name.endsWith("natives-$it.jar") }
                ?: error("Unexpected LibGDX natives JAR: ${jar.name}")
            copy {
                from(zipTree(jar)) { include("*.so") }
                into(output.dir(abi))
            }
        }
    }
}
tasks.configureEach {
    if (name.startsWith("merge") && name.endsWith("JniLibFolders")) {
        dependsOn(copyAndroidNatives)
    }
}
