import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
}

apply(from = "$rootDir/gradle/eclipse.gradle.kts")
apply(from = "$rootDir/gradle/idea.gradle.kts")

sourceSets {
    main {
        resources.setSrcDirs(kotlin.sourceDirectories)
    }
}

dependencies {
    implementation(libs.junit.api)

    testImplementation(libs.bundles.kotest)
    testRuntimeOnly(libs.kotest.runner)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.javaLanguageCompatibility.get())
    }
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = false
        jvmTarget = JvmTarget.fromTarget(libs.versions.javaLanguageCompatibility.get())
    }
}

tasks.test.configure {
    useJUnitPlatform()

    jvmArgs("-Xmx8G")
}
