package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()

    val rootName = name.toPascalCase()

    val rootInterface = TypeSpec.interfaceBuilder(rootName)

    // Root-level operation stubs
    for ((method, _) in operations.entries.sortedBy { it.key.value }) {
        rootInterface.addFunction(
            FunSpec.builder(method.value.lowercase())
                .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                .build()
        )
    }

    // Direct children: separate files, top-level interfaces
    val childFiles = mutableListOf<FileSpec>()
    for (child in children) {
        val childSimpleName = child.segment.name.toPascalCase()
        val childClassName = ClassName(config.apiPackage, childSimpleName)

        child.segment.addNavigationMember(rootInterface, childClassName, config)

        val childTypeSpec = child.toTypeSpec(config, childClassName)
        childFiles.add(
            FileSpec.builder(config.apiPackage, childSimpleName)
                .addType(childTypeSpec)
                .build()
        )
    }

    val rootFile = FileSpec.builder(config.apiPackage, rootName)
        .addType(rootInterface.build())
        .build()

    return listOf(rootFile) + childFiles
}

private fun PathNode.toTypeSpec(config: RenderConfig, className: ClassName): TypeSpec {
    val builder = TypeSpec.interfaceBuilder(className.simpleName)

    // Operation stubs
    for ((method, _) in operations.entries.sortedBy { it.key.value }) {
        builder.addFunction(
            FunSpec.builder(method.value.lowercase())
                .addModifiers(KModifier.ABSTRACT, KModifier.SUSPEND)
                .build()
        )
    }

    // Children
    for (child in children) {
        val childSimpleName = child.segment.name.toPascalCase()
        val childClassName = className.nestedClass(childSimpleName)

        child.segment.addNavigationMember(builder, childClassName, config)
        builder.addType(child.toTypeSpec(config, childClassName))
    }

    return builder.build()
}

private fun PathSegment.addNavigationMember(
    builder: TypeSpec.Builder,
    childClassName: ClassName,
    config: RenderConfig,
) {
    when (this) {
        is PathSegment.Literal -> {
            builder.addProperty(
                PropertySpec.builder(name.toCamelCase(), childClassName)
                    .build()
            )
        }

        is PathSegment.Parameter -> {
            val paramName = name.toCamelCase()
            builder.addFunction(
                FunSpec.builder(paramName)
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(paramName, type.toTypeName(config))
                    .returns(childClassName)
                    .build()
            )
        }
    }
}
