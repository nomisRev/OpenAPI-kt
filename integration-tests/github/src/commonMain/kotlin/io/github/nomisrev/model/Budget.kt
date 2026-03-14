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
data class Budget(
    val id: String,
    @SerialName("budget_type") val budgetType: BudgetType,
    @SerialName("budget_amount") val budgetAmount: Long,
    @SerialName("prevent_further_usage") val preventFurtherUsage: Boolean,
    @SerialName("budget_scope") val budgetScope: String,
    @SerialName("budget_entity_name") val budgetEntityName: String? = null,
    @SerialName("budget_product_sku") val budgetProductSku: String,
    @SerialName("budget_alerting") val budgetAlerting: BudgetAlerting,
) {
    @Serializable(with = BudgetType.Serializer::class)
    sealed interface BudgetType {
        @Serializable
        enum class SkuPricing : BudgetType {
            SkuPricing;
        }

        @Serializable
        enum class ProductPricing : BudgetType {
            ProductPricing;
        }

        object Serializer : KSerializer<BudgetType> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Budget.BudgetType", PolymorphicKind.SEALED) {
                    element("SkuPricing", BudgetType.SkuPricing.serializer().descriptor)
                    element("ProductPricing", BudgetType.ProductPricing.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): BudgetType {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    SkuPricing::class to { decodeFromJsonElement(BudgetType.SkuPricing.serializer(), it) },
                    ProductPricing::class to { decodeFromJsonElement(BudgetType.ProductPricing.serializer(), it) },
                )
            }

            override fun serialize(encoder: Encoder, value: BudgetType) = when(value) {
                is SkuPricing -> encoder.encodeSerializableValue(BudgetType.SkuPricing.serializer(), value)
                is ProductPricing -> encoder.encodeSerializableValue(BudgetType.ProductPricing.serializer(), value)
            }
        }
    }

    @Serializable
    data class BudgetAlerting(
        @SerialName("will_alert") val willAlert: Boolean,
        @SerialName("alert_recipients") val alertRecipients: List<String>,
    )
}
