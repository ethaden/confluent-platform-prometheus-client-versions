plugins {
    id("java-common-conventions")
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:7.9.0-ce")
    implementation("io.confluent:kafka-streams-avro-serde:7.9.0")
    //runtimeOnly("io.confluent:kafka-schema-rules:7.9.0")
}
