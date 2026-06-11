package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Artifact Metadata Deployment Record
 */
@Serializable
public data class ArtifactDeploymentRecord(
  public val id: Long? = null,
  public val digest: String? = null,
  @SerialName("logical_environment")
  public val logicalEnvironment: String? = null,
  @SerialName("physical_environment")
  public val physicalEnvironment: String? = null,
  public val cluster: String? = null,
  @SerialName("deployment_name")
  public val deploymentName: String? = null,
  public val tags: List<String>? = null,
  @SerialName("runtime_risks")
  public val runtimeRisks: List<RuntimeRisks>? = null,
  @SerialName("created_at")
  public val createdAt: String? = null,
  @SerialName("updated_at")
  public val updatedAt: String? = null,
  @SerialName("attestation_id")
  public val attestationId: Long? = null,
) {
  @Serializable
  public enum class RuntimeRisks(
    public val `value`: String,
  ) {
    @SerialName("critical-resource")
    CriticalResource("critical-resource"),
    @SerialName("internet-exposed")
    InternetExposed("internet-exposed"),
    @SerialName("lateral-movement")
    LateralMovement("lateral-movement"),
    @SerialName("sensitive-data")
    SensitiveData("sensitive-data"),
    ;
  }
}
