package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization roles
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationRole(
  public val id: Long,
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("base_role")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val baseRole: Optional<BaseRole> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val source: Optional<Source> = Optional.Absent,
  public val permissions: List<String>,
  public val organization: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  @Serializable
  public enum class BaseRole(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("triage")
    Triage("triage"),
    @SerialName("write")
    Write("write"),
    @SerialName("maintain")
    Maintain("maintain"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class Source {
    Organization,
    Enterprise,
    Predefined,
  }
}
