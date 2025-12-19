plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "jorgemorte.com.dilemaapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "jorgemorte.com.dilemaapp"
        minSdk = 24
        targetSdk = 35
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
    // ESTO ES CORRECTO Y NECESARIO PARA BASES DE DATOS PRE-EXISTENTES
    aaptOptions {
        noCompress.add("db")
        noCompress.add("sqlite")
    }
}

dependencies {

    // 1. SQLCipher
    implementation ("net.zetetic:android-database-sqlcipher:4.5.4")

    // Otras librerías
    implementation("com.github.yuyakaido:CardStackView:v2.3.4")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // !!!!!!!!!!!!!!! EPENDENCIAS DE PRUEBA
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation("androidx.sqlite:sqlite:2.3.1")


    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("com.google.android.material:material:1.13.0")
}