package io.github.nomisrev.render.test.client.reference.siblings

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Request(
  public val amount: Amount,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previous: Amount? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maybe: Optional<Amount> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nullableRef: Optional<Amount> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val wrongType: Amount? = null,
  public val strict: Strict,
) {
  @Serializable
  public data class Strict(
    public val `value`: Long,
  )
}
