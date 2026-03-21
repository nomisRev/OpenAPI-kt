# YouTrack OpenAPI Models -- Mental Map

## Global Patterns

- **Discriminator**: Every polymorphic schema uses `"$type"` as discriminator property (always `readOnly: true`).
- **Inheritance**: Uses `allOf`-based inheritance -- child schemas reference their parent via `allOf: [{ "$ref": "Parent" }, { properties: {...} }]`. **No `oneOf` or `anyOf`** is used anywhere.
- **Self-referencing mappings**: Every hierarchy includes the base type itself in the discriminator mapping -- the base type is a valid concrete `$type` value.
- **Flat mappings**: Discriminator mappings are flat -- they list ALL types in the hierarchy regardless of nesting depth (intermediates and leaves alike).
- **No writeOnly properties**: The spec contains zero `writeOnly` properties.
- **No required fields**: No schema defines `required` fields. All fields are optional.
- **Standalone `$type` types**: 75 schemas carry `discriminator: { propertyName: "$type" }` with no `mapping` (e.g., `Issue`, `Sprint`, `Agile`). They still include `$type` in JSON but have no subtypes.

**Total**: 218 schemas, 123 using `allOf` inheritance, 20 discriminated hierarchies, 75 standalone `$type` types.

---

## The 20 Discriminated Hierarchies

### 1. ActivityItem (27 variants) -- READ-ONLY

**Usage**: `GET /activities`, `GET /activities/{id}`, `GET /issues/{id}/activities` -- response only, never in request bodies.

```
ActivityItem
  CreatedDeletedActivityItem
    AttachmentActivityItem          -- added/removed: IssueAttachment[]
    CommentActivityItem             -- added/removed: IssueComment[]
    IssueCreatedActivityItem        -- added/removed: Issue[]
    VcsChangeActivityItem           -- added/removed: VcsChange[]
    WorkItemActivityItem            -- added/removed: IssueWorkItem[]
  CustomFieldActivityItem           -- added/removed: object
    TextCustomFieldActivityItem     -- added/removed: string, +markup
  MultiValueActivityItem            -- added/removed: object
    CommentAttachmentsActivityItem  -- added/removed: IssueAttachment[]
    LinksActivityItem               -- added/removed: IssueLink[]
    SprintActivityItem              -- added/removed: Sprint[]
    TagsActivityItem                -- added/removed: IssueTag[]
    VisibilityActivityItem          -- added/removed: object
      VisibilityGroupActivityItem   -- added/removed: UserGroup[]
      VisibilityUserActivityItem    -- added/removed: User[]
    VotersActivityItem              -- added/removed: User[]
    WorkItemTypeActivityItem        -- added/removed: WorkItemType[]
  SingleValueActivityItem           -- added/removed: object
    ProjectActivityItem             -- added/removed: Project
    SimpleValueActivityItem         -- added/removed: object
      IssueResolvedActivityItem     -- added/removed: int64
      TextMarkupActivityItem        -- added/removed: string, +markup
      UsesMarkupActivityItem        -- added/removed: boolean, +markup
    WorkItemAuthorActivityItem      -- added/removed: User
    WorkItemDurationActivityItem    -- added/removed: DurationValue
```

**Base properties**: `id`, `added`, `removed`, `author` (User), `category` (ActivityCategory), `field` (FilterField), `target`, `targetMember`, `timestamp`.

**Key insight**: The spec declares `added`/`removed`/`target` as `"type": "object"` (no properties) on intermediate types. Leaf types refine them to concrete types via `allOf`. This means intermediate types are genuinely untyped in the spec -- a code generator correctly maps them to `JsonElement`. Only the leaf variants have typed fields.

**Example JSON (issue created):**
```json
{
  "$type": "IssueCreatedActivityItem",
  "id": "2-0.0-0",
  "timestamp": 1609459200000,
  "author": {"$type": "User", "id": "1-5", "login": "admin"},
  "target": {"$type": "Issue", "id": "2-1", "idReadable": "PROJ-1"},
  "field": {"$type": "PredefinedFilterField", "id": "created"},
  "category": {"$type": "ActivityCategory", "id": "IssueCreatedCategory"},
  "added": [{"$type": "Issue", "id": "2-1"}],
  "removed": []
}
```

---

### 2. IssueCustomField (21 variants) -- MIXED read/write

**Usage**: `GET/POST /issues/{id}/customFields/{customFieldId}` -- both request and response.

**NOTE**: Two discriminator value mismatches:
- `"SingleValueIssueCustomField"` maps to schema `DatabaseSingleValueIssueCustomField`
- `"MultiValueIssueCustomField"` maps to schema `DatabaseMultiValueIssueCustomField`

```
IssueCustomField
  DatabaseMultiValueIssueCustomField  ($type: "MultiValueIssueCustomField")
    MultiBuildIssueCustomField      -- value: BuildBundleElement[]
    MultiEnumIssueCustomField       -- value: EnumBundleElement[]
    MultiGroupIssueCustomField      -- value: UserGroup[]
    MultiOwnedIssueCustomField      -- value: OwnedBundleElement[]
    MultiUserIssueCustomField       -- value: User[]
    MultiVersionIssueCustomField    -- value: VersionBundleElement[]
  DatabaseSingleValueIssueCustomField ($type: "SingleValueIssueCustomField")
    SingleBuildIssueCustomField     -- value: BuildBundleElement
    SingleEnumIssueCustomField      -- value: EnumBundleElement
    SingleGroupIssueCustomField     -- value: UserGroup
    SingleOwnedIssueCustomField     -- value: OwnedBundleElement
    SingleUserIssueCustomField      -- value: User
    SingleVersionIssueCustomField   -- value: VersionBundleElement
    StateIssueCustomField           -- value: StateBundleElement
    StateMachineIssueCustomField    -- value: object (+event, +possibleEvents)
  PeriodIssueCustomField            -- value: PeriodValue
  SimpleIssueCustomField            -- value: object (string/number/boolean)
    DateIssueCustomField            -- value: object (int64 timestamp)
  TextIssueCustomField              -- value: TextFieldValue
```

**Base properties**: `id` [RO], `name` [RO], `projectCustomField` [RO], `value` [RO on base].
**Only writable property**: `value` on concrete leaf types.

**Example JSON (set priority):**
```json
{
  "$type": "SingleEnumIssueCustomField",
  "id": "92-1",
  "name": "Priority",
  "value": {"$type": "EnumBundleElement", "id": "67-1", "name": "Critical"}
}
```

---

### 3. ProjectCustomField (12 variants) -- MIXED read/write

**Usage**: `GET/POST /admin/projects/{id}/customFields`

```
ProjectCustomField
  BundleProjectCustomField
    BuildProjectCustomField     -- bundle: BuildBundle, defaultValues: BuildBundleElement[]
    EnumProjectCustomField      -- bundle: EnumBundle, defaultValues: EnumBundleElement[]
    OwnedProjectCustomField     -- bundle: OwnedBundle, defaultValues: OwnedBundleElement[]
    StateProjectCustomField     -- bundle: StateBundle, defaultValues: StateBundleElement[]
    UserProjectCustomField      -- bundle: UserBundle
    VersionProjectCustomField   -- bundle: VersionBundle, defaultValues: VersionBundleElement[]
  GroupProjectCustomField
  PeriodProjectCustomField
  SimpleProjectCustomField
    TextProjectCustomField
```

**Base writable**: `canBeEmpty`, `emptyFieldText`, `isPublic`, `ordinal`, `condition`.
**Base readOnly**: `id`, `field`, `project`, `hasRunningJob`.

---

### 4. CustomFieldDefaults (8 variants) -- MIXED read/write

```
CustomFieldDefaults
  BundleCustomFieldDefaults
    BuildBundleCustomFieldDefaults   -- bundle: BuildBundle, defaultValues: BuildBundleElement[]
    EnumBundleCustomFieldDefaults    -- bundle: EnumBundle, defaultValues: EnumBundleElement[]
    OwnedBundleCustomFieldDefaults   -- bundle: OwnedBundle, defaultValues: OwnedBundleElement[]
    StateBundleCustomFieldDefaults   -- bundle: StateBundle, defaultValues: StateBundleElement[]
    VersionBundleCustomFieldDefaults -- bundle: VersionBundle, defaultValues: VersionBundleElement[]
  UserCustomFieldDefaults
```

**Base writable**: `canBeEmpty`, `emptyFieldText`, `isPublic`.
**Base readOnly**: `parent` (CustomField).

---

### 5. Bundle (8 variants) -- MIXED read/write

```
Bundle
  BaseBundle
    BuildBundle     -- values: BuildBundleElement[]
    EnumBundle      -- values: EnumBundleElement[]
    OwnedBundle     -- values: OwnedBundleElement[]
    StateBundle     -- values: StateBundleElement[]
    VersionBundle   -- values: VersionBundleElement[]
  UserBundle        -- groups: UserGroup[], individuals: User[], aggregatedUsers [RO]
```

**Base**: `id` [RO], `isUpdateable` [RO].

---

### 6. BundleElement (7 variants) -- MIXED read/write

```
BundleElement
  BuildBundleElement          -- +assembleDate
  LocalizableBundleElement
    EnumBundleElement         -- +localizedName
    StateBundleElement        -- +localizedName, +isResolved
  OwnedBundleElement          -- +owner: User
  VersionBundleElement        -- +released, +releaseDate, +startDate
```

**Base writable**: `name`, `description`, `archived`, `ordinal`, `color` (FieldStyle).
**Base readOnly**: `id`, `bundle`, `hasRunningJob`.

---

### 7. Visibility (3 variants) -- MIXED read/write

**Usage**: Referenced from Issue, IssueComment, Article, ArticleComment, IssueAttachment, ArticleAttachment.

```
Visibility
  LimitedVisibility       -- +permittedGroups: UserGroup[], +permittedUsers: User[]
  UnlimitedVisibility     -- no additional properties
```

**Example JSON:**
```json
{"$type": "LimitedVisibility", "permittedGroups": [{"$type": "UserGroup", "id": "1-1"}]}
{"$type": "UnlimitedVisibility"}
```

---

### 8. CommandVisibility (3 variants) -- READ-ONLY

```
CommandVisibility
  CommandLimitedVisibility     -- +permittedGroups, +permittedUsers [RO]
  CommandUnlimitedVisibility   -- no additional properties
```

---

### 9. IssueFolder (6 variants) -- MIXED read/write

```
IssueFolder
  Project               -- shortName, description, leader, archived, issues, etc.
  WatchFolder
    SavedQuery          -- +query
    Tag                 -- +color, +issues, +untagOnResolve
      IssueTag          -- no additional properties
```

**Base writable**: `name`.
**Base readOnly**: `id`.

---

### 10. User (3 variants) -- READ-ONLY

```
User
  Me                    -- no additional properties
  VcsUnresolvedUser     -- +name
```

**All readOnly**: `id`, `login`, `fullName`, `email`, `avatarUrl`, `banned`, `guest`, `online`, `ringId`, `profiles`, `savedQueries`, `tags`.

---

### 11. UserGroup (5 variants) -- READ-ONLY

```
UserGroup
  AllUsersGroup            -- no additional
  NestedGroup              -- no additional
  ProjectTeam              -- +project: Project
  RegisteredUsersGroup     -- no additional
```

**All readOnly**: `id`, `name`, `ringId`, `icon`, `allUsersGroup`, `teamForProject`, `usersCount`.

---

### 12. BaseArticle (2 variants)

```
BaseArticle
  Article    -- +childArticles, +comments, +hasStar, +parentArticle, +tags, +created [RO], +updated [RO], etc.
```

---

### 13. BaseWorkItem (2 variants)

```
BaseWorkItem
  IssueWorkItem    -- +author, +text, +type, +created, +updated, +duration, +date (writable); +id, +creator, +issue [RO]
```

---

### 14. ColorCoding (3 variants)

```
ColorCoding
  FieldBasedColorCoding      -- +prototype: CustomField
  ProjectBasedColorCoding    -- +projectColors: ProjectColor[]
```

---

### 15. CustomFieldCondition (2 variants)

```
CustomFieldCondition
  FieldBasedCondition    -- +field, +values, +showForNullValue
```

---

### 16. DatabaseAttributeValue (3 variants)

```
DatabaseAttributeValue
  AgileColumnFieldValue           -- +name, +isResolved [RO]
  SwimlaneEntityAttributeValue    -- +name, +isResolved [RO]
```

---

### 17. FilterField (3 variants) -- READ-ONLY

```
FilterField
  CustomFilterField        -- +customField: CustomField
  PredefinedFilterField    -- no additional
```

---

### 18. SwimlaneSettings (3 variants)

```
SwimlaneSettings
  AttributeBasedSwimlaneSettings  -- +field, +values
  IssueBasedSwimlaneSettings      -- +field, +defaultCardType, +values
```

---

### 19. ChangesProcessor (11 variants)

```
ChangesProcessor
  JenkinsChangesProcessor
  TeamcityChangesProcessor
  VcsHostingChangesProcessor
    BitBucketChangesProcessor
    BitbucketStandaloneChangesProcessor
    GitHubChangesProcessor
    GitLabChangesProcessor
    GiteaChangesProcessor
    GogsChangesProcessor
    SpaceChangesProcessor
```

**Base writable**: `project`, `relatedProjects`, `enabled`, `visibleForGroups`, `addComments`, `lookupIssuesInBranchName`.
**VcsHosting adds**: `path`, `branchSpecification`, `committers`.

---

### 20. VcsServer (11 variants)

```
VcsServer
  JenkinsServer
  TeamcityServer
  VcsHostingServer
    BitBucketServer / BitbucketStandaloneServer / GitHubServer / GitLabServer / GiteaServer / GogsServer / SpaceServer
```

**Base**: `id` [RO], `url` (writable). Subtypes add no unique properties.

---

## Types Needed for an API Client

### Read-only hierarchies (response types only)
| Type | Variants | Used in |
|---|---|---|
| ActivityItem | 27 | GET activities |
| CommandVisibility | 3 | Parsed command results |
| FilterField | 3 | Activity field metadata |
| User | 3 | User endpoints, embedded refs |
| UserGroup | 5 | Group endpoints, embedded refs |

### Read/Write hierarchies (request + response)
| Type | Variants | Used in |
|---|---|---|
| IssueCustomField | 21 | Issue custom field CRUD |
| ProjectCustomField | 12 | Admin project custom field CRUD |
| CustomFieldDefaults | 8 | Admin field defaults CRUD |
| Bundle | 8 | Bundle CRUD (via custom fields) |
| BundleElement | 7 | Bundle element CRUD |
| Visibility | 3 | Issue/comment/article visibility |
| IssueFolder | 6 | Project, saved query, tag operations |
| ColorCoding | 3 | Agile board color coding |
| SwimlaneSettings | 3 | Agile board swimlane config |
| ChangesProcessor | 11 | VCS integration config |
| VcsServer | 11 | VCS server config |

### Reference types (embedded, used by ID in writes)
These types appear as properties inside writable types. When writing, clients typically send `{"$type": "...", "id": "..."}` to reference an existing entity:
- `User` -- e.g., assign to issue, set reporter, set work item author
- `UserGroup` -- e.g., set visibility groups
- `BundleElement` subtypes -- e.g., set custom field value to an enum element
- `IssueFolder.Project` -- e.g., set issue project

### The `$type` + `id` reference pattern
The YouTrack API uses a consistent pattern for referencing existing entities in write operations:
```json
{"$type": "EnumBundleElement", "id": "67-1"}
```
Even though most properties on `User`, `UserGroup`, etc. are `readOnly`, the client still needs to construct instances with at least `$type` and `id` for write operations.
