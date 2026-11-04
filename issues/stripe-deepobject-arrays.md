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

## Options

1. **Keep the strict error.** This is the current behaviour, and it is accurate to the spec.
2. **Opt-in vendor convention:** a Gradle plugin flag (for example `formArrayDeepObject = "brackets"`) that serializes `deepObject` arrays as `name[]=v1&name[]=v2`. It's off by default, so spec-conformant output is unchanged.
3. **Treat `deepObject` arrays as `form` + `explode: true`** (`expand=a&expand=b`). Not recommended: Stripe rejects or misreads this, so it would swap a clear error for a wrong request.

## Pointers

- Serialization: `addFormFieldCode` in `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/ImplRenderer.kt`.
- Support check and diagnostics: `renderer/src/jvmMain/kotlin/io/github/nomisrev/openapi/GenerationDiagnostics.kt`.
- Style model: `FormStyle` on `Route.Body.FormUrlEncoded` in `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/routes/Route.kt`.
- Spec fixture: `parser/src/commonTest/resources/specs/stripe.json`.
