package io.github.nomisrev.render.test.client.union.nullable.member

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
public sealed interface Optional<out T> {
  /**
   * The value, or `null` for [Absent] and [Null].
   */
  public val valueOrNull: T?
    get() = (this as? Value<T>)?.value

  public data object Absent : Optional<Nothing>

  public data object Null : Optional<Nothing>

  public data class Value<out T>(
    public val `value`: T,
  ) : Optional<T>
}

public class OptionalSerializer<T>(
  private val valueSerializer: KSerializer<T>,
) : KSerializer<Optional<T>> {
  @OptIn(ExperimentalSerializationApi::class)
  override val descriptor: SerialDescriptor = valueSerializer.descriptor.nullable

  override fun serialize(encoder: Encoder, `value`: Optional<T>) {
    when (value) {
      Optional.Absent -> throw SerializationException("Optional.Absent has no wire form: it is only valid as the value of an @EncodeDefault(NEVER) property, which leaves the key out")
      Optional.Null -> encoder.encodeNull()
      is Optional.Value -> encoder.encodeSerializableValue(valueSerializer, value.value)
    }
  }

  override fun deserialize(decoder: Decoder): Optional<T> = if (decoder.decodeNotNullMark()) {
    Optional.Value(decoder.decodeSerializableValue(valueSerializer))
  } else {
    decoder.decodeNull()
    Optional.Null
  }
}
