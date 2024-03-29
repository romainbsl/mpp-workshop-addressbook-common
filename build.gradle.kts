plugins {
    kotlin("multiplatform") version "1.3.61"
    kotlin("plugin.serialization") version "1.3.61"
    `maven-publish`
}

group = "com.mybusiness"
version = "1.0.0"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
}

kotlin {
    jvm("android")

    iosX64("ios") {
        binaries {
            framework {
                baseName = "AddressBookCommon"
            }
        }
    }

    js { browser() }

    sourceSets {
        // Versions
        val ktorVersion = "1.2.6"
        val coroutinesVersion = "1.3.2"
        val serializationVersion = "0.14.0"
        // Shortcuts
        fun kotlinx(module: String, version: String)
                = "org.jetbrains.kotlinx:kotlinx-$module:$version"
        fun coroutines(module: String = "")
                = kotlinx("coroutines-core$module", coroutinesVersion)
        fun serialization(module: String = "")
                = kotlinx("serialization-runtime$module", serializationVersion)
        fun ktorClient(module: String, version: String
        = ktorVersion) = "io.ktor:ktor-client-$module:$version"

        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation(kotlin("stdlib-common"))
                // Kotlinx
                implementation(coroutines("-common"))
                implementation(serialization("-common"))
                // Ktor client
                implementation(ktorClient("core"))
                implementation(ktorClient("json"))
                implementation(ktorClient("serialization"))
            }
        }
        val androidMain by getting {
            dependencies {
                // Kotlin
                implementation(kotlin("stdlib"))
                // Kotlinx
                implementation(coroutines())
                implementation(serialization())
                // Ktor client
                implementation(ktorClient("core-jvm"))
                implementation(ktorClient("json-jvm"))
                implementation(ktorClient("serialization-jvm"))
                implementation(ktorClient("okhttp"))
            }
        }

        val iosMain by getting {
            dependencies {
                // Kotlinx
                implementation(coroutines("-native"))
                implementation(serialization("-native"))
                // Ktor client
                implementation(ktorClient("core-native"))
                implementation(ktorClient("json-native"))
                implementation(ktorClient("serialization-native"))
                implementation(ktorClient("ios"))
            }
        }

        val jsMain by getting {
            dependencies {
                // Kotlin
                implementation(kotlin("stdlib-js"))
                // Kotlinx
                implementation(coroutines("-js"))
                implementation(serialization("-js"))
                // Ktor client
                implementation(ktorClient("core-js"))
                implementation(ktorClient("json-js"))
                implementation(ktorClient("serialization-js"))
                implementation(ktorClient("js"))
            }
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
            .getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("ios")
            .binaries.getFramework(mode)
    inputs.property("mode", mode)

    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)