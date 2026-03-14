package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningDefaultSetup(
    val state: State? = null,
    val languages: List<Languages>? = null,
    @SerialName("runner_type") val runnerType: RunnerType? = null,
    @SerialName("runner_label") val runnerLabel: String? = null,
    @SerialName("query_suite") val querySuite: QuerySuite? = null,
    @SerialName("threat_model") val threatModel: ThreatModel? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    val schedule: Schedule? = null,
) {
    @Serializable
    enum class State {
        @SerialName("configured") Configured, @SerialName("not-configured") NotConfigured;
    }

    @Serializable
    enum class Languages {
        @SerialName("actions")
        Actions,
        @SerialName("c-cpp")
        CCpp,
        @SerialName("csharp")
        Csharp,
        @SerialName("go")
        Go,
        @SerialName("java-kotlin")
        JavaKotlin,
        @SerialName("javascript-typescript")
        JavascriptTypescript,
        @SerialName("javascript")
        Javascript,
        @SerialName("python")
        Python,
        @SerialName("ruby")
        Ruby,
        @SerialName("typescript")
        Typescript,
        @SerialName("swift")
        Swift;
    }

    @Serializable
    enum class RunnerType {
        @SerialName("standard") Standard, @SerialName("labeled") Labeled;
    }

    @Serializable
    enum class QuerySuite {
        @SerialName("default") Default, @SerialName("extended") Extended;
    }

    @Serializable
    enum class ThreatModel {
        @SerialName("remote") Remote, @SerialName("remote_and_local") RemoteAndLocal;
    }

    @Serializable
    enum class Schedule {
        @SerialName("weekly") Weekly;
    }
}
