# YouTrack Generated API Client -- Shortcomings

## 1. CRITICAL: Empty Write Variants

Many Write variants have been stripped of ALL properties, leaving behind empty `data object`s that cannot carry any data. This makes write operations impossible for these types.

**Affected types:**

| Write Type | Subtypes affected | Impact |
|---|---|---|
| `UserWrite` | All 3 (`Default`, `Me`, `VcsUnresolvedUser`) are empty `data object`s | Cannot assign users to issues, set reporters, set work item authors |
| `UserGroupWrite` | All 5 subtypes are empty `data object`s | Cannot set visibility groups, team membership |
| `DurationValueWrite` | Empty `data object` | Cannot set work item duration |
| `PeriodValueWrite` | Empty `data object` | Cannot set period custom field values |
| `TextFieldValueWrite` | Empty `data object` | Cannot set text custom field values |
| `FieldStyleWrite` | Empty `data object` | Cannot set bundle element colors |
| `IssueCustomFieldWrite` | `SimpleIssueCustomField`, `DateIssueCustomField`, `SingleValueIssueCustomField`, `MultiValueIssueCustomField`, `StateMachineIssueCustomField` are empty | Cannot set values for most custom field types when writing |
| `CommandVisibilityWrite` | `CommandLimitedVisibility` is empty | Cannot specify command visibility |

**Root cause**: The generator strips all `readOnly` properties when producing Write variants. For types where ALL concrete properties are readOnly (like `id`, `login`, `email` on User), the Write variant collapses to an empty type. But the YouTrack API uses a `{"$type": "...", "id": "..."}` reference pattern -- clients need at least `id` on Write variants to reference existing entities.

---

## 2. HIGH: `JsonElement` on Intermediate Types in `allOf` Hierarchies

When the OpenAPI spec declares `"type": "object"` with no properties on an **intermediate** schema, and leaf schemas refine it to a concrete type via `allOf`, the generator correctly uses `JsonElement` for the intermediate but the leaf types ARE properly typed.

**This is correct behavior** -- `"type": "object"` with no properties is genuinely untyped, and `JsonElement` is the safest representation. However, it means users who deserialize against an intermediate type (e.g., `CreatedDeletedActivityItem` instead of `AttachmentActivityItem`) get `JsonElement` fields they must parse manually.

**Intermediate types with `JsonElement` fields (all `readOnly`, spec says `"type": "object"`):**

| Intermediate Type | Fields as `JsonElement` | Leaf types that refine them |
|---|---|---|
| `ActivityItem.CreatedDeletedActivityItem` | `added`, `removed`, `target` | AttachmentActivityItem, CommentActivityItem, etc. (all properly typed) |
| `ActivityItem.SingleValueActivityItem` | `added`, `removed`, `target` | ProjectActivityItem, WorkItemAuthorActivityItem, etc. (typed) |
| `ActivityItem.MultiValueActivityItem` | `added`, `removed`, `target` | LinksActivityItem, TagsActivityItem, etc. (typed) |
| `ActivityItem.SimpleValueActivityItem` | `added`, `removed`, `target` | IssueResolvedActivityItem (Long), TextMarkupActivityItem (String), etc. |
| `ActivityItem.VisibilityActivityItem` | `added`, `removed`, `target` | VisibilityGroupActivityItem (UserGroup[]), VisibilityUserActivityItem (User[]) |
| `ActivityItem.CustomFieldActivityItem` | `added`, `removed` | TextCustomFieldActivityItem (String) |

**IssueCustomField intermediate types -- also correct:**

| Intermediate Type | `value` field | Leaf types that refine it |
|---|---|---|
| `SimpleIssueCustomField` | `JsonElement?` (spec: `"type": "object"`) | DateIssueCustomField (also `"type": "object"`) |
| `DateIssueCustomField` | `JsonElement?` (spec: `"type": "object"`) | (leaf) |
| `DatabaseSingleValueIssueCustomField` | `JsonElement?` (spec: `"type": "object"`) | SingleEnumIssueCustomField (EnumBundleElement), etc. (typed) |
| `DatabaseMultiValueIssueCustomField` | `JsonElement?` (spec: `"type": "object"`) | MultiEnumIssueCustomField (EnumBundleElement[]), etc. (typed) |
| `StateMachineIssueCustomField` | `JsonElement?` (spec: `"type": "object"`) | (leaf -- no further refinement in spec) |

**Other types:**
| Type | Field | Spec definition |
|---|---|---|
| `IssueFolderRead.Project` | `customFields: JsonElement?` | Spec: `"type": "object", "readOnly": true` -- correctly `JsonElement` |

**The real concern** is not correctness but usability: since these intermediate types are valid `$type` discriminator values (self-referencing mappings), the API could return them. Users matching on intermediates must manually parse `JsonElement` fields that would be typed on the leaf. This is a spec-level issue, not a generator bug.

**Exceptions -- `target` on some leaf types is still `JsonElement`:**

A few leaf types inherit `target` from an intermediate but the spec does not refine it:
- `VcsChangeActivityItem.target` -- spec does not define `target`, inherits `JsonElement` from `CreatedDeletedActivityItem`

This appears to be a spec omission rather than a generator issue.

---

## 3. HIGH: Unused Base Types (Three-Tier Duplication)

Every type with any readOnly property gets three copies: base (`User`), Read (`UserRead`), Write (`UserWrite`). The base types are not used by any API operation or by other Read/Write types -- they are dead code.

Examples:
- `User` (37 lines) / `UserRead` (92 lines) / `UserWrite` (25 lines)
- `UserGroup` (51 lines) / `UserGroupRead` (91 lines) / `UserGroupWrite` (33 lines)
- `IssueFolder` (100 lines) / `IssueFolderRead` (110 lines) / `IssueFolderWrite` (85 lines)
- `Bundle` (68 lines) / `BundleRead` (85 lines) / `BundleWrite` (68 lines)

The base variants reference other base variants (e.g., `User` references `UserProfiles`), creating a parallel type graph that is never exercised.

---

## 4. HIGH: No Forward-Compatible Deserialization

- `@SerialName("Default")` is used for fallback subtypes of sealed interfaces. If the YouTrack API sends a `$type` value that doesn't match any known subtype, kotlinx.serialization will throw an exception rather than falling back to `Default`.
- Enums like `IssueLink.Direction` (`OUTWARD`, `INWARD`, `BOTH`) have no `UNKNOWN` fallback. New API values will cause hard deserialization failures.

---

## 5. LOW: `IssueWrite` Missing `customFields` (Spec Limitation)

`IssueRead` has `customFields: List<IssueCustomFieldRead>?`, but `IssueWrite` does not have a `customFields` property. The generator is **correct** here -- the spec marks `Issue.customFields` as `readOnly: true`. Users must use the separate `POST /issues/{id}/customFields/{customFieldId}` endpoint.

This is a spec-level limitation, not a generator bug. The YouTrack API may accept `customFields` inline in practice, but the spec does not declare it as writable.

---

## 6. MEDIUM: Value Classes in Sealed Polymorphic Hierarchies

The generator uses `@JvmInline value class` for sealed interface subtypes with a single property:
- `VisibilityRead.Default(val id: String?)`
- `ColorCodingRead.Default(val id: String?)`
- `BaseWorkItemRead.Default(val id: String?)`

kotlinx.serialization of value class subtypes within `@JsonClassDiscriminator` sealed hierarchies is fragile and may cause runtime serialization failures.

---

## 7. MEDIUM: Inconsistent Bundle Element Typing

Some `BundleRead` subtypes use the specific element type:
- `StateBundle.values: List<BundleElementRead.StateBundleElement>?`
- `EnumBundle.values: List<BundleElementRead.EnumBundleElement>?`

But others use the generic base type:
- `OwnedBundle.values: List<BundleElementRead>?` (should be `OwnedBundleElement`)
- `VersionBundle.values: List<BundleElementRead>?` (should be `VersionBundleElement`)
- `BuildBundle.values: List<BundleElementRead>?` (should be `BuildBundleElement`)

---

## 8. MEDIUM: `IssueLink` Has No Read/Write Split

`IssueLink` is a plain data class referencing `IssueLinkTypeRead` and `IssueRead`. There is no `IssueLinkWrite` variant. Since it references Read types, it cannot be used for constructing write payloads consistently with the Read/Write pattern.

---

## 9. LOW: All Properties Nullable with `null` Defaults

Every property on every model is `nullable` with a default of `null`. While this reflects YouTrack's field-selection behavior (any field might be absent), it means:
- Required fields for write operations (`summary`, `project` for issue creation) are not distinguished from optional ones.
- Construction of Write types is verbose and error-prone.

---

## 10. LOW: Long Inline Default Strings for `fields` Parameter

Every API operation has a `fields` parameter with very long default values like:
```
"$type,created,customFields($type,id,name,value($type,id,name)),description,id,idReadable,..."
```

These are hardcoded as parameter defaults. They should be extractable constants so users can compose field selections.

---

## Summary by Priority

| # | Priority | Issue | Generator Bug? |
|---|---|---|---|
| 1 | CRITICAL | Empty Write variants -- User, UserGroup, DurationValue, PeriodValue, TextFieldValue, FieldStyle, many IssueCustomField subtypes collapse to empty `data object`s | Yes -- need at least `id` for reference pattern |
| 2 | HIGH | `JsonElement` on intermediate `allOf` types (ActivityItem, IssueCustomField intermediates) | No -- spec says `"type": "object"`. Correct but hurts usability when API returns intermediates |
| 3 | HIGH | Dead base types (three-tier Base/Read/Write duplication) | Yes -- base types are unreferenced |
| 4 | HIGH | No forward-compatible deserialization (unknown `$type` / enum values crash) | Yes |
| 5 | MEDIUM | Value classes in sealed polymorphic hierarchies | Yes -- fragile with kotlinx.serialization |
| 6 | MEDIUM | Inconsistent bundle element typing (OwnedBundle, VersionBundle, BuildBundle use generic `BundleElementRead` instead of specific types) | Yes -- spec says specific types |
| 7 | MEDIUM | IssueLink has no Read/Write split | Borderline -- follows spec but inconsistent with pattern |
| 8 | LOW | IssueWrite missing customFields | No -- spec marks it `readOnly` |
| 9 | LOW | All properties nullable | No -- matches spec (field selection) |
| 10 | LOW | Long inline field defaults | Style issue |
