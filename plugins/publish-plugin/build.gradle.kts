plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    implementation(libs.plugin.android)
    implementation(libs.plugin.kotlin)
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.name
}

detekt {
    buildUponDefaultConfig = true
    config.from(file("../../detekt.yml"))
}

gradlePlugin {
    plugins {
        create("appyx-publish-android") {
            id = "appyx-publish-android"
            implementationClass = "AndroidAppyxPublishPlugin"
        }
        create("appyx-publish-java") {
            id = "appyx-publish-java"
            implementationClass = "JavaAppyxPublishPlugin"
        }
    }
}
