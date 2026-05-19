plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("quiket.android.library")
    id("quiket.android.compose")
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.f1.quiket.feature.floating"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-core")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.foundation)
    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.metadata.jvm)

    debugImplementation(libs.androidx.compose.ui.tooling)
}