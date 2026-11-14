package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Artifact Metadata Deployment Record
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ArtifactDeploymentRecord(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val digest: String? = null,
  @SerialName("logical_environment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logicalEnvironment: String? = null,
  @SerialName("physical_environment")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val physicalEnvironment: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cluster: String? = null,
  @SerialName("deployment_name")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val deploymentName: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tags: Map<String, String>? = null,
  @SerialName("runtime_risks")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runtimeRisks: List<RuntimeRisks>? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: String? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: String? = null,
  @SerialName("attestation_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attestationId: Optional<Long> = Optional.Absent,
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
