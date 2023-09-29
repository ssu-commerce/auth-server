rootProject.name = "auth"

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
}
