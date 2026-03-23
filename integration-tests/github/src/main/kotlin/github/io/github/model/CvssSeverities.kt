package io.github.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CvssSeverities(
  @SerialName("cvss_v3")
  public val cvssV3: CvssV3? = null,
  @SerialName("cvss_v4")
  public val cvssV4: CvssV4? = null,
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
