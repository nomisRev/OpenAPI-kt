package io.github.nomisrev.generic

import io.github.nomisrev.generic.Generic.Info
import io.github.nomisrev.generic.Generic.Value
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@Serializable @JvmInline value class IBoolean(val value: kotlin.Boolean)

@Serializable @JvmInline value class IString(val value: kotlin.String)

@Serializable @JvmInline value class IChar(val value: kotlin.Char)

@Serializable @JvmInline value class IByte(val value: kotlin.Byte)

@Serializable @JvmInline value class IShort(val value: kotlin.Short)

@Serializable @JvmInline value class IInt(val value: kotlin.Int)

@Serializable @JvmInline value class ILong(val value: kotlin.Long)

@Serializable @JvmInline value class IFloat(val value: kotlin.Float)

@Serializable @JvmInline value class IDouble(val value: kotlin.Double)

@Serializable @JvmInline value class ITree(val value: Tree<kotlin.String>)

@Serializable @JvmInline value class IPerson(val value: Person)

class InlineSpec :
  StringSpec({
    //  testInline(Arb.bool().map(::IBoolean)) { Inline(Info(IBoolean::class.qualifiedName!!),
    // Boolean(it.value)) }
    //  testInline(Arb.string().map(::IString)) { Inline(Info(IString::class.qualifiedName!!),
    // String(it.value)) }
    //  testInline(Arb.char().map(::IChar)) { Inline(Info(IChar::class.qualifiedName!!),
    // Char(it.value)) }
    //  testInline(Arb.byte().map(::IByte)) { Inline(Info(IByte::class.qualifiedName!!),
    // Byte(it.value)) }
    //  testInline(Arb.short().map(::IShort)) { Inline(Info(IShort::class.qualifiedName!!),
    // Short(it.value)) }
    //  testInline(Arb.int().map(::IInt)) { Inline(Info(IInt::class.qualifiedName!!), Int(it.value))
    // }
    //  testInline(Arb.long().map(::ILong)) { Inline(Info(ILong::class.qualifiedName!!),
    // Long(it.value)) }
    //  testInline(Arb.float().map(::IFloat)) { Inline(Info(IFloat::class.qualifiedName!!),
    // Float(it.value)) }
    //  testInline(Arb.double().map(::IDouble)) { Inline(Info(IDouble::class.qualifiedName!!),
    // Double(it.value)) }
    //
    //  testInline(Arb.bind(Arb.string(), Arb.int(), ::Person).map(::IPerson)) {
    //    val (name, age, p) = it.value
    //    Inline(Info(IPerson::class.qualifiedName!!), person(name, age, p))
    //  }

    testInline(
      Arb.bind(Arb.string(), Arb.string(), Arb.string()) { a, b, c ->
        ITree(Branch(Leaf(a), Branch(Leaf(b), Leaf(c))))
      },
      serializersModule
    ) {
      Value(Info(ITree::class.qualifiedName!!), tree(it.value))
    }
  })

inline fun <reified A> StringSpec.testInline(
  arb: Arb<A>,
  serializersModule: SerializersModule = EmptySerializersModule,
  noinline expected: (A) -> Generic
) {
  "${A::class.qualifiedName}" {
    checkAll(arb) { inlined ->
      Generic.encode(inlined, serializersModule = serializersModule) shouldBe expected(inlined)
    }
  }

  "Nested in Id - ${A::class.qualifiedName}" {
    checkAll(arb) { inlined ->
      Generic.encode(Id(inlined), serializersModule = serializersModule) shouldBe
        expected(inlined).id()
    }
  }
}
