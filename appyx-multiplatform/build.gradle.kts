plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("commonMain")

            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(project(":libraries:core"))
            }
        }
    }
}
