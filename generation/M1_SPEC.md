# Milestone 1 — Bootstrap of KotlinPoet‑free, KMP‑compatible Generator

This specification defines the scope, requirements, technical design, and acceptance criteria for Milestone 1 (M1) of the new code generation module described in PLAN.md. M1 focuses on creating a minimal, Kotlin Multiplatform (KMP)–compatible generation foundation without KotlinPoet, including a small Kotlin declaration IR, a basic emitter, and golden tests.

References:
- Roadmap and milestones: generation/PLAN.md (Sections 1–7, 8, 11)


## 1. Goals and Non‑Goals

Goals (M1):
- Create a new KMP module (working name: `codegen`) that provides:
  - A minimal Kotlin Declaration IR sufficient to express types needed by later milestones.
  - A basic emitter (IR → Kotlin source text) with indentation, imports, identifier escaping, annotations, and simple KDoc rendering.
  - A small set of deterministic formatting rules (no external formatter dependency required).
  - A name/import resolver that avoids collisions and omits redundant imports.
- Provide 5–10 golden tests that assert stable source output from the emitter for canonical declarations.
- Expose a minimal, platform‑agnostic public API for “in‑memory” generation results: GeneratedFile(path, content).

Non‑Goals (M1):
- No KotlinPoet usage.
- No typed→IR transformers (postponed to M2).
- No Gradle plugin integration (postponed to M5).
- No fully featured formatter or YoPSI/AST parity; correctness and determinism over perfect style.


## 2. Module Layout and Build (KMP)

Create a new module `codegen` (exact naming finalized in M5/M7; for M1 we introduce it alongside existing modules):

- codegen/
  - build.gradle.kts: Kotlin Multiplatform setup
    - Targets: commonMain, commonTest, jvmMain (for future I/O hooks but not required in M1), jvmTest
    - Minimal dependencies: Kotlin stdlib; no KotlinPoet
  - src/commonMain/kotlin/
    - api/ (public API definitions: OpenApiCodegen placeholder, GeneratedFile, CodegenOptions placeholder)
    - ir/  (Kotlin declaration IR types)
    - emit/ (emitters for files, declarations, types, docs, annotations)
    - format/ (indentation helpers, simple wrapping)
    - util/ (naming, keywords, escaping, import resolution)
  - src/commonTest/kotlin/
    - golden/ (expected .kt files as resources or inline strings)
    - tests verifying IR → text emission

Gradle considerations:
- Keep dependencies minimal. No ktlint/ktfmt integration in M1 (optional JVM hook deferred).
- Ensure deterministic line separators (use \n) in emitter and tests.


## 3. Public API (Initial Surface)

- data class GeneratedFile(path: String, content: String)
- data class CodegenOptions(
    packageRules: PackageRules = PackageRules.Default,
    namingRules: NamingRules = NamingRules.Default,
    lineWidth: Int = 120,
    indent: String = "    "
  )
  - PackageRules/NamingRules are placeholders in M1 to allow deterministic choices inside emitters; they can be expanded later.
- interface OpenApiCodegen
  - In M1 provide a placeholder interface in api/ but no concrete implementation (actual OpenAPI transformers come in M2+). The emitter is test‑driven via IR directly.

Note: Although OpenApiCodegen is not implemented in M1, exposing its signature keeps future integration straightforward and avoids API churn.


## 4. Kotlin Declaration IR (Scope for M1)

Design principles:
- Minimal but expressive enough to cover canonical declarations for golden tests.
- Use sealed hierarchies where useful; prefer simple data classes.
- Preserve only information necessary to render readable, compilable Kotlin with deterministic formatting.

Core types (M1):
- KtFile(
    name: String,              // e.g., "Models.kt"
    pkg: String?,              // package name or null for default
    imports: List<KtImport>,   // explicit imports requested by IR or resolver
    declarations: List<KtDeclaration>,
    kdoc: KtKDoc? = null
  )
- KtImport(qualifiedName: String, alias: String? = null)
- KtDeclaration (sealed interface):
  - KtTypeAlias(name: String, type: KtType, annotations: List<KtAnnotation> = emptyList(), kdoc: KtKDoc? = null, modifiers: List<KtModifier> = emptyList())
  - KtClass(
      name: String,
      kind: KtClassKind,                   // Class, Interface, Object, Enum
      visibility: KtVisibility = Public,
      modifiers: List<KtModifier> = emptyList(),
      typeParams: List<KtTypeParam> = emptyList(),
      primaryCtor: KtPrimaryConstructor? = null, // For data classes, etc.
      properties: List<KtProperty> = emptyList(),
      functions: List<KtFunction> = emptyList(),
      companion: KtClass? = null,          // For companion object
      superTypes: List<KtType> = emptyList(),
      annotations: List<KtAnnotation> = emptyList(),
      kdoc: KtKDoc? = null
    )
  - KtFunction(
      name: String,
      params: List<KtParam>,
      returnType: KtType?,                 // null for Unit
      typeParams: List<KtTypeParam> = emptyList(),
      modifiers: List<KtModifier> = emptyList(),
      annotations: List<KtAnnotation> = emptyList(),
      kdoc: KtKDoc? = null,
      body: KtBlock? = null                // raw text for M1
    )
  - KtProperty(
      name: String,
      type: KtType,
      mutable: Boolean = false,
      initializer: KtExpr? = null,         // raw text for M1
      modifiers: List<KtModifier> = emptyList(),
      annotations: List<KtAnnotation> = emptyList(),
      kdoc: KtKDoc? = null
    )
  - KtEnumEntry(name: String, args: List<KtExpr> = emptyList(), annotations: List<KtAnnotation> = emptyList(), kdoc: KtKDoc? = null)

Support types:
- KtType: sealed
  - KtType.Simple(qualifiedName: String, nullable: Boolean = false)
  - KtType.Generic(raw: KtType.Simple, args: List<KtType>, nullable: Boolean = false)
  - KtType.Function(params: List<KtType>, returnType: KtType, nullable: Boolean = false)
- KtParam(name: String, type: KtType, default: KtExpr? = null, annotations: List<KtAnnotation> = emptyList())
- KtTypeParam(name: String, bounds: List<KtType> = emptyList(), variance: KtVariance? = null, reified: Boolean = false)
- KtPrimaryConstructor(params: List<KtParam>, visibility: KtVisibility = Public)
- KtAnnotation(name: KtType.Simple, args: Map<String?, KtExpr> = emptyMap())
- KtKDoc(lines: List<String>)
- KtModifier: enum (Data, Inline, Value, Sealed, Open, Final, Abstract, Override, Lateinit, Const, Companion)
- KtVisibility: enum (Public, Internal, Private, Protected)
- KtVariance: enum (In, Out)
- KtExpr: raw string holder
- KtBlock: raw string holder

Constraints (M1):
- Raw textual KtExpr/KtBlock accepted; no parsing or formatting of expressions.
- IR must not require symbol resolution; imports can be provided explicitly or inferred from qualified names.


## 5. Emitter & Formatting (M1)

Responsibilities:
- Deterministically render KtFile → Kotlin source text.
- Manage indentation via a simple stack or integer depth and a configurable indent string.
- Render package and imports at the top:
  - Omit imports of kotlin.* that are unnecessary (configurable: default true for kotlin.* root, except kotlin.collections.* when types are used explicitly by FQN policy).
  - Avoid duplicate imports; sort imports lexicographically.
- Identifier escaping:
  - Escape Kotlin keywords with backticks.
  - Provide a small set of reserved words (use Kotlin keyword list) and a utility util/Keywords.kt.
- Annotation/KDoc rendering:
  - Render annotations in order of appearance.
  - Render KDoc from KtKDoc by prefixing with /** and wrapping lines with *.
- Class rendering rules:
  - Modifiers/visibility order: visibility, modifers, kind/data as applicable.
  - Primary constructor rendering for data classes, with params separated by commas, respecting defaults.
  - Super types: “: A, B” list; if constructor call text is needed, allow KtExpr in super type later (defer for M2+).
- Functions/properties:
  - For Unit returnType, omit explicit “: Unit” unless explicitly set.
  - For body null: render as declaration without body (semicolonless).
  - For body present: render block text verbatim within braces maintaining indent.

Line wrapping:
- Keep simple: no automatic wrapping beyond ensuring newlines between top‑level declarations and after package/import blocks.


## 6. Import Resolution (M1)

- Provide util/ImportResolver that can:
  - Collect types referenced by IR (from KtType.Simple/Generic) when provided as qualified names.
  - Decide whether to import or to render fully qualified based on current package and conflicts.
  - For M1, a simplified rule: if the simple name is unique among imports and not in current package, import it; otherwise use FQN or alias if the same simple name appears twice from different packages.
- Alias handling: If conflict is detected for the same simple name, prefer FQN; aliasing support may be added but can be skipped in M1 if it simplifies logic (documented).


## 7. Testing Strategy (M1)

Golden tests (5–10 cases). For each test:
- Build IR programmatically.
- Emit Kotlin source text.
- Compare to expected string literal (or load from resource file) using strict equality.

Suggested coverage:
1) File with package and imports + a simple class with a property.
2) Data class with primary constructor, default values, and KDoc.
3) Enum class with entries and annotations.
4) Type alias to a generic type (e.g., Map<String, Int?>).
5) Function with parameters, annotations, and a simple body block.
6) Object singleton with property and function.
7) Class implementing interfaces (super types) and visibility/modifier ordering.
8) Identifier escaping for keywords (e.g., property named "object").
9) Rendering nullable and generic types, including nested generics.
10) Deterministic import sorting and de‑duplication.

Testing utilities:
- Assert helper that normalizes line endings to "\n" before compare.
- Optionally snapshot/golden via string resources colocated with tests.


## 8. Performance, Determinism, and Compatibility

- Performance: Use Appendable/StringBuilder; avoid unnecessary allocations in hot paths (e.g., one pass per declaration where possible).
- Determinism: Sort imports; maintain stable ordering of declarations as provided; ensure consistent whitespace and newline usage.
- Platform compatibility: All emitters and IR live in commonMain; no JVM‑only APIs.


## 9. Logging/Diagnostics

- In M1, add simple debug hooks (disabled by default) that can be turned on in tests via a flag in CodegenOptions for troubleshooting emission issues (e.g., include [DEBUG_LOG] markers on demand).
- No external logging dependency.


## 10. Work Breakdown (Implementation Steps)

1) Module scaffolding (codegen KMP). Define build.gradle.kts and source sets.
2) Define IR types (Section 4).
3) Implement emitter utilities (indentation, identifier escaping, kdoc render).
4) Implement import resolver and file emitter (Section 5/6).
5) Implement declaration/type renderers (class, function, property, typealias, enum entry).
6) Add golden tests (Section 7); ensure at least 5 tests initially.
7) Documentation: update generation/README.MD to link to this spec (optional in M1; README already links PLAN.md).


## 11. Acceptance Criteria

- A new module `codegen` exists and builds for common and JVM targets.
- IR types compile in commonMain.
- Emitter renders compilable Kotlin for the covered IR (validated by tests; JVM compilation of emitted text is not required in M1 but recommended as a future enhancement).
- Import resolver de‑duplicates and sorts imports; avoids importing current package; avoids unnecessary kotlin.* imports.
- 5–10 golden tests pass consistently on all supported platforms for tests.
- No KotlinPoet dependency anywhere in the new module.


## 12. Risks and Mitigations (M1)

- Formatting quality: Keep rules simple; add JVM hook for external formatter in later milestones.
- Import conflicts: Prefer FQN over aliasing in M1 to keep logic simple; revisit in later milestones.
- Scope creep: Typed→IR and plugin integration explicitly deferred to M2+ and M5.


## 13. Future Work (Out of Scope for M1)

- M2: Implement typed→IR transformers for primitives, data classes, enums.
- M3: API surfaces (client interfaces, requests/responses) and simple E2E compile test.
- M4: Serialization annotations and union handling.
- M5: Gradle plugin integration and file writing on JVM.
- M6: Default switch; parity.
- M7: Cleanup and KotlinPoet remnants removal.
