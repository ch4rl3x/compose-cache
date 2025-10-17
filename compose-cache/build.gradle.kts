import de.charlex.convention.config.configureIosTargets

plugins {
    id("de.charlex.convention.android.library")
    id("de.charlex.convention.kotlin.multiplatform.mobile")
    id("de.charlex.convention.centralPublish")
    id("de.charlex.convention.compose.multiplatform")
}

mavenPublishConfig {
    name = "compose-cache"
    group = "de.charlex.compose"
    description = "A lightweight Jetpack Compose utility that buffers user input locally until the backing state catches up — preventing cursor jumps and lost text when syncing with databases or flows."
    url = "https://github.com/ch4rl3x/compose-cache"

    scm {
        connection = "scm:git:github.com/ch4rl3x/compose-cache.git"
        developerConnection = "scm:git:ssh://github.com/ch4rl3x/compose-cache.git"
        url = "https://github.com/ch4rl3x/compose-cache/tree/main"
    }

    developers {
        developer {
            id = "ch4rl3x"
            name = "Alexander Karkossa"
            email = "alexander.karkossa@googlemail.com"
        }
        developer {
            id = "kalinjul"
            name = "Julian Kalinowski"
            email = "julakali@gmail.com"
        }
    }
}

kotlin {
    configureIosTargets()
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
            }
        }
    }
}