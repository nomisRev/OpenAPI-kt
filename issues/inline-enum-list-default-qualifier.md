# The default of an array parameter with inline enum items names the enum under the wrong class

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 2 of 1,547 documents (art19.com, anchore.io); neither has excluded errors
**Source spec valid:** yes — `default` on an array schema whose `items` is an inline enum (OAS 3.0.3 §4.7.24)

## Symptom

```
ClassificationInclusions.kt:43:59: error: unresolved reference 'Sort'.
    sort: List<Sort>? = listOf(ClassificationInclusions.Sort.CreatedAt),
Summaries.kt:29:93: error: unresolved reference 'ImageStatus'.
    imageStatus: List<ImageStatus>? = listOf(Imagetags.ImageStatus.Active)
```

The parameter type is right (`Get.Sort`, declared in the operation class); only the default expression is wrong.

## Minimal reproducer

`/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/inline-enum-list-default-qualifier.json`

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/inclusions":{"get":{
  "parameters":[{"in":"query","name":"sort","schema":{"type":"array","default":["created_at"],
     "items":{"type":"string","enum":["created_at","id"]}}}],
  "responses":{"204":{"description":"ok"}}}}}}
```

Observed:

```kotlin
public suspend operator fun invoke(sort: List<Sort>? = listOf(Inclusions.Sort.CreatedAt)) {   // enum is Inclusions.Get.Sort
Inclusions.kt:20:78: error: unresolved reference 'Sort'.
```

## What the spec says

`default` is the value the server assumes when the parameter is absent; rendering it as the Kotlin default is our choice and must then type-check.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRendererOperations.kt:759-768`: a scalar inline enum parameter builds its default from `resolvedInlineParameterClassName` (the remapped class, `InlineParameterScope.kt:59-74`); everything else falls to `publicModel.defaultLiteral(config)`. For a collection that ends in `CollectionRenderer.kt:45`:

```kotlin
is Model.Enum -> CodeBlock.of("%T.%L", context.toClassName(config), toEnumValueName(raw))
```

`context.toClassName` is the raw path context (`<Path>.<param>`, `TypeMapping.kt:67-90`), without the method class the type was moved to. Same family as `route-inline-type-owner-class-mismatch.md`: a path-context name used without the renderer's remap.

## Correct final API

```kotlin
public suspend operator fun invoke(sort: List<Sort>? = listOf(Sort.CreatedAt))
```

Open design point (applies to all parameter defaults): a Kotlin default equal to the spec default makes the client always send `sort=created_at`, where the spec only says the server assumes it. The most accurate client leaves the parameter out (`= null`) and documents the default in KDoc.

## Fix slice

- Renderer: pass the remapped enum class into the list literal (`toListLiteral` / `collectionEntryLiteral` take a class-name override), or resolve all path-context names through one mapping (see the owner-class report).

## Proof

- Failing test first: golden `ClientSpec` case of the reproducer, compiled.
- Integration tests: no array parameter with a default and inline enum items in `github.json` / `youtrack.json` (script). Not covered.

## Affected documents

art19.com [81a0d9abd056f6140cf9], anchore.io [f390adab417855459aae]. Total 2.
