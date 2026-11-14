package io.github.nomisrev.render.test.client.optional.name.collision

import kotlin.Nothing
import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A property that is optional and nullable has three states on the wire: [Absent] (no key), [Null] (`null`) and [Value].
 */
@Serializable(with = OptionalSerializer::class)
public sealed interface OptionalProperty<out T> {
  /**
   * The value, or `null` for [Absent] and [Null].
   */
  public val valueOrNull: T?
    get() = (this as? Value<T>)?.value

  public data object Absent : OptionalProperty<Nothing>

  public data object Null : OptionalProperty<Nothing>

  public data class Value<out T>(
    public val `value`: T,
  ) : OptionalProperty<T>
}

public class OptionalSerializer<T>(
  private val valueSerializer: KSerializer<T>,
) : KSerializer<OptionalProperty<T>> {
  @OptIn(ExperimentalSerializationApi::class)
  override val descriptor: SerialDescriptor = valueSerializer.descriptor.nullable

  override fun serialize(encoder: Encoder, `value`: OptionalProperty<T>) {
    when (value) {
      OptionalProperty.Absent -> throw SerializationException("OptionalProperty.Absent has no wire form: it is only valid as the value of an @EncodeDefault(NEVER) property, which leaves the key out")
      OptionalProperty.Null -> encoder.encodeNull()
      is OptionalProperty.Value -> encoder.encodeSerializableValue(valueSerializer, value.value)
    }
  }

  override fun deserialize(decoder: Decoder): OptionalProperty<T> = if (decoder.decodeNotNullMark()) {
    OptionalProperty.Value(decoder.decodeSerializableValue(valueSerializer))
  } else {
    decoder.decodeNull()
    OptionalProperty.Null
  }
}
