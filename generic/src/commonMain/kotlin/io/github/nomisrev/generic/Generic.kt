package io.github.nomisrev.generic

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlin.jvm.JvmInline

public sealed interface Generic {

  public object Null : Generic {
    override fun toString(): kotlin.String = "Generic.Null"
  }

  @JvmInline
  public value class String(public val value: kotlin.String) : Generic

  @JvmInline
  public value class Char(public val value: kotlin.Char) : Generic

  public sealed interface Number<A : kotlin.Number> : Generic {
    public val value: A

    @JvmInline
    public value class Byte(override val value: kotlin.Byte) : Number<kotlin.Byte>

    @JvmInline
    public value class Short(override val value: kotlin.Short) : Number<kotlin.Short>

    @JvmInline
    public value class Int(override val value: kotlin.Int) : Number<kotlin.Int>

    @JvmInline
    public value class Long(override val value: kotlin.Long) : Number<kotlin.Long>

    @JvmInline
    public value class Float(override val value: kotlin.Float) : Number<kotlin.Float>

    @JvmInline
    public value class Double(override val value: kotlin.Double) : Number<kotlin.Double>
  }

  public data class Inline(
    override val info: Info,
    val element: Generic
  ) : Object

  @JvmInline
  public value class Boolean(public val value: kotlin.Boolean) : Generic

  public sealed interface Object : Generic {
    public val info: Info
  }

  /**
   * Represents a product type.
   * A product type has [Info] & a fixed set of [fields]
   *
   * public data class Person(val name: String, val age: Int)
   *
   * Person =>
   *   Schema2.Product(
   *     ObjectInfo("Person"),
   *     listOf(
   *       Pair(FieldName("name"), Schema.string),
   *       Pair(FieldName("age"), Schema.int)
   *     )
   *   )
   */
  public data class Product(
    override val info: Info,
    val fields: List<Pair<kotlin.String, Generic>>,
  ) : Object {
    public companion object {
      public val Empty: Product = Product(Info.unit, emptyList())
      public operator fun invoke(info: Info, vararg fields: Pair<kotlin.String, Generic>): Product =
        Product(info, fields.toList())
    }
  }

  /**
   * Represents a sum or coproduct type.
   * Has [Info], and NonEmptyList of subtypes schemas.
   * These subtype schemas contain all details about the subtypes, since they'll all have Schema2 is Schema2.Object.
   *
   * Either<A, B> =>
   *   Schema2.Coproduct(
   *     Schema2.ObjectInfo("Either", listOf("A", "B")),
   *     listOf(
   *       Schema2.Product("Either.Left", listOf("value", schemeA)),
   *       Schema2.Product("Either.Right", listOf("value", schemeA)),
   *     )
   *   )
   */
  public data class Coproduct(
    override val info: Info,
    val productInfo: Info,
    val fields: List<Pair<kotlin.String, Generic>>,
    val index: Int
  ) : Object

  /**
   * Represents a value in an enum class
   * A product of [kotlin.Enum.name] and [kotlin.Enum.ordinal]
   */
  public data class EnumValue(val name: kotlin.String, val ordinal: Int)

  /**
   * Represents an Enum
   * Has [Info], and list of its values.
   *
   * enum class Test { A, B, C; }
   *
   * Test =>
   *   Schema2.Enum(
   *     Schema2.ObjectInfo("Test"),
   *     listOf(
   *       Schema2.EnumValue("A", 0),
   *       Schema2.EnumValue("B", 1),
   *       Schema2.EnumValue("C", 2)
   *     )
   *   )
   */
  public data class Enum(
    override val info: Info,
    val values: List<EnumValue>,
    val index: Int
  ) : Object

  /**
   * ObjectInfo contains the fullName of an object, and the type-param names.
   *
   * Either<A, B> => ObjectInfo("Either", listOf("A", "B"))
   */
  public data class Info(
    val fullName: kotlin.String,
    val typeParameterShortNames: List<kotlin.String> = emptyList()
  ) {
    public companion object {
      public val unit: Info = Info(fullName = "Unit")
    }
  }

  public companion object {
    public inline fun <reified A> encode(
      value: A,
      serializersModule: SerializersModule = EmptySerializersModule()
    ): Generic = encode(value, serializer(), serializersModule)

    @OptIn(ExperimentalSerializationApi::class)
    public fun <A> encode(
      value: A,
      ser: KSerializer<A>,
      serializersModule: SerializersModule = EmptySerializersModule()
    ): Generic {
      val genericEncoder = GenericEncoder(serializersModule)
      ser.serialize(genericEncoder, value)
      return genericEncoder.result(ser)
    }

    public inline fun <reified A> decode(
      generic: Generic,
      serializersModule: SerializersModule = EmptySerializersModule()
    ): A = decode(generic, serializer(), serializersModule)

    @OptIn(ExperimentalSerializationApi::class)
    public fun <A> decode(
      generic: Generic,
      ser: KSerializer<A>,
      serializersModule: SerializersModule = EmptySerializersModule()
    ): A = ser.deserialize(GenericDecoder(generic, serializersModule))

    public fun <A : kotlin.Enum<A>> enum(
      name: kotlin.String,
      enumValues: Array<out A>,
      index: Int
    ): Generic = Enum(
      Info(name),
      enumValues.map { EnumValue(it.name, it.ordinal) },
      index
    )

    public inline fun <reified A : kotlin.Enum<A>> enum(value: kotlin.Enum<A>): Generic =
      enum(
        requireNotNull(A::class.qualifiedName) { "Qualified name on KClass should never be null." },
        enumValues<A>(),
        enumValues<A>().indexOfFirst { it == value }
      )
  }
}

@ExperimentalSerializationApi
private class GenericEncoder(override val serializersModule: SerializersModule) : AbstractEncoder() {

  private val genericProperties: MutableMap<String, Generic> = mutableMapOf()

  // When this is set, it means that one of the primitive `encodeX` methods was called
  private var genericValue: Generic? = null

  private var value: Any? = null
  private var index: Int = -1
  private var serializer: SerializationStrategy<*>? = null
  private var descriptor: SerialDescriptor? = null
  private var debug: Boolean = false

  private var state: State = State.Init

  private enum class State {
    Init,
    BeginStructure,
    EndStructure,
    EncodeValue,
    EncodeElement,
    EncodeInline,
    EncodeSerializableValue,
    EncodeNullableSerializableValue
  }

  /**
   * Our own custom encodeValue method
   * This is called from all encodeX methods, which exists for primitives and enums
   */
  private fun encodeValue(generic: Generic): Unit =
    when (state) {
      State.Init -> {
        genericValue = generic
      }

      State.EncodeInline -> {
        genericValue = Generic.Inline(
          Generic.Info(descriptor!!.serialName),
          generic
        )
      }

      else -> {
        genericProperties[descriptor?.elementNames?.toList()?.get(index)!!] = generic
      }
    }

  override fun encodeString(value: String) {
    encodeValue(Generic.String(value))
  }

  override fun encodeBoolean(value: Boolean) {
    encodeValue(Generic.Boolean(value))
  }

  override fun encodeChar(value: Char) {
    encodeValue(Generic.Char(value))
  }

  override fun encodeByte(value: Byte) {
    encodeValue(Generic.Number.Byte(value))
  }

  override fun encodeShort(value: Short) {
    encodeValue(Generic.Number.Short(value))
  }

  override fun encodeInt(value: Int) {
    encodeValue(Generic.Number.Int(value))
  }

  override fun encodeLong(value: Long) {
    encodeValue(Generic.Number.Long(value))
  }

  override fun encodeFloat(value: Float) {
    encodeValue(Generic.Number.Float(value))
  }

  override fun encodeDouble(value: Double) {
    encodeValue(Generic.Number.Double(value))
  }

  override fun encodeNull() {
    encodeValue(Generic.Null)
  }

  override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
    encodeValue(
      Generic.Enum(
        Generic.Info(enumDescriptor.serialName),
        enumDescriptor.elementNames.mapIndexed { ord, name -> Generic.EnumValue(name, ord) },
        index
      )
    )
  }

  private fun println(message: Any?): Unit =
    if (debug) kotlin.io.println(message) else Unit

  override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
    state = State.EncodeElement
    println("encodeElement: $descriptor, $index")
    this.descriptor = descriptor
    this.index = index
    return true
  }

  override fun encodeInline(descriptor: SerialDescriptor): Encoder {
    state = State.EncodeInline
    println("encodeInline: $descriptor")
    this.descriptor = descriptor
    return super.encodeInline(descriptor)
  }

  @InternalSerializationApi
  override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
    this.serializer = serializer
    this.value = value
    val encoder = GenericEncoder(serializersModule)
    serializer.serialize(encoder, value)

    when (state) {
      State.Init -> {
        genericValue = encoder.result(serializer)
      }

      State.EncodeInline -> {
        genericValue = Generic.Inline(
          Generic.Info(descriptor!!.serialName),
          encoder.result(serializer)
        )
      }

      else -> {
        state = State.EncodeSerializableValue
        val propertyName: String = descriptor?.elementNames?.toList()?.getOrNull(index) ?: index.toString()
        genericProperties[propertyName] = encoder.result(serializer)
      }
    }
    println("encodeSerializableValue: $serializer, $value")
  }

  override fun <T : Any> encodeNullableSerializableValue(serializer: SerializationStrategy<T>, value: T?) {
    this.serializer = serializer
    this.value = value
    val encoder = GenericEncoder(serializersModule)
    val res = if (value != null) {
      serializer.serialize(encoder, value)
      encoder.result(serializer)
    } else Generic.Null

    if (state == State.Init || state == State.EncodeInline) {
      genericValue = res
    } else {
      state = State.EncodeSerializableValue
      val propertyName: String = descriptor?.elementNames?.toList()?.getOrNull(index) ?: index.toString()
      genericProperties[propertyName] = res
    }
    println("encodeNullableSerializableValue: $serializer, $value")
  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
    state = State.BeginStructure
    this.descriptor = descriptor
    println("beginStructure: $descriptor")
    return this
  }

  override fun endStructure(descriptor: SerialDescriptor) {
    state = State.EndStructure
    this.descriptor = descriptor
    println("endStructure: $descriptor")
  }

  public fun result(serializer: SerializationStrategy<*>): Generic =
    genericValue ?: when (descriptor?.kind) {
      StructureKind.CLASS -> Generic.Product(
        Generic.Info(serializer.descriptor.serialName),
        genericProperties.toList()
      )

      StructureKind.OBJECT -> Generic.Product(
        Generic.Info(serializer.descriptor.serialName),
        genericProperties.toList()
      )

      // Probably similar to SEALED. Extracting the values.
      PolymorphicKind.OPEN -> genericProperties["value"]
        ?: throw RuntimeException("Internal error: no value found for $value in $genericProperties.")

      SerialKind.CONTEXTUAL ->
        throw IllegalStateException("There are no contextual serializers for Generic.")

      StructureKind.LIST -> Generic.Product(
        Generic.Info(serializer.descriptor.serialName),
        genericProperties.toList()
      )

      StructureKind.MAP -> Generic.Product(Generic.Info(serializer.descriptor.serialName), mapGeneric())

      PolymorphicKind.SEALED ->
        Generic.Coproduct(
          Generic.Info(serializer.descriptor.serialName),
          Generic.Info(requireNotNull(this.serializer?.descriptor?.serialName)
          { "Internal error: this.serializer?.descriptor?.serialName was null ${this.serializer}" }),
          // genericProperties contains `value` and `type`
          // Where `type` is a label of the case representing the sum
          // And `value` is the actual instance, we want to extract the fields of the actual instance.
          (genericProperties["value"] as Generic.Product).fields,
          serializer
            .descriptor
            .elementDescriptors
            .last() // Take the last one. The others are filled with optional generic params
            .elementNames
            .indexOf(requireNotNull(this.serializer?.descriptor?.serialName) { "Internal error: this.serializer?.descriptor?.serialName was null ${this.serializer}" })
        )

      null -> throw RuntimeException("Internal error: descriptor is null when requesting result from $this.")
      else -> throw RuntimeException("Internal error: primitives & enum should be handled.")
    }

  private fun mapGeneric(): List<Pair<String, Generic>> {
    var index = 0
    var key: Generic? = null
    val buffer = ArrayList<Pair<String, Generic>>(genericProperties.size / 2)

    genericProperties.forEach { (_, generic) ->
      if (key == null) {
        key = generic
      } else {
        value = generic
        buffer.add(
          Pair(
            "${index++}", Generic.Product(
              Generic.Info(Pair::class.qualifiedName!!),
              "first" to key!!,
              "second" to generic
            )
          )
        )
        key = null
      }
    }

    return buffer
  }
}

@ExperimentalSerializationApi
private class GenericDecoder(
  private val generic: Generic,
  override val serializersModule: SerializersModule
) : AbstractDecoder() {

  private var currentIndex = 0

  override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
    if (currentIndex < descriptor.elementsCount) currentIndex++
    else CompositeDecoder.DECODE_DONE

  inline fun <reified A> expect(): A =
    (generic as? A) ?:  throw SerializationException("Expected ${A::class.simpleName}")

  override fun decodeString(): String =
    expect<Generic.String>().value

  override fun decodeBoolean(): Boolean =
    expect<Generic.Boolean>().value

  override fun decodeChar(): Char =
    expect<Generic.Char>().value


  override fun decodeByte(): Byte =
    expect<Generic.Number.Byte>().value

  override fun decodeShort(): Short =
    expect<Generic.Number.Short>().value

  override fun decodeInt(): Int =
    expect<Generic.Number.Int>().value

  override fun decodeLong(): Long =
    expect<Generic.Number.Long>().value

  override fun decodeFloat(): Float =
    expect<Generic.Number.Float>().value

  override fun decodeDouble(): Double =
    expect<Generic.Number.Double>().value

  override fun decodeNotNullMark(): Boolean =
    generic != Generic.Null

  override fun decodeNull(): Nothing? =
    if (generic == Generic.Null) null
    else throw SerializationException("Expected Null")

  override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
    expect<Generic.Enum>().index

//  private var currentIndex = 0
//  private val fields: List<Generic> = when (generic) {
//    is Generic.Product -> generic.fields.map { it.second }
//    is Generic.Coproduct -> generic.fields.map { it.second }
//    else -> listOf(generic)
//  }
//
//  override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
//    return if (currentIndex < descriptor.elementsCount) currentIndex else CompositeDecoder.DECODE_DONE
//  }
//
//  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
//    val elementGeneric = fields[currentIndex++]
//    return GenericDecoder(elementGeneric, serializersModule)
//  }

  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
    val elementGeneric = when (descriptor.kind) {
      is StructureKind.CLASS, is StructureKind.OBJECT -> {
        (generic as? Generic.Product)?.fields?.get(currentIndex - 1)?.second
      }
      is PolymorphicKind.SEALED -> {
        (generic as? Generic.Coproduct)?.fields?.get(currentIndex - 1)?.second
      }
      else -> generic
    }
    return GenericDecoder(elementGeneric ?: generic, serializersModule)
  }

//  override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
//    GenericDecoder(
//      expect<Generic.Product?>()?.fields?.get(currentIndex - 1)?.second ?: generic,
//      serializersModule
//    )
}