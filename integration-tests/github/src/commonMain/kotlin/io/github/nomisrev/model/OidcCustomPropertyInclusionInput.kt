package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class OidcCustomPropertyInclusionInput(@SerialName("custom_property_name") val customPropertyName: String)
