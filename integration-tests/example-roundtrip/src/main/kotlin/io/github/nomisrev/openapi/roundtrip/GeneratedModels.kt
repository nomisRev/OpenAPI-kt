package io.github.nomisrev.openapi.roundtrip

import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.toApiTree
import io.github.nomisrev.openapi.toClassName
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.internal.GeneratedSerializer
import kotlinx.serialization.serializer

/**
 * The serializer of property [name] of the generated class behind [parent], or `null`. Only serializers the
 * kotlinx.serialization plugin generated for a class know their element serializers.
 */
@OptIn(InternalSerializationApi::class)
internal fun propertySerializer(parent: KSerializer<*>, name: String): PropertyLookup {
    val generated = parent as? GeneratedSerializer<*> ?: return PropertyLookup.NoElements
    val index = parent.descriptor.getElementIndex(name)
    if (index == CompositeDecoder.UNKNOWN_NAME) return PropertyLookup.NotAProperty
    @Suppress("UNCHECKED_CAST")
    return PropertyLookup.Found(generated.childSerializers()[index] as KSerializer<Any?>)
}

internal sealed interface PropertyLookup {
    data class Found(val serializer: KSerializer<Any?>) : PropertyLookup
    /** The class is a union, enum, wrapper or otherwise has a hand-written serializer. */
    data object NoElements : PropertyLookup
    data object NotAProperty : PropertyLookup
}

sealed interface Resolution {
    data class Found(val className: String, val serializer: KSerializer<Any?>) : Resolution
    data class Missing(val reason: String) : Resolution
}

/**
 * Maps a component schema name to the generated Kotlin class and its serializer.
 *
 * The naming is NOT reimplemented: the models come from the generator's own `OpenAPI.toApiTree()` (the call the
 * Gradle plugin makes) and the class name from its own `NamingContext.toClassName(RenderConfig)`.
 *
 * One weakness remains. Whether a schema is split into `FooRead` / `FooWrite` is decided by
 * `List<Model>.contextSpecificNames()`, which is `internal` to the renderer. Instead of mirroring that rule, the
 * candidate names are computed for both settings with the real `toClassName` and the one that exists as a compiled
 * class on the test classpath wins (split name first for the Read/Write variants). That ground truth cannot drift from
 * the generator, but it would pick a wrong class if a split `FooRead` and an unsplit `Foo` ever coexisted.
 */
internal class GeneratedModels(
    openApi: OpenAPI,
    modelPackage: String,
    apiPackage: String,
    private val loader: ClassLoader,
) {
    private val config = RenderConfig(modelPackage = modelPackage, apiPackage = apiPackage, targets = setOf(KmpTarget.JVM))

    /** Top-level named models (schema name -> models, one per Read/Write/Null context). */
    private val byName: Map<String, List<Model.ContextHolder>> = run {
        val tree = runBlocking { openApi.toApiTree() }
        val all = tree.models.flatMap { model ->
            listOf(model) + (model as? Model.DiscriminatedObject)?.subtypes.orEmpty()
        }
        all.filterIsInstance<Model.ContextHolder>().filter { holder ->
            val head = holder.context.head
            head is NamingContext.Reference && holder.context.nested.isEmpty()
        }.groupBy { (it.context.head as NamingContext.Reference).name }
    }

    fun resolve(schemaName: String, role: Role): Resolution {
        val holders = byName[schemaName]
            ?: return Resolution.Missing("schema is not reachable from any operation, so the generator emits no class for it")
        val preference = when (role) {
            Role.Read -> listOf(SchemaContext.Read, SchemaContext.Null, SchemaContext.Write)
            Role.Write -> listOf(SchemaContext.Write, SchemaContext.Null, SchemaContext.Read)
            Role.Schema -> listOf(SchemaContext.Null, SchemaContext.Read, SchemaContext.Write)
        }
        val holder = preference.firstNotNullOf { wanted ->
            holders.firstOrNull { (it.context.head as NamingContext.Reference).context == wanted }
        }
        val tried = mutableListOf<String>()
        for (split in listOf(true, false)) {
            val name = holder.context.toClassName(config.copy(contextSpecificNames = if (split) setOf(schemaName) else emptySet()))
            val reflectionName = name.reflectionName()
            if (reflectionName in tried) continue
            tried += reflectionName
            val clazz = try { Class.forName(reflectionName, false, loader) } catch (_: ClassNotFoundException) { continue }
            val serializer = try { serializer(clazz) } catch (e: Exception) {
                return Resolution.Missing("class $reflectionName has no kotlinx.serialization serializer (${e::class.simpleName})")
            }
            @Suppress("UNCHECKED_CAST")
            return Resolution.Found(reflectionName, serializer as KSerializer<Any?>)
        }
        return Resolution.Missing("the generator names the class ${tried.joinToString(" or ")}, which is not on the test classpath")
    }
}
