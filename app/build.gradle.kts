import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("quiket.android.application")
    id("quiket.android.compose")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use(::load)
    }
}

fun localProperty(
    name: String,
    defaultValue: String,
): String = localProperties.getProperty(name)
    ?.takeIf(String::isNotBlank)
    ?: defaultValue

fun String.asBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

val quiketApiBaseUrl = localProperty(
    name = "quiket.api.baseUrl",
    defaultValue = "http://43.201.222.243:8080/api/v1/",
)
val releaseQuiketApiBaseUrl = localProperty(
    name = "quiket.api.baseUrl",
    defaultValue = "https://quiket.invalid/api/v1/",
)
val kakaoNativeAppKey = localProperty(
    name = "kakao.native.app.key",
    defaultValue = "",
)

android {
    namespace = "com.f1.quiket"

    defaultConfig {
        applicationId = "com.f1.quiket"
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "QUIKET_API_BASE_URL", quiketApiBaseUrl.asBuildConfigString())
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", kakaoNativeAppKey.asBuildConfigString())
        manifestPlaceholders["kakaoRedirectScheme"] = "kakao$kakaoNativeAppKey"
    }

    buildTypes {
        release {
            buildConfigField("String", "QUIKET_API_BASE_URL", releaseQuiketApiBaseUrl.asBuildConfigString())
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:session"))
    implementation(project(":feature:login"))
    implementation(project(":feature:main"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:splash"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.hilt.android)
    implementation(libs.kakao.v2.user)
    implementation(libs.timber)

    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
