plugins {
    `kotlin-dsl`
}

val avroVersion = "1.12.0"

dependencies {
    compileOnly("org.apache.avro:avro:$avroVersion")
    implementation("com.github.davidmc24.gradle.plugin:gradle-avro-plugin:1.9.1")
}

repositories {
    gradlePluginPortal()
}
