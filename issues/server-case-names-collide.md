# Two servers with the same description or host render two classes with one name

**Status:** open
**Confirmed:** census 2026-11-14 (branch openapi-3.2-streaming)
**Stage:** compile
**Impact:** 269 of 1,547 documents fail to compile with it; for 53 it is the only compile error. amazonaws.com (213), sportsdata.io (33), nytimes.com (9), and one each of clickmeter, parliament.uk:oralquestions, bclaws.ca, wso2apistore, ebay sell-fulfillment, vtex Headless-CMS, mermade, deutschebahn, bbci, shotstack, ptv, nasa, tvmaze, pressassociation. (272 documents have two servers with equal description-or-URL keys; the rest fail before compile.)
**Source spec valid:** yes — nothing requires server descriptions to be unique, and `http://host` + `https://host` is the most common server list there is.

## Symptom

```
api/T.kt:42:22: error: conflicting declarations:
api/T.kt:46:22: error: conflicting declarations:
```

on the cases of the generated `sealed interface <Api>Server`. With server variables (AWS) the cascade adds `initializer type mismatch: expected 'XServer', actual 'XServer.TheAmazonS3MultiRegionEndpoint'`, `unresolved reference 'Region'` and, in vtex Headless-CMS, `unresolved reference 'workspace'`.

The error text is the "conflicting declarations" family owned by another report, but the cause is specific to server naming and independent of schema/operation name collisions.

## Minimal reproducer

`<scratchpad>/repro/server-case-names-collide.json`:

```json
{"openapi":"3.0.3","info":{"title":"t","version":"1"},
 "servers":[{"url":"http://api.example.com"},{"url":"https://api.example.com"}],
 "paths":{"/ping":{"get":{"responses":{"200":{"description":"ok"}}}}}}
```

```
worker exit=0 phases: parse pass, typed pass, render pass, emit pass
diagnostics: []
compile exit=1
api/T.kt:42:22: error: conflicting declarations:
api/T.kt:46:22: error: conflicting declarations:
```

```kotlin
public sealed interface TServer {
  public val url: String
  public data object ApiExample : TServer { override val url: String = "http://api.example.com" }
  public data object ApiExample : TServer { override val url: String = "https://api.example.com" }
  public data class Custom(override val url: String) : TServer
}
```

Corpus shapes: sportsdata `http://azure-api.sportsdata.io/v3/soccer/stats` + `https://…` (no description); AWS two servers both described "The AWS Migration Hub multi-region endpoint" (http and https) → two `data class TheAWSMigrationHubMultiRegionEndpoint`; vtex `https://{account}.myvtex.com` + `https://{workspace}--{account}.myvtex.com` → two `MyvtexCom`.

## What the spec says

OAS 3.0.3 §4.7.5 Server Object: `url` REQUIRED, `description` optional free text ("An optional string describing the host designated by the URL"). §4.7.1: `servers` is "An array of Server Objects"; no uniqueness rule on description, and two URLs differing only in scheme are distinct servers.

## Root cause

Traced. `Server.toServerCaseName`, `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/FactoryRenderer.kt:347-360`, names a case from the description alone, else from the first two host/path words of the URL with the scheme removed:

```kotlin
if (description != null) return description!!.replace(Regex("\\s+[Ss]erver\\s*$"), "").toPascalCase()
return url.removePrefix("https://").removePrefix("http://").split("/", ".", "-")
    .filter { it.isNotEmpty() && !it.startsWith("{") }.take(2).joinToString("") { it.toPascalCase() }.ifEmpty { "Default" }
```

`generateServerType` (`FactoryRenderer.kt:213-215`) adds one nested type per server with that name and never checks the names against each other, nor against the fixed `Custom` case (`FactoryRenderer.kt:219-235`; a server described "Custom" collides too — not seen in the corpus, by reading the code). The scheme, port, path beyond two words, and variables that distinguish the servers are all discarded.

## Correct final API

Every declared server is a distinct, stable case; the first one stays the default argument:

```kotlin
public sealed interface TServer {
  public val url: String
  public data object ApiExample : TServer        // http://api.example.com   (first keeps the name)
  public data object ApiExampleHttps : TServer   // https://api.example.com
  public data class Custom(override val url: String) : TServer
}
```

Wire behaviour unchanged: `defaultRequest { url(server.url) }`.

## Fix slice

- renderer only: compute all case names for the list at once (a `List<Server>.caseNames()`), as `uniqueNames()` does for path nodes in `PathParamUtils.kt:44`. Reserve `Custom`.
- Open design decision, how to disambiguate: (a) a meaningful discriminator — scheme when only the scheme differs (`…Http`/`…Https`), then port, then more URL words; (b) the numeric suffix convention already used for path nodes (`ApiExample`, `ApiExample2`: first declared keeps the name, deterministic). (a) reads better for the 246 AWS/sportsdata documents, (b) always terminates; a combination (try scheme, fall back to a number) covers both.
- Use the same name list in `buildSecondaryConstructors` (`FactoryRenderer.kt:63-70`) so the default argument refers to the renamed case.
- Same pass should make the name a valid identifier; see `issues/server-case-name-not-identifier.md`.

## Re-checked on 2026-11-14 (census `census-2026-11-14e`, 550 documents compiling)

- 270 documents still fail to compile with this error. For 54 it is the only family of errors; for another 144 the
  only other family is `unregistered-status-codes-share-one-response-case`. Fixing both leaves 198 documents without a
  known compile error (an upper bound: the census sees nothing past the compiler).
- What tells the clashing servers apart, over all 617 clashing groups in the corpus (341 documents, counted from the
  spec files with the naming rule above): the scheme only in 611 (`http://x` next to `https://x`, usually with one
  description); different URLs with the same first two host words or the same description in 5 (nowpayments
  `…io` / `…ioo`, shotstack `/{version}` / `/serve/{version}`); scheme and another URL difference together in 1
  (Amazon S3: four servers, one description). No group repeats an identical URL.
- 575 server names are not identifiers today because of parentheses (`…China(Beijing)AndChina(Ningxia)`), and 213
  names are longer than 60 characters (longest 224): both come from using the whole description.

## Proof

- Golden `ClientSpec` case next to `renderer/src/jvmTest/resources/kotlinTestData/client/factory-servers/`: servers `http://x` + `https://x`; two servers with one description; a server described "Custom". Today the golden output would contain two identical declarations — add a compile check (the reproducer) since a golden file alone does not prove it compiles.
- integration-tests do not cover it: `github.json`, `youtrack.json` and `openai.yaml` each declare exactly one server without variables (`ServerStrategy.SingleFixed`, no server type generated at all), so the whole `Multiple`/`SingleVariable` rendering path has no compiled coverage.

## Affected documents

Only compile error is this one (53): sportsdata.io ×33 (e.g. soccer-v3-stats [107c18e1afc249df36d7], nba-v3-stats [4727667abbeab49f1f5c]), nytimes.com:article_search [23778d1fc70b7870829f], nytimes.com:semantic_api [17622b98843cfae12b78], nytimes.com:times_tags [361d349baec6103566f2], parliament.uk:oralquestions [6f97fa376973b6d946cd], bclaws.ca:bclaws [833caf45b060d0d3aad5], ebay.com:sell-fulfillment [a1de28600863e8061708], deutschebahn.com:reisezentren [ba2e02bd567baabb490e], bbci.co.uk [c6ab0fc950ba4566e5b9], amazonaws.com:marketplacecommerceanalytics [40ba6af1e878d8984153], amazonaws.com:iotsecuretunneling [65b5f5620542dd1063bb]. Total 269 (213 AWS, where other errors remain after the fix). Full list: `<scratchpad>/cc/servers.json` (script `servers.py`).
