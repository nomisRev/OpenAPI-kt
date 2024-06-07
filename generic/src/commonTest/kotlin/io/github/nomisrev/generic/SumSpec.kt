package io.github.nomisrev.generic

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable

@Serializable sealed class Tree<A>

@Serializable // TODO: Index 1??
data class Leaf<A>(val value: A) : Tree<A>()

@Serializable // TODO: Index 0 ???
data class Branch<A>(val left: Tree<A>, val right: Tree<A>) : Tree<A>()

class SumSpec :
  StringSpec({
    "Tree" {
      checkAll(Arb.string(), Arb.string(), Arb.string()) { a, b, c ->
        val tree: Tree<String> = Branch(Leaf(a), Branch(Leaf(b), Leaf(c)))
        val actual = Generic.encode(tree, serializersModule = serializersModule)
        val expected = branch(leaf(a), branch(leaf(b), leaf(c)))
        actual shouldBe expected
      }
    }
  })

fun tree(value: Tree<String>): Generic =
  when (value) {
    is Leaf -> leaf(Generic.String(value.value))
    is Branch -> branch(tree(value.left), tree(value.right))
  }

fun leaf(value: String): Generic = leaf(Generic.String(value))

fun leaf(value: Generic): Generic =
  Generic.Coproduct(
    Generic.Info(Tree::class.qualifiedName!!),
    Generic.Info(Leaf::class.qualifiedName!!),
    listOf("value" to value),
    1
  )

fun branch(left: Generic, right: Generic): Generic =
  Generic.Coproduct(
    Generic.Info(Tree::class.qualifiedName!!),
    Generic.Info(Branch::class.qualifiedName!!),
    listOf("left" to left, "right" to right),
    0
  )
