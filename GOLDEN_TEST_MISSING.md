# GOLDEN_TEST_MISSING

This document lists missing golden-test coverage in `renderer` by comparing existing golden specs/resources with renderer branch logic.

## Scope reviewed
- Golden test suites under `renderer/src/commonTest/kotlin/io/github/nomisrev/render/*RenderSpec.kt`
- Golden resources under `renderer/src/commonTest/resources/kotlinTestData/**`
- Renderer logic under `renderer/src/commonMain/kotlin/io/github/nomisrev/openapi/render/**`

## Missing cases

### 1. Client renderer gaps (`ClientRenderer.kt`)

1. Request body content types other than JSON are not covered for `SetBody`.
- Missing golden tests for `application/xml`, `application/octet-stream`, `text/plain`, and unknown/custom content type in request bodies.
- Code paths: `Route.Bodies.preferredBodyOrNull()`, `ContentType.asExpression()`, `renderBodyPlacement(RequestBody.SetBody)`.

2. Optional multipart body is not covered.
- Only required multipart cases exist; missing `required = false` coverage for both inline and ref multipart.
- Code paths: `renderBodyPlacement(RequestBody.MultipartInline|MultipartRef)` nullable `?.let` branches.

3. Optional form-urlencoded body is not covered.
- Missing `required = false` branch with conditional parameter appends.
- Code path: `renderBodyPlacement(RequestBody.FormUrlEncoded)` optional body and optional fields branches.

4. Sealed-response operations with request params/body are not covered.
- Existing multi-response/default tests use simple parameter sets; missing case where `usesSealedReturnType()` combines path/query/header/cookie + body placement.
- Code path: sealed branch in `renderOperationImpl` with request block rendering.

5. Unknown HTTP status mapping fallback is not covered.
- Missing test for non-standard status (e.g., 299/599) to hit `asWhenCondition()` fallback `HttpStatusCode(value, "...")`.
- Code path: `HttpStatusCode.asWhenCondition()` final `else`.

6. `sealedCaseName()` fallback naming for unusual status descriptions is not covered.
- Missing golden test for status descriptions that normalize via regex/PascalCase (not standard constants like `OK`, `NotFound`, `NoContent`).
- Code path: `HttpStatusCode.sealedCaseName()` non-204 branch.

7. Duplicate/invalid server description names are not covered.
- Missing tests where multiple servers produce duplicate case names and require numeric suffixes, and where description sanitization falls back.
- Code path: `List<Server>.caseNames()` duplicate-resolution loop.

8. Duplicate/invalid server variable enum names are not covered.
- Missing tests where variable names sanitize to the same enum name and require suffixing.
- Code path: `renderVariables()` duplicate enum-name loop.

9. Server enum default not present in enum list is not covered.
- Missing case where variable default is not in `enum`, forcing fallback to first enum entry.
- Code path: `renderVariables()` `defaultEntryName` fallback.

10. Unresolved URL placeholders are not covered.
- Missing case where server URL contains `{placeholder}` absent from variables; renderer should keep literal placeholder escaped.
- Code path: `toInterpolatedKotlinStringLiteral()` `interpolation == null` branch.

11. Non-standard HTTP methods are not covered.
- Missing test for methods outside GET/POST/PUT/PATCH/DELETE/HEAD/OPTIONS to hit `HttpMethod.ktorFunction()` fallback `HttpMethod("...")`.

12. Cookie ordering interaction is under-covered.
- Cookie-only presence is covered, but missing mixed query+cookie+header ordering case to validate cookie grouped with query as intended.
- Code path: `Route.sortedParameters()` grouping rules.

13. Optional body parameter placement with pre-existing optional params is under-covered.
- Required-body insertion before first optional param is covered; missing optional-body case with mixed required/optional route params.
- Code path: `Route.signatureParameters()` `else` path for optional body.

14. Response model preference order is under-covered.
- Missing response-selection tests for XML > Text > OctetStream fallback when JSON absent.
- Code path: `Route.ReturnType.preferredModel()`.

15. Default response with empty body is not covered.
- Current default-response test uses a non-empty model; missing `default` response where body is empty/Unit.
- Code paths: sealed default case generation and `isEmptyBody()` interplay.

### 2. Union renderer gaps (`UnionRenderer.kt`)

16. Discriminated union with reference case is explicitly missing.
- There is a commented-out test for this scenario.
- File reference: `renderer/src/commonTest/kotlin/io/github/nomisrev/render/UnionRenderSpec.kt` commented block at lines 143-181.

17. Comparator branch for typed additional-properties object cases is not covered.
- Existing test covers `additionalProperties = true` (allowed), but not `AdditionalProperties.Schema` in competing union cases.
- Code path: `unionCaseComparator` priority for `Model.Object.AdditionalProperties.Schema`.

18. Comparator branch including references in overlap situations is under-covered.
- References are tested, but not in an overlap-ambiguity ordering scenario where priority matters against object/primitive/string-like cases.
- Code path: `unionCaseComparator` priority for `Model.Reference`.

19. Top-level context object/enum union case naming branches are under-covered.
- Current object/enum union cases are mostly nested in union context; missing explicit coverage of `context.isTopLevel()` naming branches.
- Code path: `Model.Union.Case.unionClassName()` for top-level object/enum.

20. Nullable union case rendering is not covered.
- Missing unions where one or more cases are nullable, validating wrapper `value` nullability and serializer behavior.
- Code paths: `valueClass()` nullable handling; deserialize/serialize branches.

21. Nested collection naming depth edge cases are under-covered.
- Depth logic for names ending in `s` is complex; only a subset is covered.
- Code path: `unionClassName()` collection suffix generation and `TypeName.name(depth)`.

### 3. Enum renderer gaps (`EnumRender.kt`, `Naming.kt`)

22. JS name sanitization for enum entries is not covered by golden tests.
- Missing case where enum entry requires `@JsName` due to invalid JS identifier.
- Code path: `EnumRender.value()` `ctx.js && valueName.invalidJsName()`.

23. Special enum raw values are not covered.
- Missing golden cases for `*`, `/`, negative numbers, bracketed values, and keyword-like values to verify `toEnumValueName` sanitization/escaping.
- Code path: `toEnumValueName()`.

24. Enum default fallback in client parameter rendering is under-covered.
- Inline enum default is covered for matching default; missing non-matching default fallback to first enum entry.
- Code path: `Model.renderDefault()` for `Model.Enum` fallback.

### 4. Discriminated object renderer gaps (`DiscriminatedObjectRender.kt`)

25. Discriminated object with additional properties is not covered.
- Missing subtype/object cases with `additionalProperties = true` and typed schema, including serializer generation.

26. Discriminator/property naming edge cases are not covered.
- Missing case where discriminator values or inherited property names need escaping/`@SerialName` handling.

27. Nullable discriminated object case is not covered.
- Missing top-level discriminated object with `isNullable = true`.

### 5. Cross-cutting / generation gaps

28. Importer exceptional branch for invalid `Model.Reference` context is not covered.
- No negative golden test for `Model.Reference` that should trigger `IllegalStateException` in `import(model)`.
- Code path: `Importer.kt` `is Model.Reference -> throw IllegalStateException(...)`.

29. `ModelRenderer` unsupported/`TODO` branch is not covered.
- Missing regression test around `Model.Reference` nested discriminated-case path currently marked `TODO()`.
- Code path: `ModelRenderer.kt` `is Model.Reference if context.nested.single() is NamingContext.DiscriminatedObjectCase -> TODO()`.

## Priority recommendations

1. Add missing client tests first (items 1-15) because `ClientRenderer` has the largest branch surface and highest regression risk.
2. Add union discriminator/reference + comparator tests next (items 16-21).
3. Add enum/discriminated edge tests (items 22-27).
4. Add explicit negative tests for known exceptional branches (items 28-29).
