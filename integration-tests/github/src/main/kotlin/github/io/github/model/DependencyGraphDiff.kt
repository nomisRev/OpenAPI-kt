package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A diff of the dependencies between two commits.
 */
@JvmInline
@Serializable
public value class DependencyGraphDiff(
  public val items: List<Item>,
) {
  @Serializable
  public data class Item(
    @SerialName("change_type")
    public val changeType: ChangeType,
    public val manifest: String,
    public val ecosystem: String,
    public val name: String,
    public val version: String,
    @SerialName("package_url")
    public val packageUrl: String?,
    public val license: String?,
    @SerialName("source_repository_url")
    public val sourceRepositoryUrl: String?,
    public val vulnerabilities: List<Vulnerabilities>,
    public val scope: Scope,
  ) {
    @Serializable
    public enum class ChangeType(
      public val `value`: String,
    ) {
      @SerialName("added")
      Added("added"),
      @SerialName("removed")
      Removed("removed"),
      ;
    }

    @Serializable
    public enum class Scope(
      public val `value`: String,
    ) {
      @SerialName("unknown")
      Unknown("unknown"),
      @SerialName("runtime")
      Runtime("runtime"),
      @SerialName("development")
      Development("development"),
      ;
    }

    @Serializable
    public data class Vulnerabilities(
      public val severity: String,
      @SerialName("advisory_ghsa_id")
      public val advisoryGhsaId: String,
      @SerialName("advisory_summary")
      public val advisorySummary: String,
      @SerialName("advisory_url")
      public val advisoryUrl: String,
    )
  }
}
