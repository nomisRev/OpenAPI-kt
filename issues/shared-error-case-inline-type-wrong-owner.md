# Inline types of a shared error case are referenced under the wrong class

**Status:** fixed in `a8e070fa`.

**Stage:** compile. **Documents:** 43, all apisetu.gov.in (eighth census; reached since `3e55150c`).

## Problem

An operation with several success media types (`application/pdf` and `application/xml` on `200`) has one method
and one sealed result per media type (`PdfResponse`, `XmlResponse`). An error status is the same for each, so it is
one class declared next to them that implements all of them:

```kotlin
public data class BadRequest(
  val error: PdfResponse.BadRequest.Error? = null, // unresolved: the class is Post.BadRequest
) : PdfResponse, XmlResponse {
  enum class Error { … }
}
```

The helper that builds the case used one class both as the interface to implement and as the class the case is
nested in, and the shared case passed its first interface. Types nested in the error were therefore named under
`PdfResponse`. An inline error object with only primitive properties has no nested name, which is why the
`response-separate-methods*` goldens did not show it.

## Fix

`buildInlineResponseCaseTypeSpec` takes the owner class separately; a shared case passes the method class. The
property is now `BadRequest.Error`.

## Proof

Golden `client/shared-error-case-inline-types` (inline enums, a nested object with its own enum, an array of inline
objects) and `SharedErrorCaseInlineTypesRuntimeTest` (the error decoded by both `json()` and `pdf()`). A census
re-check with `--filter apisetu.gov.in`: 43 compile → pass, the other 138 unchanged.
