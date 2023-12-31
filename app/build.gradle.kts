plugins {
    alias(libs.plugins.voicenote.android.application)
    alias(libs.plugins.voicenote.android.application.compose)
    alias(libs.plugins.voicenote.android.hilt)
//    alias(libs.plugins.android.kotlin)
//    alias(libs.plugins.android.hilt)
//    alias(libs.plugins.google.devtools.ksp)
//    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.openai.voicenote"

    defaultConfig {
        applicationId = "com.openai.voicenote"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.jetbrains.kotlin.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.bundles.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // Google Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Compose View Model
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // HILT
    implementation(libs.hilt.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.converter.scalars)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Google Font
    implementation(libs.androidx.ui.text.google.fonts)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Lottie Animation
    implementation(libs.lottie.compose)
}