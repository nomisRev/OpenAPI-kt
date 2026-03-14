package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ActionsCanApprovePullRequestReviews(val value: Boolean)
