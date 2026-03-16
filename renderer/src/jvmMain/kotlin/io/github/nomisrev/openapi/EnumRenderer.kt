package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private val JsIdentifierRegex = Regex("^[A-Za-z_$][A-Za-z0-9_$]*$")
private val InvalidJsIdentifierCharsRegex = Regex("[^A-Za-z0-9_$]+")

fun Model.Enum.toTypeSpec(
    config: RenderConfig,
    parentInterface: ClassName? = null,
    serialName: String? = null,
): TypeSpec {
    val className = context.toClassName(config)
    return TypeSpec.enumBuilder(className.simpleName)
        .addAnnotation(Serializable::class)
        .apply {
            serialName?.let { value ->
                addAnnotation(
                    AnnotationSpec.builder(SerialName::class)
                        .addMember("%S", value)
                        .build()
                )
            }
            parentInterface?.let(::addSuperinterface)

            values.forEach { value ->
                val rawValue = value ?: "null"
                val entryName = toEnumValueName(rawValue)
                val entry = TypeSpec.anonymousClassBuilder()

                if (rawValue != entryName.unescapeBackticks()) {
                    entry.addAnnotation(AnnotationSpec.builder(SerialName::class).addMember("%S", rawValue).build())
                }

                if (KmpTarget.JS in config.targets && entryName.needsJsName()) {
                    entry.addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin.js", "JsName"))
                            .addMember("%S", entryName.toJsNameValue())
                            .build()
                    )
                }

                addEnumConstant(entryName, entry.build())
            }
        }
        .build()
}

fun Model.Enum.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .build()
}

private fun String.unescapeBackticks(): String =
    if (startsWith("`") && endsWith("`") && length >= 2) substring(1, length - 1) else this

private fun String.needsJsName(): Boolean {
    val candidate = unescapeBackticks()
    return candidate.firstOrNull()?.isDigit() == true || !JsIdentifierRegex.matches(candidate)
}

private fun String.toJsNameValue(): String {
    val candidate = unescapeBackticks()
    val symbolic = candidate
        .replace("*", "star")
        .replace("/", "slash")
    val sanitized = symbolic.replace(InvalidJsIdentifierCharsRegex, "").ifEmpty { "unnamed" }
    return if (sanitized.firstOrNull()?.isDigit() == true) "_$sanitized" else sanitized
}
