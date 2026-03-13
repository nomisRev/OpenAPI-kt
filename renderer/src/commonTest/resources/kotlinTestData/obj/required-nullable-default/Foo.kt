package obj.required.nullable.default.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Required

@Serializable
data class Foo(val requiredNullable: String?, @Required val requiredWithDefault: String)
