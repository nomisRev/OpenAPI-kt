# `style: deepObject` on array form fields (Stripe `expand[]=a`)

**Status:** open, not supported by design
**Found:** 2026-11-04, while fixing form field serialization (commit 16169168)

## Problem

Stripe's OpenAPI spec declares `application/x-www-form-urlencoded` request bodies whose array fields use an Encoding Object with `style: deepObject`. The best-known one is `expand`:

```yaml
requestBody:
  content:
    application/x-www-form-urlencoded:
      schema:
        type: object
        properties:
          expand:
            type: array
            items: { type: string }
      encoding:
        expand:
          explode: true
          style: deepObject
```

Stripe's servers expect the array as repeated bracketed keys:

```
expand[]=customer&expand[]=invoice
```

## Why it is not supported

The OpenAPI Specification doesn't define this serialization. In the Style Values table (OAS 3.0.x / 3.1.x), `deepObject` applies to `object` only: it "allows objects with scalar properties to be represented using form parameters", and the representation of array or object properties "is not defined". Neither `deepObject` on an array nor the `name[]=value` encoding appears in the spec, so this is a Stripe (Rails/Rack) convention.

Since commit 16169168, the generator reports form fields it can't serialize faithfully through `GenerationDiagnostics`:

- an **error** when that form is the operation's only request body;
- a **warning** with the overload skipped otherwise.

Every Stripe form is the operation's only body, so generating a client from Stripe's spec now fails with clear errors. Before 16169168 these operations generated code that didn't compile.

## Census 2026-11-14 (branch openapi-3.2-streaming)

Counted over the 1,547 OpenAPI 3 documents of the census (`exploration/build/census-2026-11-14/`), by scanning the
source specs rather than the diagnostics:

- 112 documents declare an `application/x-www-form-urlencoded` request body; only 3 of them carry an Encoding Object
  at all (stripe.com, seldon.local:core, seldon.local:wrapper), and **stripe.com [bd6338ab48ec594bbcc7] is the only
  document that uses `style: deepObject` on a form field**. This is a one-vendor convention in the corpus.
- In stripe.com, `encoding.<field>.style: deepObject` is declared for 764 form fields: 261 arrays (the subject of this
  report), 255 objects, 183 `anyOf` unions, 48 typed maps (`additionalProperties: {schema}`) and 17 free-form objects.
  Only the flat object-of-scalars and map-of-scalars subsets have a serialization OpenAPI defines; the unions and
  free-form objects are covered by `issues/form-field-union-of-scalars-refused.md` and
  `issues/untyped-schema-parameters-and-form-fields-refused.md`.
- None of this is visible in the census results yet: stripe.com fails earlier, in `typed`, with
  `IllegalArgumentException: Form URL encoded body must be an object.` (a different root cause:
  `issues/form-urlencoded-non-object-body.md`). The diagnostics described above only appear once that is fixed, so the census shows 0 occurrences of a
  `deepObject` form-field refusal.
- The other form-field refusals the census does show (objects and arrays of objects without an Encoding Object,
  unions, untyped and binary fields; 22 documents) are separate root causes with their own reports:
  `form-field-object-without-encoding-refused.md`, `form-field-union-of-scalars-refused.md`,
  `untyped-schema-parameters-and-form-fields-refused.md`, `form-field-binary-refused.md`.
- Related query-parameter family: 7 documents use `style: deepObject` on query parameters with shapes the generator
  refuses (`deepobject-parameter-open-or-nested-object-refused.md`); apideck.com:issue-tracking's `filter` has an array property (`status`),
  the same undefined array-inside-`deepObject` case as Stripe's `expand`.

## Options

1. **Keep the strict error.** This is the current behaviour, and it is accurate to the spec.
2. **Opt-in vendor convention:** a Gradle plugin flag (for example `formArrayDeepObject = "brackets"`) that serializes `deepObject` arrays as `name[]=v1&name[]=v2`. It's off by default, so spec-conformant output is unchanged.
3. **Treat `deepObject` arrays as `form` + `explode: true`** (`expand=a&expand=b`). Not recommended: Stripe rejects or misreads this, so it would swap a clear error for a wrong request.

## Pointers

- Serialization: `addFormFieldCode` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`.
- Support check and diagnostics: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/GenerationDiagnostics.kt`.
- Style model: `FormStyle` on `Route.Body.FormUrlEncoded` in `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt`.
- Spec fixture: `parser/src/commonTest/resources/specs/stripe.json`.
