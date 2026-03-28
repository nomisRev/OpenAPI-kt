package io.github.nomisrev.openapi

fun Model.EnumValue.wireValue(): String = rawValue ?: "null"
