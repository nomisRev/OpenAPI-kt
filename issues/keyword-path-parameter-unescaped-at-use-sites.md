# Path parameters named `object`, `class` or `_` are escaped where declared but not where used

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 4 of 1,547 documents compile-fail on it (googleapis.com:storage, monarchinitiative.org, flat.io, brainbi.net); 3 more with a keyword path parameter fail earlier (zuora.com, googleapis.com:datastream, gambitcomm.local:mimic)
**Source spec valid:** yes for keywords and `_`; brainbi.net's empty parameter name (`"name": ""`) is not meaningful

## Symptom

```
api/B.kt:1814:86: error: argument type mismatch: actual type is '<anonymous>', but 'String' was expected.
api/B.kt:1814:92: error: syntax error: Expecting a class body.
api/Classes.kt:41:70: error: syntax error: Expecting an argument.
api/Api.kt:69:15: error: names _, __, ___, ... can only be used in back-ticks (`_`, `__`, `___`, ...).
```

followed by a parser cascade (`Expecting member declaration`, `Property getter or setter expected`, ...): 483 errors in flat.io.

## Minimal reproducer

`<scratchpad>/repro/keyword-path-parameter.json`:

```json
{
  "openapi": "3.0.3",
  "info": { "title": "keyword", "version": "1" },
  "paths": {
    "/b/{bucket}/o/{object}": {
      "get": {
        "parameters": [
          { "name": "bucket", "in": "path", "required": true, "schema": { "type": "string" } },
          { "name": "object", "in": "path", "required": true, "schema": { "type": "string" } }
        ],
        "responses": { "200": { "description": "ok" } }
      }
    }
  }
}
```

Generated (`api/B.kt`):

```kotlin
public fun `object`(`object`: String): ObjectPath = ObjectPath(client, bucket, object)
...
public val `get`: Get = Get(client, bucket, object)
...
val response = client.get("b/${bucket.encodeURLParameter()}/o/${object.encodeURLParameter()}")
```

```
compile exit=1
api/B.kt:24:86: error: argument type mismatch: actual type is '<anonymous>', but 'String' was expected.
api/B.kt:24:92: error: syntax error: Expecting a class body.
api/B.kt:31:53: error: argument type mismatch: actual type is '<anonymous>', but 'String' was expected.
api/B.kt:31:59: error: syntax error: Expecting a class body.
api/B.kt:39:83: error: syntax error: Expecting a class body.
api/B.kt:39:84: error: candidate 'fun String.encodeURLParameter(spaceToPlus: Boolean = ...): String' is inapplicable because of a receiver type mismatch.
```

`<scratchpad>/repro/parameter-named-underscore-or-empty.json` (operation parameter, same mechanism):

```json
{
  "openapi": "3.0.3",
  "info": { "title": "underscore", "version": "1" },
  "paths": {
    "/customers": { "get": { "parameters": [ { "name": "", "in": "query", "schema": { "type": "string" } } ],
                             "responses": { "200": { "description": "ok" } } } },
    "/orders":    { "get": { "parameters": [ { "name": "_", "in": "query", "schema": { "type": "string" } } ],
                             "responses": { "200": { "description": "ok" } } } }
  }
}
```

```kotlin
public suspend operator fun invoke(`_`: String? = null) {
  val response = client.get("orders") {
    if (_ != null) {
      url.encodedParameters.append("_", _.encodeURLParameter())
```

```
compile exit=1
api/Customers.kt:19:13: error: names _, __, ___, ... can only be used in back-ticks (`_`, `__`, `___`, ...).
api/Orders.kt:19:13: error: names _, __, ___, ... can only be used in back-ticks (`_`, `__`, `___`, ...).
```

## What the spec says

OAS 3.0.3 §4.7.12.1: a path parameter's `name` "MUST correspond to a template expression occurring within the path
field". `object`, `class`, `in`, `is`, `_` are legal names. `_` is the jQuery cache-buster query parameter. An empty
`name` (brainbi.net) is syntactically a string but cannot be sent; it deserves a diagnostic instead of the parameter `_`
written as `append("", ...)`.

## Root cause

Names reach KotlinPoet two ways. Declarations go through `ParameterSpec`/`PropertySpec`/`FunSpec` builders, which
escape keywords and `_`. Uses are concatenated as raw text with `%L`:

- `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt:1157-1162` `constructorArgs`: joins
  `it.name.toCamelCase()` with `", "`, used at `:154`, `:174`, `:234` (`"%T(%L)"`, `"return %T(%L)"`);
- `ClientRendererOperations.kt:101-105` `operationConstructorArgs`, used at `:56`;
- `ImplRenderer.kt:103-105`: `PropertySpec.builder(paramName, ...).initializer(paramName)`;
- `ParameterSerialization.kt:447` (`val variable = segment.name.toCamelCase()`), passed as `expr` to `expansionCode`
  and at `:445` / `:449` written with `%L` into the path template;
- operation parameters: `NamingUtils.kt:39-50` `toParamName` backticks only names that fail `isValidParamName`
  (`:122`, `IdentifierRegex` and `KOTLIN_KEYWORDS`). `_`, `__` match the identifier regex, so they are returned bare
  and every use site (`if (_ != null)`, `_.encodeURLParameter()`) is invalid Kotlin. An empty name becomes `_` at
  `:47` (`ifEmpty { "_" }`).

Path parameter names never go through `toParamName` (only `toCamelCase`), which is why keyword operation parameters
compile (they are pre-escaped strings) and keyword path parameters do not.

## Correct final API

```kotlin
public fun `object`(`object`: String): ObjectPath = ObjectPath(client, bucket, `object`)
val response = client.get("b/${bucket.encodeURLParameter()}/o/${`object`.encodeURLParameter()}")

public suspend operator fun invoke(`_`: String? = null) {
  ...
    if (`_` != null) {
      url.encodedParameters.append("_", `_`.encodeURLParameter())
```

Wire: `GET b/my-bucket/o/a%2Fb.txt`, `GET orders?_=1699999999`. A parameter with an empty name: an error diagnostic
(`GenerationDiagnostics`) naming the operation, no generated parameter.

## Fix slice

Renderer only.

- Use `%N` (name, escaped by KotlinPoet) instead of `%L` wherever a parameter or property name is referenced:
  build `constructorArgs` as a `CodeBlock` joined from `CodeBlock.of("%N", name)`.
- In `pathLiteral` / `expansionCode` the name sits inside a string template; `$object` must become `${`object`}`
  (`interpolated()` at `ParameterSerialization.kt:521` only takes the `$name` shortcut for `Identifier` matches, so an
  escaped name already falls into the `${...}` branch once the expression is escaped).
- Make `isValidParamName` reject names that consist only of underscores, or drop the pre-escaping in `toParamName`
  and rely on `%N` everywhere.
- Diagnostic for empty parameter names (typed layer or `GenerationDiagnostics.kt`).

## Proof

- Failing tests first: `ClientSpec` golden cases `keyword-path-parameter` (`/b/{bucket}/o/{object}`, plus `{class}` and
  `{in}`) and `underscore-parameter`; a MockEngine runtime test so the generated code is compiled.
- integration-tests: `github.json` and `youtrack.json` have no path template parameter that is a Kotlin hard keyword
  and no `_` parameter; not covered.

## Affected documents

- googleapis.com:storage [738d5feeeb3ccb056360] — `{object}`; all 66 errors are this.
- monarchinitiative.org [8a781bdb47853357ba8a] — `{object}`; all 18 errors are this.
- flat.io [0c343c4e447589dc5b7e] — `{class}`; also the `url` shadowing (224 of 483 errors).
- brainbi.net [7feff951ba2385533f01] — query parameter with an empty name (rendered `_`); also the `url` shadowing.

Blocked before compile, will hit this next: zuora.com [2c001941fbc8ed312c21] (`{object}`, fails in typed),
googleapis.com:datastream [c2985941450bedc3a2b6] (`{object}`, fails in emit),
gambitcomm.local:mimic [c3edba593cb89c99b4a9] (`{var}`, `{object}`, `{interface}`, fails in typed).
