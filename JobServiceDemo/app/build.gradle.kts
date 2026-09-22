plugins {
    alias(libs.plugins.android.application)
}
android {
    namespace = "edu.cs4730.jobservicedemo"
    compileSdk = 37
    defaultConfig {
        applicationId = "edu.cs4730.jobservicedemo"
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
                "proguard-rules.txt"
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
    implementation(libs.material)
}
