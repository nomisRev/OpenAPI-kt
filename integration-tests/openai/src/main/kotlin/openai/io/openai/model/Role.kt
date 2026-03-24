package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Details about a role that can be assigned through the public Roles API.
 */
@Serializable
public data class Role(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  public val description: String?,
  public val permissions: List<String>,
  @SerialName("resource_type")
  public val resourceType: String,
  @SerialName("predefined_role")
  public val predefinedRole: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("role")
    Role("role"),
    ;
  }
}
