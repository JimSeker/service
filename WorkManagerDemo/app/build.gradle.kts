plugins {
    alias(libs.plugins.android.application)
}
android {
    namespace = "edu.cs4730.workmanagerdemo"
    compileSdk = 37
    defaultConfig {
        applicationId = "edu.cs4730.workmanagerdemo"
        minSdk = 32
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.material)
    implementation(libs.androidx.work.runtime)
}
