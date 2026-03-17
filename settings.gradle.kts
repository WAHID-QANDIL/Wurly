import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                val localTokenProvider = providers.provider {
                    val propsFile = rootDir.resolve("secrets.properties")
                    if (!propsFile.exists()) return@provider null
                    propsFile.inputStream().use { stream ->
                        Properties().apply { load(stream) }.getProperty("MAPBOX_DOWNLOADS_TOKEN")
                    }
                }
                password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN")
                    .orElse(providers.environmentVariable("MAPBOX_DOWNLOADS_TOKEN"))
                    .orElse(localTokenProvider)
                    .orElse("")
                    .get()
            }
        }
    }
}
rootProject.name = "Wurly"
include(":app")
include(":glass")