package io.github.nomisrev.render.test.union.name.collisions

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Collisions(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val transitionId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mixed: Mixed? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val result: Result? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val titled: Titled? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val positional: Positional? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val accepted: Accepted? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val files: Map<String, Files?>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val contact: Contact? = null,
) {
  /**
   * one
   */
  @OptIn(
    ExperimentalUuidApi::class,
    ExperimentalSerializationApi::class,
  )
  @Serializable
  public data class Accepted(
    @SerialName("message_uuid")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val messageUuid: Uuid? = null,
  )

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Files(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val filename: String? = null,
  )

  @Serializable(with = Mixed.Serializer::class)
  public sealed interface Mixed {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Mixed

    @Serializable
    @JvmInline
    public value class CaseInt(
      public val `value`: Int,
    ) : Mixed

    public object Serializer : KSerializer<Mixed> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.union.name.collisions.Collisions.Mixed", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseInt", Int.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Mixed {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
            value,
            CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Mixed) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
        }
      }
    }
  }

  @Serializable(with = Positional.Serializer::class)
  public sealed interface Positional {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Value1(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `value`: String? = null,
    ) : Positional

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Value2(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `value`: Int? = null,
    ) : Positional

    public object Serializer : KSerializer<Positional> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.union.name.collisions.Collisions.Positional", PolymorphicKind.SEALED) {
        element("Value1", Value1.serializer().descriptor)
        element("Value2", Value2.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Positional {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Value1::class to { decodeFromJsonElement(Value1.serializer(), it) },
          Value2::class to { decodeFromJsonElement(Value2.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Positional) {
        when(value) {
          is Value1 -> encoder.encodeSerializableValue(Value1.serializer(), value)
          is Value2 -> encoder.encodeSerializableValue(Value2.serializer(), value)
        }
      }
    }
  }

  @Serializable(with = Result.Serializer::class)
  public sealed interface Result {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Succeeded(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val status: Status? = null,
    ) : Result {
      @Serializable
      public enum class Status(
        public val `value`: String,
      ) {
        @SerialName("succeeded")
        Succeeded("succeeded"),
        ;
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class FailedOrRejected(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val status: Status? = null,
    ) : Result {
      @Serializable
      public enum class Status(
        public val `value`: String,
      ) {
        @SerialName("failed")
        Failed("failed"),
        @SerialName("rejected")
        Rejected("rejected"),
        ;
      }
    }

    public object Serializer : KSerializer<Result> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.union.name.collisions.Collisions.Result", PolymorphicKind.SEALED) {
        element("Succeeded", Succeeded.serializer().descriptor)
        element("FailedOrRejected", FailedOrRejected.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Result {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          Succeeded::class to { decodeFromJsonElement(Succeeded.serializer(), it) },
          FailedOrRejected::class to { decodeFromJsonElement(FailedOrRejected.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Result) {
        when(value) {
          is Succeeded -> encoder.encodeSerializableValue(Succeeded.serializer(), value)
          is FailedOrRejected -> encoder.encodeSerializableValue(FailedOrRejected.serializer(), value)
        }
      }
    }
  }

  @Serializable(with = Titled.Serializer::class)
  public sealed interface Titled {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class First(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `value`: String? = null,
    ) : Titled

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Second(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `value`: Int? = null,
    ) : Titled

    public object Serializer : KSerializer<Titled> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.union.name.collisions.Collisions.Titled", PolymorphicKind.SEALED) {
        element("First", First.serializer().descriptor)
        element("Second", Second.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Titled {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          First::class to { decodeFromJsonElement(First.serializer(), it) },
          Second::class to { decodeFromJsonElement(Second.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: Titled) {
        when(value) {
          is First -> encoder.encodeSerializableValue(First.serializer(), value)
          is Second -> encoder.encodeSerializableValue(Second.serializer(), value)
        }
      }
    }
  }
}
