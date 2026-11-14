package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CvssSeverities(
  @SerialName("cvss_v3")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cvssV3: Optional<CvssV3> = Optional.Absent,
  @SerialName("cvss_v4")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cvssV4: Optional<CvssV4> = Optional.Absent,
) {
  @Serializable
  public data class CvssV3(
    @SerialName("vector_string")
    public val vectorString: String?,
    public val score: Double?,
  )

  @Serializable
  public data class CvssV4(
    @SerialName("vector_string")
    public val vectorString: String?,
    public val score: Double?,
  )
}
