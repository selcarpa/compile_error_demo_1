import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget



plugins {
    kotlin("multiplatform") version "1.9.24"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}


kotlin {
    applyDefaultHierarchyTemplate()


    linuxX64 {
        config()
    }
//    mingwX64 {
//        config()
//    }
//    linuxArm64 {
//        config()
//        setupNativeConfig()
//    }
    jvm {
        withJava()
        val jvmJar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            doFirst {
                manifest {
                    attributes["Main-Class"] = "MainKt"
                }
                from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("net.peanuuutz.tomlkt:tomlkt:0.3.7")
            }
        }
        val nativeMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
    }
}
fun KotlinNativeTarget.config(custom: Executable.() -> Unit = {}) {
    binaries {
        executable {
            entryPoint = "main"
            custom()
        }
    }
}
