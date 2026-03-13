// Shared configuration for integration test modules
// Each submodule tests code generation + compilation for a large OpenAPI spec

plugins {
    base
}

// Common task to generate all integration test clients
tasks.register("generateAllClients") {
    group = "openapi"
    description = "Generate clients for all integration test specs"
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("generateClient") })
}

// Common task to compile all integration test clients
tasks.register("compileAllClients") {
    group = "openapi"
    description = "Compile all generated integration test clients"
    dependsOn(subprojects.map { it.tasks.named("compileKotlin") })
}
