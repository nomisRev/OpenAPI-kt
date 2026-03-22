package io.github.nomisrev.render.test.object_.multi.line

import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
public data class MultiLine(
  public val name: String? = null,
  public val email: Long? = null,
  public val age: Int,
  public val longername: Double?,
  public val longername2: Float? = null,
  @SerialName("longer_name_3")
  public val longerName3: Uuid? = null,
  public val longername4: Instant? = null,
)
