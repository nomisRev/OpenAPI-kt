package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * How the model should select which tool (or tools) to use when generating
 * a response. See the `tools` parameter to see how to specify which tools
 * the model can call.
 *
 */
@Serializable(with = ToolChoiceParam.Serializer::class)
public sealed interface ToolChoiceParam {
  @Serializable
  @JvmInline
  public value class CaseToolChoiceOptions(
    public val `value`: ToolChoiceOptions,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseToolChoiceAllowed(
    public val `value`: ToolChoiceAllowed,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseToolChoiceTypes(
    public val `value`: ToolChoiceTypes,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseToolChoiceFunction(
    public val `value`: ToolChoiceFunction,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseToolChoiceMCP(
    public val `value`: ToolChoiceMCP,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseToolChoiceCustom(
    public val `value`: ToolChoiceCustom,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseSpecificApplyPatchParam(
    public val `value`: SpecificApplyPatchParam,
  ) : ToolChoiceParam

  @Serializable
  @JvmInline
  public value class CaseSpecificFunctionShellParam(
    public val `value`: SpecificFunctionShellParam,
  ) : ToolChoiceParam

  public object Serializer : KSerializer<ToolChoiceParam> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ToolChoiceParam", PolymorphicKind.SEALED) {
      element("CaseToolChoiceOptions", ToolChoiceOptions.serializer().descriptor)
      element("CaseToolChoiceAllowed", ToolChoiceAllowed.serializer().descriptor)
      element("CaseToolChoiceTypes", ToolChoiceTypes.serializer().descriptor)
      element("CaseToolChoiceFunction", ToolChoiceFunction.serializer().descriptor)
      element("CaseToolChoiceMCP", ToolChoiceMCP.serializer().descriptor)
      element("CaseToolChoiceCustom", ToolChoiceCustom.serializer().descriptor)
      element("CaseSpecificApplyPatchParam", SpecificApplyPatchParam.serializer().descriptor)
      element("CaseSpecificFunctionShellParam", SpecificFunctionShellParam.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ToolChoiceParam {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseToolChoiceOptions::class to { CaseToolChoiceOptions(decodeFromJsonElement(ToolChoiceOptions.serializer(), it)) },
        CaseToolChoiceAllowed::class to { CaseToolChoiceAllowed(decodeFromJsonElement(ToolChoiceAllowed.serializer(), it)) },
        CaseToolChoiceTypes::class to { CaseToolChoiceTypes(decodeFromJsonElement(ToolChoiceTypes.serializer(), it)) },
        CaseToolChoiceFunction::class to { CaseToolChoiceFunction(decodeFromJsonElement(ToolChoiceFunction.serializer(), it)) },
        CaseToolChoiceMCP::class to { CaseToolChoiceMCP(decodeFromJsonElement(ToolChoiceMCP.serializer(), it)) },
        CaseToolChoiceCustom::class to { CaseToolChoiceCustom(decodeFromJsonElement(ToolChoiceCustom.serializer(), it)) },
        CaseSpecificApplyPatchParam::class to { CaseSpecificApplyPatchParam(decodeFromJsonElement(SpecificApplyPatchParam.serializer(), it)) },
        CaseSpecificFunctionShellParam::class to { CaseSpecificFunctionShellParam(decodeFromJsonElement(SpecificFunctionShellParam.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ToolChoiceParam) {
      when(value) {
        is CaseToolChoiceOptions -> encoder.encodeSerializableValue(ToolChoiceOptions.serializer(), value.value)
        is CaseToolChoiceAllowed -> encoder.encodeSerializableValue(ToolChoiceAllowed.serializer(), value.value)
        is CaseToolChoiceTypes -> encoder.encodeSerializableValue(ToolChoiceTypes.serializer(), value.value)
        is CaseToolChoiceFunction -> encoder.encodeSerializableValue(ToolChoiceFunction.serializer(), value.value)
        is CaseToolChoiceMCP -> encoder.encodeSerializableValue(ToolChoiceMCP.serializer(), value.value)
        is CaseToolChoiceCustom -> encoder.encodeSerializableValue(ToolChoiceCustom.serializer(), value.value)
        is CaseSpecificApplyPatchParam -> encoder.encodeSerializableValue(SpecificApplyPatchParam.serializer(), value.value)
        is CaseSpecificFunctionShellParam -> encoder.encodeSerializableValue(SpecificFunctionShellParam.serializer(), value.value)
      }
    }
  }
}
