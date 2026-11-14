package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Contributor Activity
 */
@Serializable
public data class ContributorActivity(
  public val author: NullableSimpleUser?,
  public val total: Long,
  public val weeks: List<Weeks>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Weeks(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val w: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val a: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val d: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val c: Long? = null,
  )
}
