plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.0"
    id("kotlin-parcelize")
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":decomposerouter"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.essenty.parcelable)
                implementation(libs.decompose)
                implementation(libs.decompose.compose.multiplatform)

                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("dev.gitlive:firebase-firestore:1.8.1") // This line
                implementation("dev.gitlive:firebase-common:1.8.1")// This line
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                implementation("io.insert-koin:koin-core:3.2.0")
            }
        }
        val androidMain by getting {
            dependencies {
                /*api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")*/

                implementation(compose.material3)
                implementation(libs.decompose)
                implementation(libs.decompose.compose.multiplatform)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.fragment.ktx)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.3.5")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.juagri.shared"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    flavorDimensions += listOf("appMode")
    productFlavors {
        create("cdo") {
            dimension = "appMode"
        }
        create("dealer") {
            dimension = "appMode"
        }
        create("staff") {
            dimension = "appMode"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
