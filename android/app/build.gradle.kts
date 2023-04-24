plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "eu.euromov.activmotiv"
    compileSdk = 33

    defaultConfig {
        applicationId = "eu.euromov.activmotiv"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "0.5"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled  = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.9")

    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    kapt("androidx.room:room-compiler:${rootProject.extra["room_version"]}")

    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit_version"]}")
    implementation("com.squareup.retrofit2:converter-jackson:${rootProject.extra["retrofit_version"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.work:work-runtime-ktx:${rootProject.extra["work_version"]}")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.navigation:navigation-compose:${rootProject.extra["nav_version"]}")
}