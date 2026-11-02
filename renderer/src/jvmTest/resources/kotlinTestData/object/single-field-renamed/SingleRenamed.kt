package io.github.nomisrev.render.test.object_.single.`field`.renamed

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SingleRenamed(
  @SerialName("error_message")
  public val errorMessage: String,
)
