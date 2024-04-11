# Generic

A `Generic` implementation that allows parsing any model into a structured ADT.

Status: Work In Progress (`Encoder` working)

# Rationale

KotlinX Serialization currently doesn't allow peeking, and this is sometimes necessary.
For example to deserialize (union/sealed) types that we don't own,
for example `Schema | Ref` in OpenAPI.

The goal of this library is to allow peeking through `Generic`,
such as to not break any other implementations.

I.e. we currently "peek" using `Json`, but that is incompatible with `Yaml`.
So `Generic` aims to fix that, and allow universal peeking.

# Details

KotlinX Serialization is implemented using `KSerializer`,
and it allows implementing 2-way, or isomorphic, parsing agnostic of the Kotlin types.
Or in other words, `Person` can be serialized into `Json`, `ProtoBuf`, `Yaml`, `Xml`,
and all other formats that might be available for KotlinX Serialization.

```kotlin
@Serializable
data class Person(val name: String, val age: Int) 
```

Thanks to the `KSerializer<Person>`,
we can detect the structure of `Person` regardless of the underlying format.
And deserialize it to `Generic` ADT.

This ADT will then return `Generic.Product` containing:

- The classname
- Properties (name, type and value)

This allows us to _peek_, inspect and modify the structure and values,
before serializing it further from `Generic` -> `Person`.

Alternatively, for sealed classes it allows us to _peek_ the structure.
i.e.

```kotlin
override fun deserialize(decoder: Decoder): ReferenceOr<T> {
  val generic = decoder.decodeSerializableValue(Generic.serializer())
  return if ((generic as? CoProduct)?.containsField(RefKey)) Reference((generic[RefKey] as Primitive).value)
  else Value(Generic.decode(dataSerializer, generic))
}
```
