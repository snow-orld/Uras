plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tuanjie.client"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tuanjie.client"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        aidl = true
    }

    aaptOptions {
        noCompress += arrayOf(".tj3d", ".ress", ".resource", ".obb", ".bundle", ".tuanjieexp", "global-metadata.so")
//        noCompress += tuanjieStreamingAssets.tokenize(", ")
        ignoreAssetsPattern = "!.svn:!.git:!.ds_store:!*.scc:!CVS:!thumbs.db:!picasa.ini:!*~"
    }
}

dependencies {

    implementation(project(":renderServiceLibrary"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}