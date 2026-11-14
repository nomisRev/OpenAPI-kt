# A server with `description: ""` gets a case with an empty name and file writing crashes

**Status:** fixed on branch worktree-agent-af3ec27e9e0840678 (based on openapi-3.2-streaming), with `server-case-names-collide.md`: an empty, blank or `server`-only description is named after the URL (`ServerNamesTest.anEmptyOrBlankDescriptionIsNamedAfterTheUrl`).
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** emit
**Impact:** 2 of 1,547 documents (wealthreader.com, groundhog-day.com); 2 more have such a server but stop at typed (vtex.local:Marketplace-Protocol, microcks.local)
**Source spec valid:** yes — `description` is an optional string, and SwaggerHub exports write `"description": ""`

Related, same function, written up separately: `server-case-name-not-identifier.md` (digits, `:`, parentheses,
unescaped default argument) and `server-case-names-collide.md` (duplicate descriptions, 269 documents). One fix
should cover all three; this report exists because the symptom is a crash in a different phase and the census
family is a different one.

## Symptom

`NoSuchElementException: Char sequence is empty.` from KotlinPoet while writing the root client file. No diagnostic.
This is half of the census family "emit: Char sequence is empty" (4 documents); the other half is a path segment
`-` (`path-literal-segment-not-a-kotlin-identifier.md`).

## Minimal reproducer

`<scratchpad>/repro/server-empty-description-empty-class-name.json`

```json
{
  "openapi": "3.0.0",
  "info": { "title": "Repro", "version": "1" },
  "servers": [
    { "description": "Mock", "url": "https://mock.example.com" },
    { "description": "", "url": "https://api.example.com" }
  ],
  "paths": { "/ping": { "get": { "operationId": "ping", "responses": { "204": { "description": "ok" } } } } }
}
```

```
worker exit=1 phases: [parse pass, typed pass, render pass, emit fail]
java.util.NoSuchElementException: Char sequence is empty.
	at kotlin.text.StringsKt___StringsKt.first(_Strings.kt:77)
	at com.squareup.kotlinpoet.UtilKt.escapeIfNotJavaIdentifier(Util.kt:357)
```

Which type fails was found with `<scratchpad>/naming/EmitDiag.kt` (it re-emits every nested `TypeSpec` on its own):
`FAILING TYPE WealthReaderAPIServer.` followed by the empty name, `kind=OBJECT`, `props=['url']`. Both corpus
documents have the same server list shape: a SwaggerHub mock server plus
`{ "description": "", "url": "https://api.wealthreader.com/" }`.

## What the spec says

OAS 3.0.3 Server Object: `description` — "An optional string describing the host designated by the URL"
(https://spec.openapis.org/oas/v3.0.3.html#server-object). An empty string is a string.

## Root cause (traced)

`renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt:349-354` `Server.toServerCaseName()`:

```kotlin
if (description != null) {
    return description!!.replace(Regex("\\s+[Ss]erver\\s*$"), "").toPascalCase()
}
```

A non-null description always wins, also when it yields `""` (empty, blank, only separators, or just the word
"server" after a space). The URL-derived fallback and its `.ifEmpty { "Default" }` (`:355-362`) are only reached for a
`null` description. `FactoryRenderer.kt:247` then calls `TypeSpec.objectBuilder("")`, which KotlinPoet accepts until
the file is written.

## Correct final API

The case is named from the URL when the description gives nothing:

```kotlin
public sealed interface ReproServer {
  public val url: String
  public data object Mock : ReproServer { override val url: String = "https://mock.example.com" }
  public data object ApiExample : ReproServer { override val url: String = "https://api.example.com" }
  public data class Custom(override val url: String) : ReproServer
}
```

## Fix slice

- renderer: `description?.…toPascalCase()?.takeIf { it.isNotEmpty() } ?: <url-derived name>` — and, with the two
  related reports, compute all case names of a server list in one pass (sanitise, fall back, make unique, reference
  with `%N`).
- No open design decision specific to this case.

## Proof

- Failing golden `ClientSpec` case with the reproducer (today the exception escapes from `file.writeTo`).
- A unit test for `toServerCaseName` with `""`, `" "`, `"-"`, `" server"`.
- integration-tests: GitHub, OpenAI and YouTrack each declare one server without variables, so no server type is
  generated (`ServerStrategy.SingleFixed`). No coverage.

## Affected documents

wealthreader.com [499e4994bdbdf52b6994], groundhog-day.com [c67864a5457ce007fd59]. Hidden behind typed failures:
vtex.local:Marketplace-Protocol [68d43fefe2e2de41a8d4], microcks.local [c2e53ef7acd78f1fd5d1].
