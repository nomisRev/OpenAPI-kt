package obj.single.line.model

import kotlinx.serialization.Serializable

@Serializable
data class Foo(val name: String? = null, val email: Long? = null, val age: Int, val longername: Double?)
