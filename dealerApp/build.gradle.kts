plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.fragment.ktx)
                implementation(compose.material3)
                implementation(project(":shared"))
                implementation(project(":decomposerouter"))

                implementation(platform("com.google.firebase:firebase-bom:31.3.0"))
                implementation("com.google.firebase:firebase-firestore-ktx")
                implementation("com.google.firebase:firebase-crashlytics")
                implementation("com.google.firebase:firebase-analytics")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.juagri.judealer"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = namespace
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
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
}
