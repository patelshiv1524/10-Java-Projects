plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.restaurantguideapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.restaurantguideapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Core Android Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    //Google API's Dependencies For Google Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.libraries.places:places:2.6.0")


    // Room Database Dependencies for Java
    implementation("androidx.room:room-runtime:2.5.0")
    implementation(libs.gridlayout)
    annotationProcessor("androidx.room:room-compiler:2.5.0")

    // RecyclerView Dependency
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.fragment:fragment:1.5.7")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Test Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}