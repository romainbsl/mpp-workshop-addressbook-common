plugins {
    kotlin("multiplatform") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    `maven-publish`
}

group = "com.mybusiness"
version = "3.0.0"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
}

val iosDevHack: String? by project
val iosDevHackEnabled = iosDevHack == "true"

kotlin {
    jvm("android") {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    val iosTargets =
            if (iosDevHackEnabled) listOf(iosX64("ios"))
            else listOf(iosX64(), iosArm64(), iosArm32())
    configure(iosTargets) {
        binaries {
            framework {
                baseName = "AddressBookCommon"
            }
        }
    }

    js { browser() }

    sourceSets {
        // Versions
        val ktorVersion = "1.2.5"
        val coroutinesVersion = "1.3.1"
        val serializationVersion = "0.13.0"
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
                // Kodein-DI
                implementation("org.kodein.di:kodein-di-erased:6.4.1")
                // Klock
                implementation ("com.soywiz.korlibs.klock:klock:1.7.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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

        if (!iosDevHackEnabled) {
            create("iosMain").apply {
                dependsOn(commonMain)
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

        if (!iosDevHackEnabled) {
            create("iosTest").apply {
                dependsOn(commonTest)
                dependsOn(iosMain)
            }
        }

        val iosTest by getting {}

        if (!iosDevHackEnabled) {
            listOf("iosX64", "iosArm64", "iosArm32").forEach {
                getByName("${it}Main") {
                    dependsOn(iosMain)
                }
                getByName("${it}Test") {
                    dependsOn(iosTest)
                }
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
            .getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("iosX64")
            .binaries.getFramework(mode)
    inputs.property("mode", mode)

    dependsOn(framework.linkTask)

    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)