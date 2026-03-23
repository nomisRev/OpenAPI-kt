package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class Budget(
  public val id: String,
  @SerialName("budget_type")
  public val budgetType: BudgetType,
  @SerialName("budget_amount")
  public val budgetAmount: Long,
  @SerialName("prevent_further_usage")
  public val preventFurtherUsage: Boolean,
  @SerialName("budget_scope")
  public val budgetScope: String,
  @SerialName("budget_entity_name")
  public val budgetEntityName: String? = null,
  @SerialName("budget_product_sku")
  public val budgetProductSku: String,
  @SerialName("budget_alerting")
  public val budgetAlerting: BudgetAlerting,
) {
  @Serializable
  public data class BudgetAlerting(
    @SerialName("will_alert")
    public val willAlert: Boolean,
    @SerialName("alert_recipients")
    public val alertRecipients: List<String>,
  )

  /**
   * The type of pricing for the budget
   */
  @Serializable(with = BudgetType.Serializer::class)
  public sealed interface BudgetType {
    @Serializable
    public enum class SkuPricing : BudgetType {
      SkuPricing,
    }

    @Serializable
    public enum class ProductPricing : BudgetType {
      ProductPricing,
    }

    public object Serializer : KSerializer<BudgetType> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Budget.BudgetType", PolymorphicKind.SEALED) {
        element("SkuPricing", SkuPricing.serializer().descriptor)
        element("ProductPricing", ProductPricing.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): BudgetType {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          SkuPricing::class to { decodeFromJsonElement(SkuPricing.serializer(), it) },
          ProductPricing::class to { decodeFromJsonElement(ProductPricing.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: BudgetType) {
        when(value) {
          is SkuPricing -> encoder.encodeSerializableValue(SkuPricing.serializer(), value)
          is ProductPricing -> encoder.encodeSerializableValue(ProductPricing.serializer(), value)
        }
      }
    }
  }
}
