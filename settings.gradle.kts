rootProject.name = "account"

fun findUserName() = (extra.properties["gpr.user"] as String?).nullWhenEmpty() ?: System.getenv("USERNAME")
fun findToken() = (extra.properties["gpr.key"] as String?).nullWhenEmpty() ?: System.getenv("TOKEN")

fun String?.nullWhenEmpty() = if (this.isNullOrEmpty()) null else this

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ssu-commerce/ssu-commerce-core")
            credentials {
                username = extra.properties["gpr.user"] as String?
                password = extra.properties["gpr.key"] as String?
            }
        }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.contains("ssu.commerce.plugin"))
                useVersion(extra.properties["ssu-commerce-core-plugin"] as String)
        }
    }
}
