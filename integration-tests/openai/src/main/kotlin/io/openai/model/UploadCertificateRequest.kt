package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UploadCertificateRequest(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  public val content: String,
)
