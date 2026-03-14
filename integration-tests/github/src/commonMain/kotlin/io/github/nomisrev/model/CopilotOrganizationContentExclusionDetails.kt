package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class CopilotOrganizationContentExclusionDetails(val values: List<List<String>>? = null)
