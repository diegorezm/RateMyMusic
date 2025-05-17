import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    val envProperties = Properties()
    envProperties.load(FileInputStream("env.properties"))

    signingConfigs {
        getByName("debug") {
            storeFile = file(envProperties.getProperty("DEBUG_KEYSTORE_PATH"))
            storePassword = envProperties.getProperty("DEBUG_KEYSTORE_PASSWORD")
            keyAlias = envProperties.getProperty("DEBUG_KEY_ALIAS")
            keyPassword = envProperties.getProperty("DEBUG_KEY_PASSWORD")
        }
        create("release") {
            storeFile = file(envProperties.getProperty("RELEASE_KEYSTORE_PATH"))
            storePassword = envProperties.getProperty("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = envProperties.getProperty("RELEASE_KEY_ALIAS")
            keyPassword = envProperties.getProperty("RELEASE_KEY_PASSWORD")
        }
    }
    namespace = "com.diegorezm.ratemymusic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.diegorezm.ratemymusic"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["redirectHostName"] = "callback"
        manifestPlaceholders["redirectSchemeName"] = "com.diegorezm.ratemymusic"

        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            "\"${envProperties.getProperty("SUPABASE_ANON_KEY")}\""
        )
        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${envProperties.getProperty("SUPABASE_URL")}\""
        )
        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${envProperties.getProperty("GOOGLE_WEB_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "SPOTIFY_CLIENT_ID",
            "\"${envProperties.getProperty("SPOTIFY_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "SPOTIFY_SECRET_KEY",
            "\"${envProperties.getProperty("SPOTIFY_SECRET_KEY")}\""
        )

    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.koin.android)

    implementation(libs.koin.androidx.compose)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.bundles.ktor)

    implementation(platform(libs.supabase.bom))

    implementation(libs.supabase.auth.kt)
    implementation(libs.supabase.postgrest.kt)

    implementation(libs.google.gson)

    implementation(libs.androidx.room.runtime)
    implementation(libs.sqlite.bundled)

    implementation(libs.spotify.auth)
    implementation(libs.androidx.browser)

    implementation(libs.googleid)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    implementation(libs.bundles.coil)

    implementation(libs.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    ksp(libs.androidx.room.compiler)
}
