package io.github.nomisrev.openapi.parser

import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.yamlMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/** The available paths and operations for the API, together with Paths Object extensions. */
@Serializable(with = Paths.Companion.Serializer::class)
public data class Paths(
    /** Maps relative paths to their operations. */
    public val paths: Map<String, PathItem> = emptyMap(),
    /** Specification extensions (beginning with x-), with arbitrary JSON values. */
    public val extensions: Map<String, JsonElement> = emptyMap(),
) {
    public constructor(
        head: Pair<String, PathItem>,
        vararg paths: Pair<String, PathItem>
    ) : this(mapOf(head, *paths))

    public companion object {
        internal object Serializer : KSerializer<Paths> {
            private val pathsSerializer = MapSerializer(String.serializer(), PathItem.serializer())
            private val serializer = MapSerializer(String.serializer(), JsonElementWithYamlSerializer)
            override val descriptor: SerialDescriptor = serializer.descriptor

            override fun deserialize(decoder: Decoder): Paths =
                when (decoder) {
                    is JsonDecoder -> {
                        val paths = decoder.decodeJsonElement().jsonObject
                        Paths(
                            paths = decoder.json.decodeFromJsonElement(
                                pathsSerializer,
                                JsonObject(paths.filterKeys { !it.startsWith("x-") }),
                            ),
                            extensions = paths.filterKeys { it.startsWith("x-") },
                        )
                    }

                    is YamlInput -> {
                        val paths = decoder.decodeSerializableValue(YamlNode.serializer()).yamlMap
                        Paths(
                            paths = decoder.yaml.decodeFromYamlNode(
                                pathsSerializer,
                                YamlMap(paths.entries.filterKeys { !it.content.startsWith("x-") }, paths.path),
                            ),
                            extensions = paths.entries.filterKeys { it.content.startsWith("x-") }
                                .mapKeys { it.key.content }.mapValues { it.value.toJsonElement() },
                        )
                    }

                    else -> error("Unsupported decoder $decoder")
                }

            override fun serialize(encoder: Encoder, value: Paths) {
                val paths =
                    buildMap {
                        value.paths.forEach { (path, pathItem) ->
                            put(path, OpenAPI.Json.encodeToJsonElement(PathItem.serializer(), pathItem))
                        }
                        putAll(value.extensions)
                    }
                encoder.encodeSerializableValue(serializer, paths)
            }
        }
    }
}
