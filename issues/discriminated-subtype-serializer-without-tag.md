# An operation that returns or sends a discriminated subtype uses the case's own serializer, which has no tag

**Status:** open
**Confirmed:** 2026-11-16, while fixing `discriminator-mapping-without-base-entry` (branch openapi-3.2-streaming)
**Stage:** runtime (the client compiles)
**Impact:** 3 documents use a subtype of a discriminated base directly as a response or request body: youtrack (17
subtypes, the integration client: `GET /articles/{id}` returns `BaseArticleRead.Article`), microsoft.com:graph (444,
which does not reach compile today), apple.com:sirikit-cloud-media (3). Counted over the top-level schema and `items` of
every request and response body.
**Source spec valid:** yes

## Symptom

A subtype is a case of the base's sealed interface (`Pet.Feline`), and `@JsonClassDiscriminator("petType")` writes and
reads the tag only when the value is serialized as the base, `Pet`. An operation whose response is `$ref: Cat` is
typed `Pet.Feline` and decodes the body with `Pet.Feline.serializer()`:

```
io.ktor.serialization.JsonConvertException: Illegal input: Unexpected JSON token at offset 2:
Encountered an unknown key 'petType' at path: $
JSON input: {"petType":"feline","name":"Tom"}
```

The generated client installs `json()` with the default `Json`, so every such call fails; YouTrack sends `$type` on
every entity. A request body of a subtype is sent without its tag, so the server cannot tell which subtype it is.

## Reproducer

`renderer/src/jvmTest/resources/kotlinTestData/client/discriminator-without-base` (`GET /cats/{id}` returns `$ref:
Cat`, which the tag `feline` selects). `DiscriminatorWithoutBaseRuntimeTest.anOperationReturningASubtypeReturnsItsCase`
passes only with `ignoreUnknownKeys = true`.

## Correct final API

Keep the precise type, and serialize through the base:

```kotlin
public suspend operator fun invoke(): Pet.Feline {
  val response = __client.get("cats/$id")
  ...
  return response.body<Pet>() as? Pet.Feline ?: throw … // a value of another case is an error, not a ClassCastException
}
```

and `setBody<Pet>(body)` for a request body of a case, so the tag is written. The same for `List<Pet.Feline>` (decode
`List<Pet>`), a property whose type is a case (its serializer is the base's, with a check), and a union branch.

Open decisions: how a body of the wrong case is reported (the project's errors-as-values preference); whether a
property typed as a case needs the same (a model's property `pet: Pet.Feline` has the same problem inside the
generated models).

## Proof

- The runtime test above without `ignoreUnknownKeys`, and an encode test for a request body of a case.
- integration-tests: YouTrack's article and bundle operations; the round-trip harness sends no requests, so it does not
  catch this today.
