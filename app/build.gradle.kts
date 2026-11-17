plugins {
    alias(libs.plugins.android.application)

    // KSP hỗ trợ Room Database
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.vanlang.smartshop"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.vanlang.smartshop"

        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // =====================================
    // 1. ANDROIDX
    // =====================================

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)


    // =====================================
    // 2. RECYCLERVIEW & CARDVIEW
    // =====================================

    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.cardview:cardview:1.0.0")


    // =====================================
    // 3. GLIDE - IMAGE
    // =====================================

    implementation("com.github.bumptech.glide:glide:4.16.0")


    // =====================================
    // 4. MVVM - VIEWMODEL & LIVEDATA
    // =====================================

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:2.11.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:2.11.0"
    )


    // =====================================
    // 5. COROUTINES & STATEFLOW
    // =====================================

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0"
    )


    // =====================================
    // 6. ROOM DATABASE
    // =====================================

    val roomVersion = "2.8.5"

    implementation(
        "androidx.room:room-runtime:$roomVersion"
    )

    implementation(
        "androidx.room:room-ktx:$roomVersion"
    )

    ksp(
        "androidx.room:room-compiler:$roomVersion"
    )

    testImplementation(
        "androidx.room:room-testing:$roomVersion"
    )


    // =====================================
    // 7. RETROFIT - REST API
    // =====================================

    implementation(
        "com.squareup.retrofit2:retrofit:3.0.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:3.0.0"
    )


    // =====================================
    // 8. TEST
    // =====================================

    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )
}
