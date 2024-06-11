package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

fun TypeName.nullable(): TypeName =
  copy(nullable = true)

fun ClassName.postfix(postfix: String): ClassName =
  ClassName(
    packageName,
    simpleNames.dropLast(1) + "${simpleNames.last()}$postfix"
  )

inline fun <reified A : Annotation> annotationSpec(): AnnotationSpec =
  AnnotationSpec.builder(A::class).build()

fun TypeSpec.Companion.dataClassBuilder(
  className: ClassName,
  parameters: List<ParameterSpec>
): TypeSpec.Builder =
  classBuilder(className)
    .addModifiers(KModifier.DATA)
    .primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()
    ).addProperties(
      parameters.map { param ->
        PropertySpec.builder(param.name, param.type)
          .initializer(param.name)
          .build()
      }
    )
