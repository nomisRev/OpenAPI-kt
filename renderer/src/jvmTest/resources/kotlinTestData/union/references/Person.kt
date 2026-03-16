package io.github.nomisrev.render.test.union.references

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Person(
  public val name: String,
  public val age: Int,
)
