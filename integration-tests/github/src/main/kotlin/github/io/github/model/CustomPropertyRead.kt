package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Custom property defined on an organization
 */
@Serializable
public data class CustomPropertyRead(
  @SerialName("property_name")
  public val propertyName: String,
  public val url: String? = null,
  @SerialName("source_type")
  public val sourceType: SourceType? = null,
  @SerialName("value_type")
  public val valueType: ValueType,
  public val required: Boolean? = null,
  @SerialName("default_value")
  public val defaultValue: DefaultValue? = null,
  public val description: String? = null,
  @SerialName("allowed_values")
  public val allowedValues: List<String>? = null,
  @SerialName("values_editable_by")
  public val valuesEditableBy: ValuesEditableBy? = null,
  @SerialName("require_explicit_values")
  public val requireExplicitValues: Boolean? = null,
) {
  /**
   * Default value of the property
   */
  @Serializable(with = DefaultValue.Serializer::class)
  public sealed interface DefaultValue {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : DefaultValue

    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : DefaultValue

    public object Serializer : KSerializer<DefaultValue> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.CustomPropertyRead.DefaultValue", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): DefaultValue {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: DefaultValue) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class SourceType(
    public val `value`: String,
  ) {
    @SerialName("organization")
    Organization("organization"),
    @SerialName("enterprise")
    Enterprise("enterprise"),
    ;
  }

  @Serializable
  public enum class ValueType(
    public val `value`: String,
  ) {
    @SerialName("string")
    String("string"),
    @SerialName("single_select")
    SingleSelect("single_select"),
    @SerialName("multi_select")
    MultiSelect("multi_select"),
    @SerialName("true_false")
    TrueFalse("true_false"),
    @SerialName("url")
    Url("url"),
    ;
  }

  @Serializable
  public enum class ValuesEditableBy(
    public val `value`: String,
  ) {
    @SerialName("org_actors")
    OrgActors("org_actors"),
    @SerialName("org_and_repo_actors")
    OrgAndRepoActors("org_and_repo_actors"),
    ;
  }
}
