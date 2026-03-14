package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class GetBudget(
    val id: String,
    @SerialName("budget_scope") val budgetScope: BudgetScope,
    @SerialName("budget_entity_name") val budgetEntityName: String,
    @SerialName("budget_amount") val budgetAmount: Long,
    @SerialName("prevent_further_usage") val preventFurtherUsage: Boolean,
    @SerialName("budget_product_sku") val budgetProductSku: String,
    @SerialName("budget_type") val budgetType: BudgetType,
    @SerialName("budget_alerting") val budgetAlerting: BudgetAlerting,
) {
    @Serializable
    enum class BudgetScope {
        @SerialName("enterprise")
        Enterprise,
        @SerialName("organization")
        Organization,
        @SerialName("repository")
        Repository,
        @SerialName("cost_center")
        CostCenter;
    }

    @Serializable(with = BudgetType.Serializer::class)
    sealed interface BudgetType {
        @Serializable
        enum class ProductPricing : BudgetType {
            ProductPricing;
        }

        @Serializable
        enum class SkuPricing : BudgetType {
            SkuPricing;
        }

        object Serializer : KSerializer<BudgetType> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.GetBudget.BudgetType", PolymorphicKind.SEALED) {
                    element("ProductPricing", BudgetType.ProductPricing.serializer().descriptor)
                    element("SkuPricing", BudgetType.SkuPricing.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): BudgetType {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    ProductPricing::class to { decodeFromJsonElement(BudgetType.ProductPricing.serializer(), it) },
                    SkuPricing::class to { decodeFromJsonElement(BudgetType.SkuPricing.serializer(), it) },
                )
            }

            override fun serialize(encoder: Encoder, value: BudgetType) = when(value) {
                is ProductPricing -> encoder.encodeSerializableValue(BudgetType.ProductPricing.serializer(), value)
                is SkuPricing -> encoder.encodeSerializableValue(BudgetType.SkuPricing.serializer(), value)
            }
        }
    }

    @Serializable
    data class BudgetAlerting(
        @SerialName("will_alert") val willAlert: Boolean? = null,
        @SerialName("alert_recipients") val alertRecipients: List<String>? = null,
    )
}
