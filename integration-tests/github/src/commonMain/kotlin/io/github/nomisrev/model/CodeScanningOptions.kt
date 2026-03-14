package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class CodeScanningOptions(@SerialName("allow_advanced") val allowAdvanced: Boolean? = null)
