# Inline operation types are named after the raw path, not after the class the path was rendered as

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 6 of 1,547 documents (nexmo.com:verify, nexmo.com:sms, openbankingproject.ch, nexmo.com:messages-olympus, smart-me.com, nordigen.com). Only messages-olympus also has excluded errors (5 `conflicting declarations`).
**Source spec valid:** yes — enum path parameters, the path `/`, trailing slashes and paths differing by case are all legal (OAS 3.0.3 §4.7.8 Paths Object, §4.7.12 Parameter Object).

## Symptom

A type declared inside an operation (form field enum, inline JSON body, union case) is referenced through a class chain that does not exist:

```
nexmo verify   : unresolved reference 'FormatPath'          cmd: Control.FormatPath.Post.Body.Cmd
openbanking    : unresolved reference 'PaymentServicePath'  body: V1.PaymentServicePath.PaymentProductPath.Post.Body
nexmo messages : unresolved reference 'Root'                invoke(body: Root.Post.Body.MMS)
nordigen       : unresolved reference 'Slash'               type: Creditors.Slash.Post.Body.Type?
smart-me       : unresolved reference 'Settings'            folderType: Folder.Settings.IdPath.Post.Body.FolderType?
```

plus the cascades `unresolved reference 'value' on receiver of type 'Any'` and `cannot infer type for type parameter 'T'` (`setBody` of an unresolved type).

## Minimal reproducer

Four variants, same operation (a form body with one inline enum field), under `/private/tmp/claude-501/-Users-simonvergauwen-Developer-OpenAPI-kt/a0df68b9-ccc6-4d18-981e-753c6b12368d/scratchpad/repro/`:

| file | path | rendered class | reference emitted |
|---|---|---|---|
| `route-inline-type-owner-fixed.json` | `/verify/{format}`, `format` enum `[json, xml]` | `Verify.Json.Post`, `Verify.Xml.Post` | `Verify.FormatPath.Post.Body.Type` |
| `route-inline-type-owner-root.json` | `/` | `T.Post` (the API class) | `Root.Post.Body.Type` |
| `route-inline-type-owner-slash.json` | `/creditors/` | `Creditors.Post` | `Creditors.Slash.Post.Body.Type` |
| `route-inline-type-owner-suffix.json` | `/folder/{id}` + `/Folder/settings` | `Folder2.Settings.Post` | `Folder.Settings.Post.Body.Type` |

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/verify/{format}":{
  "parameters":[{"name":"format","in":"path","required":true,"schema":{"type":"string","enum":["json","xml"]}}],
  "post":{"requestBody":{"required":true,"content":{"application/x-www-form-urlencoded":{"schema":{"type":"object","properties":{"type":{"type":"string","enum":["a","b"]}}}}}},
          "responses":{"204":{"description":"ok"}}}}}}
```

Observed for `fixed` (the other three are analogous, each exits kotlinc with 1):

```kotlin
public class Verify { public class Json { public class Post {
  public suspend operator fun invoke(type: Verify.FormatPath.Post.Body.Type? = null) { ... }
  public object Body { public enum class Type ... }   // the type is here: Verify.Json.Post.Body.Type
}}}
Verify.kt:30:55: error: unresolved reference 'FormatPath'.
Verify.kt:35:35: error: unresolved reference 'value' on receiver of type 'Any'.
```

## What the spec says

Nothing special: these are ordinary paths. The class tree is our own naming decision, so every reference must follow it.

## Root cause

Traced. `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/TypeMapping.kt:67-90` turns a `NamingContext.Path` into a `ClassName` on its own: `root.segments.map(PathSegment::routeSegmentSimpleName).ifEmpty { listOf("Root") }` + method + `Body`/`Response`. The rendered tree is named elsewhere and differs in four ways:

- an enum path parameter is expanded into `PathSegment.FixedValue` nodes (`typed/.../routes/Route.kt:415,435`), classes `Json`/`Xml`, while the context still holds `Parameter("format")` -> `FormatPath`;
- `/` has no segments: operations live on the API class, `Root` does not exist;
- a trailing-slash segment is a node of its own only next to the same path without it (`PathParamUtils.kt:16-22`, since ca63f057); otherwise there is no `Slash` class;
- siblings with one Kotlin name are suffixed (`uniqueNames()`, `PathParamUtils.kt:47-70`, since c6c9a76d): `Folder2`, but the context says `Folder`.

Call sites that know the real `methodClassName` remap (`InlineParameterScope.kt:79-96` for query/header parameters, `methodClassName.nestedClass("Body")` at `ClientRendererOperations.kt:787`), which is why most operations compile. The ones that use `model.toTypeName(config)` unremapped leak the raw name: form and multipart fields (`ClientRendererOperations.kt:816,828`) and JSON bodies when `usesNestedBodyType` is false (`ClientRendererOperations.kt:789,803`; openbanking, messages-olympus).

The suffix and `Slash` variants exist only since c6c9a76d and ca63f057 (earlier those paths were dropped or merged), so those two are new on this branch; whether smart-me/nordigen compiled before is unknown (not in the Oct 31 pilot).

## Correct final API

The reference is the class the type is declared in:

```kotlin
public suspend operator fun invoke(type: Verify.Json.Post.Body.Type? = null)   // in Verify.Json.Post
public suspend operator fun invoke(type: Verify.Xml.Post.Body.Type? = null)    // in Verify.Xml.Post
```

Wire behaviour is unchanged (`type=a`). Preferable for the enum-path case: one shared `Verify.<...>.Body.Type` instead of a copy per fixed value, as shared inline parameters are hoisted to the path class.

## Fix slice

- Renderer: one source of truth for "class of (path, method)". Either build a map from `NamingContext.Path` (segments + method) to the rendered method `ClassName` while walking the tree and have `NamingContext.toClassName` consult it, or thread `methodClassName` to every use and remap as `inlineParameterTypeName` does. The map is safer: the remaining unremapped call sites are found by the compiler, not by the corpus.
- Open decision: with fixed-value expansion the same model is emitted once per value (`Json.Post.Body`, `Xml.Post.Body`); either keep the copies (simple remap) or hoist to the parent node.

## Proof

- Failing tests first: four golden `ClientSpec` cases from the reproducers, each compiled; assert the parameter type's canonical name is the declared class.
- Integration tests: `github.json` has 1 operation with an enum path parameter and an inline body and 1 operation on `/`, and compiles, so those go through remapped call sites; no form field enum under such a path, no suffixed sibling, no trailing slash with an inline form type. Not covered.

## Affected documents

nexmo.com:verify [2671bf329d55eec20c0b] (fixed value), nexmo.com:sms [bcfcb4ef5ade4419f405] (fixed value), openbankingproject.ch [8770f51323b0ffb06158] (fixed value, JSON body), nexmo.com:messages-olympus [9072255cb991c6d55be2] (`Root`), smart-me.com [e3426242c60b04dcddba] (`Folder2` suffix), nordigen.com [e7bb45654e2703c425cc] (`Slash`). Total 6.
