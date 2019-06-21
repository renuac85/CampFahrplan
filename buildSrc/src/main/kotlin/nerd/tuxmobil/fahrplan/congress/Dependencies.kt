@file:Suppress("unused")

package nerd.tuxmobil.fahrplan.congress

object Android {
    const val buildToolsVersion = "28.0.3"
    const val compileSdkVersion = 28
    const val minSdkVersion = 14
    const val targetSdkVersion = 27
}

private const val kotlinVersion = "1.3.40"

object GradlePlugins {

    private object Versions {
        const val androidGradle = "3.3.2"
        const val gradleVersions = "0.21.0"
        const val proguardGradle = "6.1.1"
        const val sonarQubeGradle = "2.7.1"
    }

    const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
    const val proguardGradle = "net.sf.proguard:proguard-gradle:${Versions.proguardGradle}"
    const val sonarQubeGradle = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${Versions.sonarQubeGradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
}

object Libs {

    private object Versions {
        const val assertjAndroid = "1.2.0"
        const val emailIntentBuilder = "1.0.0"
        const val espresso = "3.0.2"
        const val junit = "4.12"
        const val kotlinCoroutines = "1.2.2"
        const val kotlinCoroutinesRetrofit = "1.1.0"
        const val mockito = "2.27.0"
        const val mockitoKotlin = "2.1.0"
        const val okhttp = "3.12.3"
        const val sessionize = "2.0.0"
        const val snackengage = "0.19"
        const val supportLibrary = "27.1.1"
        const val testRules = "1.0.2"
        const val threeTenBp = "1.4.0"
        const val tracedroid = "1.4"
    }

    const val assertjAndroid = "com.squareup.assertj:assertj-android:${Versions.assertjAndroid}"
    const val emailIntentBuilder = "de.cketti.mailto:email-intent-builder:${Versions.emailIntentBuilder}"
    const val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    const val junit = "junit:junit:${Versions.junit}"
    const val kotlinCoroutinesRetrofit = "ru.gildor.coroutines:kotlin-coroutines-retrofit:${Versions.kotlinCoroutinesRetrofit}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    const val sessionizeBase = "info.metadude.kotlin.library.sessionize:sessionize-base:${Versions.sessionize}"
    const val snackengagePlayrate = "com.github.ligi.snackengage:snackengage-playrate:${Versions.snackengage}"
    const val supportLibraryAnnotations = "com.android.support:support-annotations:${Versions.supportLibrary}"
    const val supportLibraryAppcompatV7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    const val supportLibraryDesign = "com.android.support:design:${Versions.supportLibrary}"
    const val testRules = "com.android.support.test:rules:${Versions.testRules}"
    const val threeTenBp = "org.threeten:threetenbp:${Versions.threeTenBp}"
    const val tracedroid = "org.ligi:tracedroid:${Versions.tracedroid}"
}
