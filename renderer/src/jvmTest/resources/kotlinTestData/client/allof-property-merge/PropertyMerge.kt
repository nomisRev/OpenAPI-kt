package io.github.nomisrev.render.test.client.allof.`property`.merge

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PropertyMerge(
  public val nullableThenNonNull: String,
  public val nonNullThenNullable: String,
  public val bothNullable: String?,
  public val createdAt: Instant,
  public val status: Status,
  public val pet: Dog,
  public val owner: NullableOwner?,
  public val name: MyString,
  public val ownerWithExtra: OwnerWithExtra,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val label: String? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnerWithExtra(
    public val id: Long,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val extra: String? = null,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("inactive")
    Inactive("inactive"),
    ;
  }
}
