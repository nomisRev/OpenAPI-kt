package io.github.nomisrev.openapi

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * A map of possible out-of band callbacks related to the parent operation. Each value in the map is
 * a [PathItem] Object that describes a set of requests that may be initiated by the API provider
 * and the expected responses. The key value used to identify the path item object is an expression,
 * evaluated at runtime, that identifies a URL to use for the callback operation.
 */
@Serializable @JvmInline public value class Callback(public val value: Map<String, PathItem>)
