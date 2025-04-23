import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java-application-conventions")
    id("kafka-java-conventions")
    id("com.github.davidmc24.gradle.plugin.avro")
    id("com.github.ben-manes.versions") version "0.52.0"
    id("com.gradleup.shadow") version "8.3.6"
}

val avroVersion = "1.12.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    compileOnly("org.apache.avro:avro-tools:$avroVersion")
}

avro {
    setCreateSetters(false)
}

// Used by dependency update plugin
fun String.isNonStable(): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA", "ccs").any { uppercase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(this)
  return isStable.not()
}

// disallow release candidates as upgradable versions from stable versions
tasks.withType<DependencyUpdatesTask> {
  resolutionStrategy {
    componentSelection {
      all {
        if (candidate.version.isNonStable() && !currentVersion.isNonStable()) {
          reject("Release candidate")
        }
      }
    }
  }
}

tasks.shadowJar {
  archiveVersion = "7.2.1"
}


tasks.shadowJar {
    //configurations = project.configurations.compileClasspath.map { listOf(it) }
    mergeServiceFiles()
    archiveBaseName.set("kafka-producer")
    manifest {
        attributes(mapOf("Main-Class" to "io.confluent.developer.KafkaAvroProducerApplication"))
    }
}

application {
  mainClass = "io.confluent.developer.KafkaAvroProducerApplication"
  // Optionally, you can add default JVM arguments to the start scripts like this:
  //applicationDefaultJvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

tasks.runShadow {
  args("../configuration/dev.properties", "../input.txt")
}

// tasks {
//     named<ShadowJar>("shadowJar") {
//         archiveBaseName.set("kafka-producer")
//         mergeServiceFiles()
//         manifest {
//             attributes(mapOf("Main-Class" to "io.confluent.developer.KafkaAvroProducerApplication"))
//         }
//     }
// }

// tasks {
//     build {
//         dependsOn(shadowJar)
//     }
// }
