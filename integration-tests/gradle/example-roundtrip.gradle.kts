// Wires the example round-trip test (see ../README.md) into a generated-client project.
//
// The applying project sets, before `apply(from = ...)`:
//   extra["exampleRoundTrip.spec"]         the spec file the client is generated from (File)
//   extra["exampleRoundTrip.modelPackage"] the generator's modelPackage
//   extra["exampleRoundTrip.apiPackage"]   the generator's apiPackage
// Run: ./gradlew :<project>:test       Accept the findings: ./gradlew :<project>:test -PupdateRoundTripBaseline=true

val spec = extra["exampleRoundTrip.spec"] as File
val modelPackage = extra["exampleRoundTrip.modelPackage"] as String
val apiPackage = extra["exampleRoundTrip.apiPackage"] as String
val baseline = layout.projectDirectory.file("example-roundtrip-baseline.json").asFile
val reportDir = layout.buildDirectory.dir("reports/example-roundtrip").get().asFile
val updateBaseline = providers.gradleProperty("updateRoundTripBaseline").orElse("false")

dependencies {
    "testImplementation"(project(":example-roundtrip"))
    "testImplementation"(kotlin("test"))
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    inputs.file(spec).withPropertyName("exampleRoundTripSpec").withPathSensitivity(PathSensitivity.NONE)
    inputs.files(baseline).withPropertyName("exampleRoundTripBaseline").withPathSensitivity(PathSensitivity.NONE).optional()
    systemProperty("roundtrip.project", project.name)
    systemProperty("roundtrip.spec", spec.absolutePath)
    systemProperty("roundtrip.modelPackage", modelPackage)
    systemProperty("roundtrip.apiPackage", apiPackage)
    systemProperty("roundtrip.baseline", baseline.absolutePath)
    systemProperty("roundtrip.reportDir", reportDir.absolutePath)
    systemProperty("roundtrip.updateBaseline", updateBaseline.get())
    // Rewriting the baseline is a side effect that the build cache must not replay or skip.
    if (updateBaseline.get().toBoolean()) outputs.upToDateWhen { false }
    testLogging { showStandardStreams = true; exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL }
}
