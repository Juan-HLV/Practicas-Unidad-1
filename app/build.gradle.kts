plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.exame_u3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.exame_u3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true // Keeping this since existing Fragments use it
    }

    // Since we are using the alias(libs.plugins.kotlin.compose), the compiler version is often managed by the plugin version
    // explicitly or implicitly. However, user asked for:
    // composeOptions { kotlinCompilerExtensionVersion "1.5.15" }
    // BUT 'kotlinCompilerExtensionVersion' is deprecated/ignored when using the KGP 2.0+ or the new Compose Compiler Gradle Plugin.
    // Use the prompt's fallback logic or valid configuration. The user asks for it.
    // If we use `alias(libs.plugins.kotlin.compose)` (which maps to org.jetbrains.kotlin.plugin.compose),
    // we don't set kotlinCompilerExtensionVersion. 
    // IF we were using the old way, we would.
    // Given the prompt "Asegúrate de que la versión de Kotlin y del plugin Android sean compatibles", 
    // and the prompt explicitly asks for `composeOptions`, I will include it, but with a warning or just include it if using old AGP/Kotlin.
    // Actually, looking at the initial state (Step 0), it had `alias(libs.plugins.kotlin.compose)`.
    // I will include the block but if using K2/Compose Plugin it might be redundant. 
    // Let's assume standard behavior for modern projects.
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    
    // Compose
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    
    implementation("androidx.compose.ui:ui-viewbinding")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // Legacy/Hybrid Dependencies (keeping for now as per prompt "Configura...", potentially keeping app logic)
    implementation("androidx.appcompat:appcompat:1.6.1") // Needed for Theme.AppCompat if used
    implementation("com.google.android.material:material:1.11.0") // Needed for Material Components
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}