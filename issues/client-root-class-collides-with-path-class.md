# The root client class and a first-level path class get the same name, and one replaces the other

**Status:** fixed in `8272a0cc`. The root class is named as an identifier (a character that is not a letter or a digit
is a word break; no title is `Api`; a leading digit is prefixed with `Api`), and a root class with the name of a first
path segment's class, ignoring case, is named with `Api` after it (`FilesApi`), each with a warning. Proof:
`RootClassNameSpec`, goldens `client/root-class-name-title` and `client/root-class-name-path`,
`RootClassNameRuntimeTest`.
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit (harness check) / wrong output (no client entry point is generated)
**Impact:** 9 of 1,547 documents: 6 fail at emit (tomtom.com:search, tomtom.com:routing, apideck.com:customer-support, getthedata.com:bng2latlong, hubapi.com:files, vtex.local:Marketplace-APIs-), 3 collide only by case (nasa.gov:apod and tl-api.azurewebsites.net "pass" the census without a client class, amazonaws.com:clouddirectory)
**Source spec valid:** yes

## Symptom

`info.title` "Files" and a path `/files/...` both produce `api/Files.kt` with a class `Files`. The harness reports
`Duplicate generated files: [….api.Files]`. With the Gradle plugin the child file is written last and wins: the build
compiles (verified by re-emitting bng2latlong and customer-support with last-write-wins) and the generated code has
no class with the public constructors — no way to create the client.

Case-only variant: title "APOD" and path `/apod`. `APOD.kt` and `Apod.kt` are one file on macOS/Windows. The census
counts nasa.gov:apod as passing; its `sources/…/api/` directory holds a single `APOD.kt` whose content is
`public class Apod internal constructor(private val client: HttpClient)` — the root client is gone.

## Minimal reproducer

`<scratchpad>/repro/api-root-class-collides-with-path-node.json`

```json
{
  "openapi": "3.0.3",
  "info": { "title": "files", "version": "1" },
  "paths": {
    "/files/{id}": { "get": { "operationId": "getFile",
      "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }],
      "responses": { "204": { "description": "ok" } } } }
  }
}
```

```
worker exit=1 phases: [parse pass, typed pass, render pass, emit fail]
java.lang.IllegalStateException: Duplicate generated files: [io.github.nomisrev.openapi.generated.api.Files]
```

The two files (from `<scratchpad>/naming/dups/33cfb6e8167ca902ddb2/dup/{0,1}`): file 0 is the root
`public class Bng2latlong internal constructor(client) : AutoCloseable { public val bng2latlong: Bng2latlong = Bng2latlong(client) … }`,
file 1 is the path class `public class Bng2latlong internal constructor(client) { public fun easting(…) … }`.
Note the root also declares a property whose type is meant to be the *other* `Bng2latlong`.

## What the spec says

`info.title` is "REQUIRED. The title of the API" (OAS 3.0.3, Info Object) and unrelated to path names. Nothing wrong
with the documents.

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ClientRenderer.kt:28-29`: the root class is
`ClassName(config.apiPackage, name.toPascalCase())` with `name = info.title.toPascalCase()`
(`typed/.../ApiTree.kt:66`). `ClientRenderer.kt:32-37` names each first-level child from
`children.uniqueNames()` and puts it in the same package. `uniqueNames`
(`renderer/.../PathParamUtils.kt:47`) makes siblings unique among themselves, ignoring case, but the root's name is
not in its `takenClasses` set. The server type `<Root>Server` (`renderer/.../ServerStrategy.kt:24`) is not reserved
either (no collision in the corpus).

## Correct final API

The root client keeps the name derived from the title (it is the entry point users type). The colliding path class is
renamed by the existing sibling rule:

```kotlin
public class Files(...) : AutoCloseable {          // api/Files.kt
  public val files: Files2 = Files2(client)        // GET /files/{id} -> client.files.id("1").get()
}
public class Files2 internal constructor(...)      // api/Files2.kt
```

Wire behaviour is unchanged.

## Fix slice

- renderer: pass the reserved top-level names (`rootName`, `"${rootName}Server"`, and `SerializationUtils` if it is
  emitted into the api package, `ClientRenderer.kt:56`) into `uniqueNames` for the root's children, compared ignoring
  case like the others.
- Open decision: suffix the path class (`Files2`) or the client (`FilesClient`/`FilesApi`). Renaming the client only
  on collision makes the entry point's name depend on the paths; a fixed `Client` suffix for every root would be a
  breaking rename for all users. Related: there is no way to choose the client name from the Gradle plugin
  (`generateWithDiagnostics` does not pass `name`, `renderer/.../Generate.kt:18`), see
  `client-name-from-info-title-not-an-identifier.md`.

## Proof

- Failing golden `ClientSpec` case with the reproducer (`renderGeneratedFiles` throws on the duplicate today); a
  second case with title `APOD` and path `/apod` asserting file names are unique ignoring case.
- MockEngine runtime test: `Files("http://x").files.id("1").get()` requests `GET /files/1`.
- integration-tests: GitHub ("GitHub v3 REST API"), OpenAI ("OpenAI API"), YouTrack ("YouTrack REST API") have no
  path segment equal to the title; no coverage.

## Affected documents

Exact (6, all fail at emit): tomtom.com:search [13051cf9256153b7c737], tomtom.com:routing [c94eaae86e9143a7f6cd],
apideck.com:customer-support [1f1cc48a6abb6f9779ab], getthedata.com:bng2latlong [33cfb6e8167ca902ddb2],
hubapi.com:files [98e6e7f4d83f60aac1ab], vtex.local:Marketplace-APIs- [69162096b89d5095dc73].
Case-only (3, by static comparison of title and first segments; nasa verified in the generated sources):
nasa.gov:apod [69ad4b9d00a4c1b4728c], tl-api.azurewebsites.net [0858c2aecb2233cc1204],
amazonaws.com:clouddirectory [3d69be371ad1fec6abad].
