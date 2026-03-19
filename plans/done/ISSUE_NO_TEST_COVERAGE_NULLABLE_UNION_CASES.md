# ISSUE: No Test Coverage for Nullable Union Cases

## Problem

The existing test suite for union rendering has **zero tests** for nullable union cases. All test data in `renderer/src/jvmTest/resources/kotlinTestData/union/` uses only non-nullable cases.

Nullable tests exist for:
- Collections (`collection/nullable/`)
- Enums (`enum/nullable/`)
- Objects (nullable properties in ObjectSpec)

But **not** for unions, despite unions being one of the most complex serialization patterns.

## Missing Test Scenarios

### Non-discriminated unions:
1. All cases nullable: `oneOf: [{ type: string, nullable: true }, { type: integer, nullable: true }]`
2. Mixed nullable/non-nullable cases
3. Nullable collection case: `oneOf: [{ type: array, nullable: true, items: { type: string } }, ...]`
4. Nullable reference case: `oneOf: [{ $ref: "#/.../Foo", nullable: true }, ...]`
5. Nullable union itself: `{ nullable: true, oneOf: [...] }`
6. Nullable union with nullable cases (compound nullability)

### Discriminated unions:
7. Nullable discriminated union: `{ nullable: true, discriminator: { ... }, oneOf: [...] }`
8. Discriminated union with nullable case models

### Open enum pattern:
9. Nullable open enum: `{ nullable: true, oneOf: [{ enum: [...] }, { type: string }] }`

### Usage contexts:
10. Nullable union as required object property
11. Nullable union as optional object property
12. Nullable union as request/response body
13. Nullable union as query/path parameter

## Affected Code

- `renderer/src/jvmTest/kotlin/io/github/nomisrev/render/UnionSpec.kt` (or equivalent test file)
- `renderer/src/jvmTest/resources/kotlinTestData/union/` (test data directory)

## Fix

Add test cases for each scenario above. Each test should:
1. Define an OpenAPI schema with nullable union cases
2. Generate Kotlin code
3. Verify the generated code matches expected output (including `.nullable` on serializers, `?` on types)
4. Ideally, compile and run roundtrip serialization tests
