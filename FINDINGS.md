# Findings: GitHub integration diff after Phase 10 entrypoint switch

## Scope

This review covers the generated changes under `integration-tests/github` after the Phase 10 entrypoint switch.

I reviewed:

- the pipeline changes in `typed`
- the generated Kotlin diff in `integration-tests/github`
- the GitHub OpenAPI source in `parser/src/commonTest/resources/specs/github.json`
- the request/response usage of the affected schemas in the generated APIs

## Executive summary

Most of the churn is real.

It is not random.

The new pipeline now threads request/response context through nested schema resolution much more consistently.

That makes three kinds of changes appear:

1. genuinely correct `Read`/`Write` splits in API signatures and nested models
2. conservative `Read`/`Write` duplication for context-neutral leaf schemas
3. stale generated files left in `src/main/kotlin`, which makes the diff look much larger than the semantic change

## Size of the change

- `48` tracked generated files changed
- `116` generated files are newly untracked
- `39` tracked unsuffixed generated models are now orphaned because the generated code references `*Read` / `*Write` instead
- `13` tracked `*Read` / `*Write` aliases are now orphaned because the new output references a neutral unsuffixed alias instead

The stale-file problem is real.

The generator writes directly into `integration-tests/github/src/main/kotlin`.

That directory is not cleaned before regeneration.

## What changed in the pipeline

The Phase 10 switch changed nested schema resolution from the old dispatcher to the engine.

This matters because nested properties, collections, unions, `allOf`, `anyOf`, `oneOf`, and additional-properties schemas now go through the same context-aware engine path as top-level schemas.

Relevant code changes:

- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/ApiTree.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/registry/Registry.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Object.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Collection.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Union.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/AllOf.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/Enum.kt`
- `typed/src/commonMain/kotlin/io/github/nomisrev/openapi/transformers/DiscriminatedObject.kt`

The important semantic effect is simple:

- response schemas now reliably produce `Read`-side nested models
- request schemas now reliably produce `Write`-side nested models
- neutral schemas can stop inheriting an outer `Read`/`Write` state when that state should not leak inward

## Proven-correct changes

### 1. `selected-actions` split is correct

OpenAPI proof:

- `GET /orgs/{org}/actions/permissions/selected-actions` returns `#/components/schemas/selected-actions`
- `PUT /orgs/{org}/actions/permissions/selected-actions` sends `#/components/schemas/selected-actions`
- the same read/write pairing exists for the repository endpoints

Generated proof:

- generated GET methods now return `SelectedActionsRead`
- generated PUT methods now accept `SelectedActionsWrite`

Conclusion:

The new `SelectedActionsRead` / `SelectedActionsWrite` states are correct.

They exactly match response vs request usage in the source spec.

### 2. `webhook-config` split is correct

OpenAPI proof:

- `GET /app/hook/config` returns `#/components/schemas/webhook-config`
- `PATCH /app/hook/config` returns `#/components/schemas/webhook-config`
- `PATCH /app/hook/config` request body contains an inline object with the same webhook-config fields
- `GET /orgs/{org}/hooks/{hook_id}/config` returns `#/components/schemas/webhook-config`
- `PATCH /orgs/{org}/hooks/{hook_id}/config` returns `#/components/schemas/webhook-config`
- `PATCH /orgs/{org}/hooks/{hook_id}/config` request body contains the same webhook-config fields
- `PATCH /repos/{owner}/{repo}/hooks/{hook_id}` request body references `#/components/schemas/webhook-config`
- repository and organization hook responses also use the `hook` schema, whose `config` property is the webhook config in response position

Generated proof:

- generated GET methods return `WebhookConfigRead`
- generated PATCH methods accept `WebhookConfig*Write` fields and return `WebhookConfigRead`
- response models such as `Hook` now point at `WebhookConfigRead`

Conclusion:

The read/write states are correct.

This split is semantically sound.

It tracks request vs response usage correctly.

However, this one is also conservative.

The underlying leaf fields do not rely on `readOnly` / `writeOnly`.

So the split is correct, but not obviously required for correctness.

### 3. Actions permission leaf schemas are correctly split

OpenAPI proof:

- `actions-get-default-workflow-permissions` is returned by:
  - `GET /orgs/{org}/actions/permissions/workflow`
  - `GET /repos/{owner}/{repo}/actions/permissions/workflow`
- `actions-set-default-workflow-permissions` is sent by:
  - `PUT /orgs/{org}/actions/permissions/workflow`
  - `PUT /repos/{owner}/{repo}/actions/permissions/workflow`
- both wrapper schemas reference:
  - `actions-default-workflow-permissions`
  - `actions-can-approve-pull-request-reviews`

Generated proof:

- response wrapper model `ActionsGetDefaultWorkflowPermissions` now uses:
  - `ActionsDefaultWorkflowPermissionsRead`
  - `ActionsCanApprovePullRequestReviewsRead`
- request wrapper model `ActionsSetDefaultWorkflowPermissions` now uses:
  - `ActionsDefaultWorkflowPermissionsWrite`
  - `ActionsCanApprovePullRequestReviewsWrite`

Conclusion:

The new `Read` / `Write` states are correct.

The enum and boolean leaf schemas are used in both contexts.

The generated state now matches those contexts.

This is another conservative split.

The value domains are identical.

But the request/response state assignment itself is correct.

### 4. Repository ruleset nested write/read states are correct

OpenAPI proof:

- `GET /repos/{owner}/{repo}/rulesets/{ruleset_id}` returns `#/components/schemas/repository-ruleset`
- `PUT /repos/{owner}/{repo}/rulesets/{ruleset_id}` both sends and returns a ruleset shape
- the request body contains nested references to:
  - `repository-rule-enforcement`
  - `repository-ruleset-bypass-actor`
  - `repository-ruleset-conditions`
  - `repository-rule`
- the response body returns `#/components/schemas/repository-ruleset`, which contains the same logical concepts in response position

Generated proof:

- generated repo/org ruleset create and update methods accept:
  - `RepositoryRuleEnforcementWrite`
  - `RepositoryRulesetBypassActorWrite`
  - `RepositoryRulesetConditionsWrite`
  - `RepositoryRuleWrite`
- generated response models use:
  - `RepositoryRuleEnforcementRead`
  - `RepositoryRulesetBypassActorRead`
  - `RepositoryRulesetConditionsRead`
  - `RepositoryRuleRead`

Conclusion:

These new states are correct.

This is exactly the kind of nested context propagation the Phase 10 switch was supposed to fix.

### 5. Secret scanning alert update states are correct

OpenAPI proof:

- `GET /repos/{owner}/{repo}/secret-scanning/alerts/{alert_number}` returns `#/components/schemas/secret-scanning-alert`
- `PATCH /repos/{owner}/{repo}/secret-scanning/alerts/{alert_number}` returns `#/components/schemas/secret-scanning-alert`
- the PATCH request body defines request-only fields:
  - `state` using `#/components/schemas/secret-scanning-alert-state`
  - `resolution` using `#/components/schemas/secret-scanning-alert-resolution`

Generated proof:

- the PATCH API now accepts:
  - `SecretScanningAlertStateWrite`
  - `SecretScanningAlertResolutionWrite`
- response models now expose:
  - `SecretScanningAlertStateRead`
  - `SecretScanningAlertResolutionRead`

Conclusion:

The write states for request payloads are correct.

The read states for returned alert models are correct.

### 6. Code scanning alert update states are correct

OpenAPI proof:

- `GET /repos/{owner}/{repo}/code-scanning/alerts/{alert_number}` returns `#/components/schemas/code-scanning-alert`
- `PATCH /repos/{owner}/{repo}/code-scanning/alerts/{alert_number}` returns `#/components/schemas/code-scanning-alert`
- the PATCH request body defines request-side fields:
  - `dismissed_reason` using `#/components/schemas/code-scanning-alert-dismissed-reason`
  - `dismissed_comment` using `#/components/schemas/code-scanning-alert-dismissed-comment`

Generated proof:

- the PATCH API now accepts:
  - `CodeScanningAlertDismissedReasonWrite`
  - `CodeScanningAlertDismissedCommentWrite`
- response models now expose:
  - `CodeScanningAlertDismissedReasonRead`
  - `CodeScanningAlertDismissedCommentRead`

Conclusion:

These new `Read` / `Write` states are correct.

### 7. Security advisory request/response states are correct

OpenAPI proof:

- `POST /repos/{owner}/{repo}/security-advisories` sends `repository-advisory-create` and returns `repository-advisory`
- `PATCH /repos/{owner}/{repo}/security-advisories/{ghsa_id}` sends `repository-advisory-update` and returns `repository-advisory`
- request schemas contain nested values using:
  - `security-advisory-credit-types`
  - `security-advisory-ecosystems`
- response schemas also contain nested values using the same logical concepts

Generated proof:

- request-side API models use:
  - `SecurityAdvisoryCreditTypesWrite`
  - `SecurityAdvisoryEcosystemsWrite`
- response-side models use:
  - `SecurityAdvisoryCreditTypesRead`
  - `SecurityAdvisoryEcosystemsRead`

Conclusion:

These states are correct.

## Correct changes that reduced suffixing

Not all changes add `Read` / `Write`.

Some changes removed them.

Those removals also look correct.

### 8. Neutral aliases in code scanning and secret scanning responses are correct

Examples:

- `AlertCreatedAt`
- `AlertUpdatedAt`
- `AlertUrl`
- `AlertHtmlUrl`
- `AlertDismissedAt`
- `AlertFixedAt`

Generated proof:

- `CodeScanningAlert`
- `CodeScanningAlertItems`
- `CodeScanningOrganizationAlertItems`
- `DependabotAlert`
- `DependabotAlertWithRepository`
- `SecretScanningAlert`
- `OrganizationSecretScanningAlert`

now reference neutral aliases for several alert fields instead of the older `*Read` / `*Write` aliases.

OpenAPI proof:

- these aliases are scalar schemas such as `alert-created-at`, `alert-updated-at`, `alert-url`, `alert-html-url`
- they appear in response models
- the request payloads for code scanning and secret scanning updates do not directly send those alert metadata fields

Conclusion:

Using a neutral alias here is correct.

Those values are not request-payload shapes in the affected operations.

The old suffixed aliases are now dead generated files.

## Important nuance: some splits are correct but redundant

A deep review needs this distinction.

Some new `Read` / `Write` states are required.

Some are only conservative.

### Required splits

These are clearly justified by separate request and response positions in the spec:

- `SelectedActionsRead` / `SelectedActionsWrite`
- `RepositoryRuleEnforcementRead` / `RepositoryRuleEnforcementWrite`
- `RepositoryRulesetBypassActorRead` / `RepositoryRulesetBypassActorWrite`
- `RepositoryRulesetConditionsRead` / `RepositoryRulesetConditionsWrite`
- `RepositoryRuleRead` / `RepositoryRuleWrite`
- `SecretScanningAlertStateRead` / `SecretScanningAlertStateWrite`
- `SecretScanningAlertResolutionRead` / `SecretScanningAlertResolutionWrite`
- `CodeScanningAlertDismissedReasonRead` / `CodeScanningAlertDismissedReasonWrite`
- `CodeScanningAlertDismissedCommentRead` / `CodeScanningAlertDismissedCommentWrite`
- `SecurityAdvisoryCreditTypesRead` / `SecurityAdvisoryCreditTypesWrite`
- `SecurityAdvisoryEcosystemsRead` / `SecurityAdvisoryEcosystemsWrite`

### Conservative splits

These look semantically correct, but the spec does not show a value-domain difference between the read and write leaf schema:

- `ActionsDefaultWorkflowPermissionsRead` / `ActionsDefaultWorkflowPermissionsWrite`
- `ActionsCanApprovePullRequestReviewsRead` / `ActionsCanApprovePullRequestReviewsWrite`
- `ActionsEnabledRead` / `ActionsEnabledWrite`
- `AllowedActionsRead` / `AllowedActionsWrite`
- `WebhookConfigUrlRead` / `WebhookConfigUrlWrite`
- `WebhookConfigContentTypeRead` / `WebhookConfigContentTypeWrite`
- `WebhookConfigSecretRead` / `WebhookConfigSecretWrite`
- `WebhookConfigInsecureSslRead` / `WebhookConfigInsecureSslWrite`
- `DeploymentReviewerTypeRead` / `DeploymentReviewerTypeWrite`
- `WaitTimerRead` / `WaitTimerWrite`

Conclusion:

These conservative splits are still correct.

They do not contradict the spec.

But they are also a large source of churn.

## Stale generated files

This is the biggest reason the diff looks drastic.

### Orphaned tracked unsuffixed models

These files are still tracked, but the new generated code no longer references them:

- `ActionsCacheRetentionLimitForEnterprise.kt`
- `ActionsCacheRetentionLimitForOrganization.kt`
- `ActionsCacheRetentionLimitForRepository.kt`
- `ActionsCacheStorageLimitForEnterprise.kt`
- `ActionsCacheStorageLimitForOrganization.kt`
- `ActionsCacheStorageLimitForRepository.kt`
- `ActionsCanApprovePullRequestReviews.kt`
- `ActionsDefaultWorkflowPermissions.kt`
- `ActionsEnabled.kt`
- `ActionsForkPrContributorApproval.kt`
- `ActionsWorkflowAccessToRepository.kt`
- `AllowedActions.kt`
- `AppPermissions.kt`
- `CampaignState.kt`
- `CodeScanningAlertDismissedComment.kt`
- `CodeScanningAlertDismissedReason.kt`
- `CodeScanningAnalysisCommitSha.kt`
- `CodeScanningAnalysisSarifId.kt`
- `CodeScanningAnalysisToolGuid.kt`
- `CodeScanningAnalysisToolName.kt`
- `CodeScanningRef.kt`
- `CodeScanningVariantAnalysisLanguage.kt`
- `DeploymentBranchPolicySettings.kt`
- `DeploymentReviewerType.kt`
- `InteractionGroup.kt`
- `OidcCustomSub.kt`
- `RepositoryRuleEnforcement.kt`
- `RepositoryRulesetBypassActor.kt`
- `RepositoryRulesetConditions.kt`
- `SecretScanningAlertResolution.kt`
- `SecretScanningAlertState.kt`
- `SecretScanningPushProtectionBypassPlaceholderId.kt`
- `SecretScanningPushProtectionBypassReason.kt`
- `SecretScanningRowVersion.kt`
- `SecurityAdvisoryCreditTypes.kt`
- `SecurityAdvisoryEcosystems.kt`
- `ShaPinningRequired.kt`
- `WaitTimer.kt`
- `WebhookConfig.kt`

### Orphaned tracked suffixed aliases

These files are still tracked, but the new generated code now prefers a neutral unsuffixed alias instead:

- `AlertCreatedAtRead.kt`
- `AlertDismissedAtRead.kt`
- `AlertFixedAtRead.kt`
- `AlertHtmlUrlRead.kt`
- `AlertUpdatedAtRead.kt`
- `AlertUrlRead.kt`
- `AlertCreatedAtWrite.kt`
- `AlertDismissedAtWrite.kt`
- `AlertFixedAtWrite.kt`
- `AlertHtmlUrlWrite.kt`
- `AlertNumberWrite.kt`
- `AlertUpdatedAtWrite.kt`
- `AlertUrlWrite.kt`

Conclusion:

The current diff overstates the semantic change because the output folder retains dead files from older generations.

## Final verdict

### What is correct

The new generator is mostly doing the right thing.

The biggest semantic changes are correct:

- request positions now consistently use `Write`
- response positions now consistently use `Read`
- nested request/response types in rulesets, selected actions, alerts, advisory payloads, and webhook config are much more coherent than before

### What is only conservative

Some leaf aliases now split into `Read` / `Write` even when the OpenAPI schema itself does not show a different value domain.

That is still correct.

It is just noisy.

### What is definitely wrong operationally

The output directory is not cleaned.

That leaves stale generated Kotlin files behind.

This makes the change set look much larger and more chaotic than it really is.

## Recommended follow-up

1. clean `integration-tests/github/src/main/kotlin` before regeneration
2. regenerate once
3. review the remaining semantic diff only
4. optionally reduce conservative leaf splitting if the project wants smaller generated diffs
