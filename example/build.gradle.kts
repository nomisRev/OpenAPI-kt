plugins {
    java
    id("io.github.nomisrev.openapi.plugin") version "1.0.0"
}

openApiConfig {
    spec.set(file("openai.json"))
}
