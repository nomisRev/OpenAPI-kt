package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Configuration for code scanning default setup.
 */
@Serializable
public data class CodeScanningDefaultSetupUpdate(
  public val state: State? = null,
  @SerialName("runner_type")
  public val runnerType: RunnerType? = null,
  @SerialName("runner_label")
  public val runnerLabel: String? = null,
  @SerialName("query_suite")
  public val querySuite: QuerySuite? = null,
  @SerialName("threat_model")
  public val threatModel: ThreatModel? = null,
  public val languages: List<Languages>? = null,
) {
  @Serializable
  public enum class Languages(
    public val `value`: String,
  ) {
    @SerialName("actions")
    Actions("actions"),
    @SerialName("c-cpp")
    CCpp("c-cpp"),
    @SerialName("csharp")
    Csharp("csharp"),
    @SerialName("go")
    Go("go"),
    @SerialName("java-kotlin")
    JavaKotlin("java-kotlin"),
    @SerialName("javascript-typescript")
    JavascriptTypescript("javascript-typescript"),
    @SerialName("python")
    Python("python"),
    @SerialName("ruby")
    Ruby("ruby"),
    @SerialName("swift")
    Swift("swift"),
    ;
  }

  @Serializable
  public enum class QuerySuite(
    public val `value`: String,
  ) {
    @SerialName("default")
    Default("default"),
    @SerialName("extended")
    Extended("extended"),
    ;
  }

  @Serializable
  public enum class RunnerType(
    public val `value`: String,
  ) {
    @SerialName("standard")
    Standard("standard"),
    @SerialName("labeled")
    Labeled("labeled"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("configured")
    Configured("configured"),
    @SerialName("not-configured")
    NotConfigured("not-configured"),
    ;
  }

  @Serializable
  public enum class ThreatModel(
    public val `value`: String,
  ) {
    @SerialName("remote")
    Remote("remote"),
    @SerialName("remote_and_local")
    RemoteAndLocal("remote_and_local"),
    ;
  }
}
