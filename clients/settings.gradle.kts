// Central versions are defined in "gradle.properties" now
pluginManagement {
  val gradlePluginAvroVersion: String by settings
  val gradlePluginDependencyUpdatesVersion: String by settings
  val gradlePluginShadowJarVersion: String by settings
  plugins {
    id("com.github.davidmc24.gradle.plugin.avro") version "${gradlePluginAvroVersion}"
    id("com.github.ben-manes.versions") version "${gradlePluginDependencyUpdatesVersion}"
    id("com.gradleup.shadow") version "${gradlePluginShadowJarVersion}"
  }
}

rootProject.name = "kafka-producer"

include(
    "producer-7.2.1",
    "producer-latest"
)
