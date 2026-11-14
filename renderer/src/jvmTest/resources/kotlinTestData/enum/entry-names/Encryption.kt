package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Encryption(
  public val `value`: String,
) {
  @SerialName("aws:kms")
  AwsColonKms("aws:kms"),
  @SerialName("aws-kms")
  AwsMinusKms("aws-kms"),
  ;
}
