package obj.multi.line.model

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Foo(
    val name: String? = null,
    val email: Long? = null,
    val age: Int,
    val longername: Double?,
    val longername2: Float? = null,
    @SerialName("longer_name_3") val longerName3: Uuid? = null,
    val longername4: LocalDateTime? = null,
)
