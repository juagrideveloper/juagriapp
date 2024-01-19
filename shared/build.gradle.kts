plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.0"
    id("kotlin-parcelize")
    id("app.cash.sqldelight")
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

                implementation(libs.ktor.core)
                implementation(libs.ktor.logs)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization)

                api(libs.firebase.firestore)
                api(libs.firebase.common)

                api(libs.koin.core)
                api(libs.koin.compose)

                api(libs.precompose)
                api(libs.precompose.viewmodel)
                api(libs.precompose.koin)
                //api(libs.kamel.image)
                implementation(libs.sqldelight.coroutines)
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
                implementation("androidx.camera:camera-lifecycle:1.3.0")
                implementation("io.coil-kt:coil-compose:2.2.1")
                implementation(libs.sqldelight.android)
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
                implementation(libs.ktor.ios)
                implementation(libs.sqldelight.ios)
                implementation(libs.touchlab.state)
            }
        }
    }
}

sqldelight {
    databases {
        create("JUDatabase") {
            packageName.set("com.juagri.shared")
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
