package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The aggregated code interpreter sessions usage details of the specific time bucket.
 */
@Serializable
public data class UsageCodeInterpreterSessionsResult(
  public val `object`: Object,
  @SerialName("num_sessions")
  public val numSessions: Long? = null,
  @SerialName("project_id")
  public val projectId: String? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.usage.code_interpreter_sessions.result")
    OrganizationUsageCodeInterpreterSessionsResult("organization.usage.code_interpreter_sessions.result"),
    ;
  }
}
