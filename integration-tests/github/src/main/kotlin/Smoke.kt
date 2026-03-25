package github.integration

import io.github.api.CodesOfConduct
import io.github.api.Gists
import io.github.api.GitHubV3RESTAPI
import io.github.api.Gitignore
import io.github.api.Licenses
import io.github.api.Orgs
import io.github.api.Organizations
import io.github.api.Repositories
import io.github.api.Repos
import io.github.api.Search
import io.github.api.Users
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

private const val API_BASE_URL = "https://api.github.com"
private const val REPO_OWNER = "ktorio"
private const val REPO_NAME = "ktor"
private const val ORG_LOGIN = "github"
private const val USER_LOGIN = "octocat"
private const val CONTENT_PATH = "README.md"
private const val GITIGNORE_TEMPLATE = "Kotlin"
private const val LICENSE_KEY = "mit"
private const val CODE_OF_CONDUCT_KEY = "citizen_code_of_conduct"

private data class RepoFixture(
  var repositoryId: Long? = null,
  var defaultBranch: String? = null,
  var latestCommitSha: String? = null,
  var compareBasehead: String? = null,
  var issueNumber: Long? = null,
  var pullNumber: Long? = null,
  var releaseId: Long? = null,
  var releaseTag: String? = null,
  var releaseAssetId: Long? = null,
  var rulesetId: Long? = null,
  var ruleSuiteId: Long? = null,
)

private data class OrgFixture(
  var rulesetId: Long? = null,
  var ruleSuiteId: Long? = null,
)

private data class GistFixture(
  var gistId: String? = null,
)

private enum class ProbeStatus {
  PASS,
  SKIP,
  FAIL,
}

private data class ProbeResult(
  val name: String,
  val status: ProbeStatus,
  val detail: String,
)

private suspend fun MutableList<ProbeResult>.probe(
  name: String,
  block: suspend () -> String,
) {
  try {
    add(ProbeResult(name, ProbeStatus.PASS, sanitize(block())))
  } catch (t: Throwable) {
    add(ProbeResult(name, ProbeStatus.FAIL, sanitize(describeThrowable(t))))
  }
}

private fun MutableList<ProbeResult>.skip(name: String, reason: String) {
  add(ProbeResult(name, ProbeStatus.SKIP, sanitize(reason)))
}

private fun List<ProbeResult>.printSummary() {
  forEach { result ->
    println("${result.status.name.padEnd(4)} ${result.name} :: ${result.detail}")
  }

  val grouped = groupingBy { it.status }.eachCount()
  println(
    buildString {
      append("Summary: ")
      append("pass=${grouped[ProbeStatus.PASS] ?: 0}")
      append(", skip=${grouped[ProbeStatus.SKIP] ?: 0}")
      append(", fail=${grouped[ProbeStatus.FAIL] ?: 0}")
    }
  )
}

private fun List<ProbeResult>.assertNoFailures() {
  val failures = filter { it.status == ProbeStatus.FAIL }
  check(failures.isEmpty()) {
    failures.joinToString(
      prefix = "GitHub smoke encountered ${failures.size} failing probe(s): ",
      separator = "; ",
    ) { "${it.name} -> ${it.detail}" }
  }
}

private fun Any?.responseName(): String = when (this) {
  null -> "null"
  else -> javaClass.simpleName.ifBlank { javaClass.name.substringAfterLast('.') }
}

private fun preview(value: Any?): String = sanitize(
  when (value) {
    null -> "null"
    is String -> value
    else -> value.toString()
  },
  max = 180,
)

private fun <T> describeList(
  values: List<T>,
  sample: (T) -> String = { preview(it) },
): String {
  val first = values.firstOrNull()
  return "count=${values.size}, first=${first?.let(sample) ?: "none"}"
}

private fun sanitize(text: String, max: Int = 160): String =
  text.replace(Regex("\\s+"), " ").trim().take(max)

private fun describeThrowable(t: Throwable): String =
  buildString {
    append(t.javaClass.simpleName)
    if (!t.message.isNullOrBlank()) {
      append(": ")
      append(t.message)
    }
  }

private fun githubToken(): String? =
  listOf("GITHUB_TOKEN", "GH_TOKEN")
    .firstNotNullOfOrNull { key ->
      System.getenv(key)?.takeIf { it.isNotBlank() }
    }

private fun requireValue(name: String, value: String?): String =
  requireNotNull(value) { "$name was not discovered by an earlier probe" }

suspend fun GitHubV3RESTAPI.readOnlySmoke() {
  val results = mutableListOf<ProbeResult>()
  val repoFixture = RepoFixture()
  val orgFixture = OrgFixture()
  val gistFixture = GistFixture()

  println("GitHub read-only smoke against $API_BASE_URL")
  println(
    "Fixtures: repo=$REPO_OWNER/$REPO_NAME, org=$ORG_LOGIN, user=$USER_LOGIN, token=" +
      if (githubToken() != null) "present" else "absent"
  )

  runCatalogProbes(results)
  runRepoBaselineProbes(results, repoFixture)
  runSearchProbes(results, repoFixture)
  runUserAndOrgProbes(results, orgFixture)
  runGistProbes(results, gistFixture)
  runRepoDeepProbes(results, repoFixture)

  println()
  results.printSummary()
  results.assertNoFailures()
}

private suspend fun GitHubV3RESTAPI.runCatalogProbes(results: MutableList<ProbeResult>) {
  results.probe("root.get") {
    val root = get()
    "currentUser=${root.currentUserUrl}, search=${root.repositorySearchUrl}"
  }

  results.probe("meta.get") {
    when (val response = meta.get()) {
      is io.github.api.Meta.Get.Response.Ok ->
        "apiRanges=${response.value.api?.size ?: 0}, webRanges=${response.value.web?.size ?: 0}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("rateLimit.get") {
    when (val response = rateLimit.get()) {
      is io.github.api.RateLimit.Get.Response.Ok ->
        "core=${response.value.resources.core.remaining}/${response.value.resources.core.limit}, search=${response.value.resources.search.remaining}/${response.value.resources.search.limit}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("feeds.get") {
    val response = feeds.get()
    preview(response)
  }

  results.probe("gitignore.templates.get") {
    when (val response = gitignore.templates.get()) {
      is Gitignore.Templates.Get.Response.Ok -> describeList(response.value) { it }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("gitignore.templates.name.get") {
    when (val response = gitignore.templates.name(GITIGNORE_TEMPLATE).get()) {
      is Gitignore.Templates.NamePath.Get.Response.Ok ->
        "name=${response.value.name}, source=${preview(response.value.source)}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("licenses.get") {
    when (val response = licenses.get(featured = true, perPage = 5L, page = 1L)) {
      is Licenses.Get.Response.Ok -> describeList(response.value) { it.key }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("licenses.license.get") {
    when (val response = licenses.license(LICENSE_KEY).get()) {
      is Licenses.LicensePath.Get.Response.Ok ->
        "name=${response.value.name}, featured=${response.value.featured}, permissions=${response.value.permissions.size}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("codesOfConduct.get") {
    when (val response = codesOfConduct.get()) {
      is CodesOfConduct.Get.Response.Ok -> describeList(response.value) { it.key }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("codesOfConduct.key.get") {
    when (val response = codesOfConduct.key(CODE_OF_CONDUCT_KEY).get()) {
      is CodesOfConduct.KeyPath.Get.Response.Ok ->
        "name=${response.value.name}, key=${response.value.key}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("versions.get") {
    when (val response = versions.get()) {
      is io.github.api.Versions.Get.Response.Ok -> describeList(response.value) { it.toString() }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repositories.get") {
    when (val response = repositories.get(since = null)) {
      is Repositories.Get.Response.Ok -> describeList(response.value) { it.fullName }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("organizations.get") {
    when (val response = organizations.get(since = null, perPage = 5L)) {
      is Organizations.Get.Response.Ok -> describeList(response.value) { it.login }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }
}

private suspend fun GitHubV3RESTAPI.runRepoBaselineProbes(
  results: MutableList<ProbeResult>,
  fixture: RepoFixture,
) {
  val repo = repos.owner(REPO_OWNER).repo(REPO_NAME)

  results.probe("repo.get") {
    when (val response = repo.get()) {
      is Repos.OwnerPath.RepoPath.Get.Response.Ok -> {
        fixture.repositoryId = response.value.id
        fixture.defaultBranch = response.value.defaultBranch
        "repo=${response.value.fullName}, id=${response.value.id}, defaultBranch=${response.value.defaultBranch}"
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  val defaultBranch = fixture.defaultBranch ?: "main"

  results.probe("repo.branches.get") {
    when (
      val response = repo.branches.get(
        `protected` = true,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Repos.OwnerPath.RepoPath.Branches.Get.Response.Ok -> describeList(response.value) { it.name }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.branches.branch.get") {
    when (val response = repo.branches.branch(defaultBranch).get()) {
      is Repos.OwnerPath.RepoPath.Branches.BranchPath.Get.Response.Ok ->
        "branch=${response.value.name}, protected=${response.value.`protected`}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.branches.branch.protection.get") {
    val response = repo.branches.branch(defaultBranch).protection.get()
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.languages.get") {
    val response = repo.languages.get()
    "languageBuckets=${response.values?.size ?: 0}, sample=${preview(response.values?.take(5))}"
  }

  results.probe("repo.license.get") {
    when (val response = repo.license.get(ref = defaultBranch)) {
      is Repos.OwnerPath.RepoPath.License.Get.Response.Ok ->
        "name=${response.value.name}, path=${response.value.path}, size=${response.value.size}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.topics.get") {
    when (val response = repo.topics.get(page = 1L, perPage = 30L)) {
      is Repos.OwnerPath.RepoPath.Topics.Get.Response.Ok ->
        "topics=${response.value.names.joinToString(limit = 10)}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.contributors.get") {
    when (val response = repo.contributors.get(anon = "1", perPage = 5L, page = 1L)) {
      is Repos.OwnerPath.RepoPath.Contributors.Get.Response.Ok ->
        describeList(response.value) { "${it.login ?: "anon"}:${it.contributions}" }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.community.profile.get") {
    val response = repo.community.profile.get()
    "health=${response.healthPercentage}, hasReadme=${response.files.readme != null}, hasLicense=${response.files.license != null}"
  }

  results.probe("repo.commits.get") {
    when (
      val response = repo.commits.get(
        sha = defaultBranch,
        path = null,
        author = null,
        committer = null,
        since = null,
        until = null,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Repos.OwnerPath.RepoPath.Commits.Get.Response.Ok -> {
        val latest = response.value.firstOrNull()
        fixture.latestCommitSha = latest?.sha
        fixture.compareBasehead = latest
          ?.parents
          ?.firstOrNull()
          ?.sha
          ?.let { "$it...${latest.sha}" }
        describeList(response.value) { "${it.sha.take(12)} ${it.commit.message.lineSequence().firstOrNull().orEmpty()}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.issues.get") {
    when (
      val response = repo.issues.get(
        milestone = null,
        state = Repos.OwnerPath.RepoPath.Issues.Get.State.All,
        assignee = null,
        type = null,
        creator = null,
        mentioned = null,
        labels = null,
        sort = Repos.OwnerPath.RepoPath.Issues.Get.Sort.Updated,
        direction = Repos.OwnerPath.RepoPath.Issues.Get.Direction.Desc,
        since = null,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Repos.OwnerPath.RepoPath.Issues.Get.Response.Ok -> {
        fixture.issueNumber = response.value.firstOrNull()?.number
        describeList(response.value) { "#${it.number} ${it.title}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.pulls.get") {
    when (
      val response = repo.pulls.get(
        state = Repos.OwnerPath.RepoPath.Pulls.Get.State.All,
        head = null,
        base = null,
        sort = Repos.OwnerPath.RepoPath.Pulls.Get.Sort.Updated,
        direction = Repos.OwnerPath.RepoPath.Pulls.Get.Direction.Desc,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Repos.OwnerPath.RepoPath.Pulls.Get.Response.Ok -> {
        fixture.pullNumber = response.value.firstOrNull()?.number
        describeList(response.value) { "#${it.number} ${it.title}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.releases.get") {
    when (val response = repo.releases.get(perPage = 5L, page = 1L)) {
      is Repos.OwnerPath.RepoPath.Releases.Get.Response.Ok -> {
        val first = response.value.firstOrNull()
        fixture.releaseId = first?.id
        fixture.releaseTag = first?.tagName
        fixture.releaseAssetId = first?.assets?.firstOrNull()?.id
        describeList(response.value) { "${it.tagName} assets=${it.assets.size}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.releases.latest.get") {
    val response = repo.releases.latest.get()
    fixture.releaseId = response.id
    fixture.releaseTag = response.tagName
    fixture.releaseAssetId = response.assets.firstOrNull()?.id
    "tag=${response.tagName}, assets=${response.assets.size}, draft=${response.draft}"
  }

  results.probe("repo.tags.get") {
    val response = repo.tags.get(perPage = 5L, page = 1L)
    describeList(response) { it.name }
  }

  results.probe("repo.rulesets.get") {
    when (
      val response = repo.rulesets.get(
        perPage = 5L,
        page = 1L,
        includesParents = true,
        targets = "branch",
      )
    ) {
      is Repos.OwnerPath.RepoPath.Rulesets.Get.Response.Ok -> {
        fixture.rulesetId = response.value.firstOrNull()?.id
        describeList(response.value) { "${it.id}:${it.name}:${it.target?.value}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.rulesets.ruleSuites.get") {
    when (
      val response = repo.rulesets.ruleSuites.get(
        ref = defaultBranch,
        timePeriod = Repos.OwnerPath.RepoPath.Rulesets.RuleSuites.Get.TimePeriod.Day,
        actorName = null,
        ruleSuiteResult = Repos.OwnerPath.RepoPath.Rulesets.RuleSuites.Get.RuleSuiteResult.All,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Repos.OwnerPath.RepoPath.Rulesets.RuleSuites.Get.Response.Ok -> {
        fixture.ruleSuiteId = response.value.items.firstOrNull()?.id
        describeList(response.value.items) { "${it.id}:${it.result?.value}:${it.ref}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }
}

private suspend fun GitHubV3RESTAPI.runSearchProbes(
  results: MutableList<ProbeResult>,
  fixture: RepoFixture,
) {
  results.probe("search.repositories.get") {
    when (
      val response = search.repositories.get(
        q = "repo:$REPO_OWNER/$REPO_NAME",
        sort = Search.Repositories.Get.Sort.Stars,
        order = Search.Repositories.Get.Order.Desc,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Search.Repositories.Get.Response.Ok ->
        "total=${response.totalCount}, ${describeList(response.items) { it.fullName }}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("search.code.get") {
    when (
      val response = search.code.get(
        q = "repo:$REPO_OWNER/$REPO_NAME path:$CONTENT_PATH ktor",
        sort = Search.Code.Get.Sort.Indexed,
        order = Search.Code.Get.Order.Desc,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Search.Code.Get.Response.Ok ->
        "total=${response.totalCount}, ${describeList(response.items) { "${it.path}@${it.repository.fullName}" }}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("search.issues.get") {
    when (
      val response = search.issues.get(
        q = "repo:$REPO_OWNER/$REPO_NAME is:issue",
        sort = Search.Issues.Get.Sort.Updated,
        order = Search.Issues.Get.Order.Desc,
        perPage = 5L,
        page = 1L,
        advancedSearch = "true",
      )
    ) {
      is Search.Issues.Get.Response.Ok ->
        "total=${response.totalCount}, ${describeList(response.items) { "#${it.number} ${it.title}" }}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("search.users.get") {
    when (
      val response = search.users.get(
        q = "language:kotlin type:user",
        sort = Search.Users.Get.Sort.Followers,
        order = Search.Users.Get.Order.Desc,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Search.Users.Get.Response.Ok ->
        "total=${response.totalCount}, ${describeList(response.items) { it.login }}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("search.topics.get") {
    when (val response = search.topics.get(q = "kotlin", perPage = 5L, page = 1L)) {
      is Search.Topics.Get.Response.Ok ->
        "total=${response.totalCount}, ${describeList(response.items) { it.name }}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  val repositoryId = fixture.repositoryId
  if (repositoryId == null) {
    results.skip("search.labels.get", "repositoryId unavailable from repo.get")
  } else {
    results.probe("search.labels.get") {
      when (
        val response = search.labels.get(
          repositoryId = repositoryId,
          q = "bug",
          sort = Search.Labels.Get.Sort.Updated,
          order = Search.Labels.Get.Order.Desc,
          perPage = 5L,
          page = 1L,
        )
      ) {
        is Search.Labels.Get.Response.Ok ->
          "total=${response.totalCount}, ${describeList(response.items) { it.name }}"

        else -> "${response.responseName()} ${preview(response)}"
      }
    }
  }
}

private suspend fun GitHubV3RESTAPI.runUserAndOrgProbes(
  results: MutableList<ProbeResult>,
  fixture: OrgFixture,
) {
  val user = users.username(USER_LOGIN)
  val org = orgs.org(ORG_LOGIN)

  results.probe("users.get") {
    when (val response = users.get(since = null, perPage = 5L)) {
      is Users.Get.Response.Ok -> describeList(response.value) { it.login }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("users.username.get") {
    when (val response = user.get()) {
      is Users.UsernamePath.Get.Response.Ok.Public ->
        "login=${response.value.login}, followers=${response.value.followers}, repos=${response.value.publicRepos}"

      is Users.UsernamePath.Get.Response.Ok.Private ->
        "login=${response.value.login}, followers=${response.value.followers}, repos=${response.value.publicRepos}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("users.username.events.public.get") {
    val response = user.events.public.get(perPage = 5L, page = 1L)
    describeList(response) { preview(it) }
  }

  results.probe("users.username.events.org.get") {
    val response = user.events.orgs.org(ORG_LOGIN).get(perPage = 5L, page = 1L)
    describeList(response) { preview(it) }
  }

  results.probe("users.username.followers.get") {
    val response = user.followers.get(perPage = 5L, page = 1L)
    describeList(response) { it.login }
  }

  results.probe("users.username.following.get") {
    val response = user.following.get(perPage = 5L, page = 1L)
    describeList(response) { it.login }
  }

  results.probe("users.username.gists.get") {
    when (val response = user.gists.get(since = null, perPage = 5L, page = 1L)) {
      is Users.UsernamePath.Gists.Get.Response.Ok -> describeList(response.value) { it.id }
      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("users.username.gpgKeys.get") {
    val response = user.gpgKeys.get(perPage = 5L, page = 1L)
    describeList(response) { it.keyId.take(16) }
  }

  results.probe("users.username.orgs.get") {
    val response = user.orgs.get(perPage = 5L, page = 1L)
    describeList(response) { it.login }
  }

  results.probe("users.username.repos.get") {
    val response = user.repos.get(
      type = Users.UsernamePath.Repos.Get.Type.Owner,
      sort = Users.UsernamePath.Repos.Get.Sort.Pushed,
      direction = Users.UsernamePath.Repos.Get.Direction.Desc,
      perPage = 5L,
      page = 1L,
    )
    describeList(response) { it.fullName }
  }

  results.probe("orgs.org.get") {
    when (val response = org.get()) {
      is Orgs.OrgPath.Get.Response.Ok ->
        "login=${response.value.login}, publicRepos=${response.value.publicRepos}, verified=${response.value.isVerified}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("orgs.org.events.get") {
    val response = org.events.get(perPage = 5L, page = 1L)
    describeList(response) { preview(it) }
  }

  results.probe("orgs.org.publicMembers.get") {
    val response = org.publicMembers.get(perPage = 5L, page = 1L)
    describeList(response) { it.login }
  }

  results.probe("orgs.org.repos.get") {
    val response = org.repos.get(
      type = Orgs.OrgPath.Repos.Get.Type.Public,
      sort = Orgs.OrgPath.Repos.Get.Sort.Pushed,
      direction = Orgs.OrgPath.Repos.Get.Direction.Desc,
      perPage = 5L,
      page = 1L,
    )
    describeList(response) { it.fullName }
  }

  results.probe("orgs.org.rulesets.get") {
    when (val response = org.rulesets.get(perPage = 5L, page = 1L, targets = "repository")) {
      is Orgs.OrgPath.Rulesets.Get.Response.Ok -> {
        fixture.rulesetId = response.value.firstOrNull()?.id
        describeList(response.value) { "${it.id}:${it.name}:${it.target?.value}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("orgs.org.rulesets.ruleSuites.get") {
    when (
      val response = org.rulesets.ruleSuites.get(
        ref = null,
        repositoryName = null,
        timePeriod = Orgs.OrgPath.Rulesets.RuleSuites.Get.TimePeriod.Day,
        actorName = null,
        ruleSuiteResult = Orgs.OrgPath.Rulesets.RuleSuites.Get.RuleSuiteResult.All,
        perPage = 5L,
        page = 1L,
      )
    ) {
      is Orgs.OrgPath.Rulesets.RuleSuites.Get.Response.Ok -> {
        fixture.ruleSuiteId = response.value.items.firstOrNull()?.id
        describeList(response.value.items) { "${it.id}:${it.repositoryName}:${it.result?.value}" }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  val orgRulesetId = fixture.rulesetId
  if (orgRulesetId == null) {
    results.skip("orgs.org.rulesets.rulesetId.get", "org ruleset id unavailable")
  } else {
    results.probe("orgs.org.rulesets.rulesetId.get") {
      val response = org.rulesets.rulesetId(orgRulesetId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val orgRuleSuiteId = fixture.ruleSuiteId
  if (orgRuleSuiteId == null) {
    results.skip("orgs.org.rulesets.ruleSuites.ruleSuiteId.get", "org rule suite id unavailable")
  } else {
    results.probe("orgs.org.rulesets.ruleSuites.ruleSuiteId.get") {
      val response = org.rulesets.ruleSuites.ruleSuiteId(orgRuleSuiteId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }
}

private suspend fun GitHubV3RESTAPI.runGistProbes(
  results: MutableList<ProbeResult>,
  fixture: GistFixture,
) {
  results.probe("gists.public.get") {
    when (val response = gists.public.get(since = null, perPage = 5L, page = 1L)) {
      is Gists.Public.Get.Response.Ok -> {
        fixture.gistId = response.value.firstOrNull()?.id
        describeList(response.value) { it.id }
      }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  val gistId = fixture.gistId
  if (gistId == null) {
    results.skip("gists.gistId.*", "no public gist id discovered")
    return
  }

  val gist = gists.gistId(gistId)

  results.probe("gists.gistId.get") {
    when (val response = gist.get()) {
      is Gists.GistIdPath.Get.Response.Ok ->
        "id=${response.value.id}, files=${response.value.files?.size ?: 0}, comments=${response.value.comments ?: 0}"

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("gists.gistId.comments.get") {
    when (val response = gist.comments.get(perPage = 5L, page = 1L)) {
      is Gists.GistIdPath.Comments.Get.Response.Ok ->
        describeList(response.value) { "#${it.id} ${it.user?.login ?: "unknown"}" }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("gists.gistId.commits.get") {
    when (val response = gist.commits.get(perPage = 5L, page = 1L)) {
      is Gists.GistIdPath.Commits.Get.Response.Ok ->
        describeList(response.value) { it.version.take(12) }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("gists.gistId.forks.get") {
    when (val response = gist.forks.get(perPage = 5L, page = 1L)) {
      is Gists.GistIdPath.Forks.Get.Response.Ok ->
        describeList(response.value) { it.id ?: "unknown" }

      else -> "${response.responseName()} ${preview(response)}"
    }
  }
}

private suspend fun GitHubV3RESTAPI.runRepoDeepProbes(
  results: MutableList<ProbeResult>,
  fixture: RepoFixture,
) {
  val repo = repos.owner(REPO_OWNER).repo(REPO_NAME)
  val defaultBranch = fixture.defaultBranch ?: "main"

  val compareBasehead = fixture.compareBasehead
  if (compareBasehead == null) {
    results.skip("repo.compare.get", "compare basehead unavailable from commits list")
  } else {
    results.probe("repo.compare.get") {
      when (val response = repo.compare.basehead(compareBasehead).get(page = 1L, perPage = 30L)) {
        is Repos.OwnerPath.RepoPath.Compare.BaseheadPath.Get.Response.Ok ->
          "status=${response.value.status.value}, commits=${response.value.commits.size}, files=${response.value.files?.size ?: 0}"

        else -> "${response.responseName()} ${preview(response)}"
      }
    }
  }

  results.probe("repo.contents.get.json") {
    val response = repo.contents.path(CONTENT_PATH).get.json(ref = defaultBranch)
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.contents.get.vndGithubObject") {
    val response = repo.contents.path(CONTENT_PATH).get.vndGithubObject(ref = defaultBranch)
    "${response.responseName()} ${preview(response)}"
  }

  val latestCommitSha = fixture.latestCommitSha
  if (latestCommitSha == null) {
    results.skip("repo.ref.*", "commit sha unavailable from commits list")
  } else {
    val commitRef = repo.commits.ref(latestCommitSha)
    val commitPath = repo.commits.commitSha(latestCommitSha)

    results.probe("repo.ref.get") {
      val response = commitRef.get(page = 1L, perPage = 30L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.ref.checkRuns.get") {
      val response = commitRef.checkRuns.get()
      "total=${response.totalCount}, ${describeList(response.checkRuns) { it.name }}"
    }

    results.probe("repo.ref.checkSuites.get") {
      val response = commitRef.checkSuites.get(perPage = 5L, page = 1L)
      "total=${response.totalCount}, ${describeList(response.checkSuites) { "${it.id}:${it.status?.value}" }}"
    }

    results.probe("repo.ref.status.get") {
      val response = commitRef.status.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.ref.statuses.get") {
      val response = commitRef.statuses.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.commits.commitSha.branchesWhereHead.get") {
      val response = commitPath.branchesWhereHead.get()
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.commits.commitSha.comments.get") {
      val response = commitPath.comments.get(perPage = 5L, page = 1L)
      describeList(response) { "#${it.id} ${it.path ?: "no-path"}" }
    }

    results.probe("repo.commits.commitSha.pulls.get") {
      val response = commitPath.pulls.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }
  }

  val issueNumber = fixture.issueNumber
  if (issueNumber == null) {
    results.skip("repo.issues.issueNumber.*", "issue number unavailable from issues list")
  } else {
    val issue = repo.issues.issueNumber(issueNumber)

    results.probe("repo.issues.issueNumber.get") {
      val response = issue.get()
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.issues.issueNumber.comments.get") {
      val response = issue.comments.get(since = null, perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.issues.issueNumber.events.get") {
      val response = issue.events.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.issues.issueNumber.timeline.get") {
      val response = issue.timeline.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.pulls.comments.get") {
    val response = repo.pulls.comments.get(
      sort = Repos.OwnerPath.RepoPath.Pulls.Comments.Get.Sort.Updated,
      direction = Repos.OwnerPath.RepoPath.Pulls.Comments.Get.Direction.Desc,
      since = null,
      perPage = 5L,
      page = 1L,
    )
    describeList(response) { "#${it.id} ${it.path}" }
  }

  val pullNumber = fixture.pullNumber
  if (pullNumber == null) {
    results.skip("repo.pulls.pullNumber.*", "pull number unavailable from pulls list")
  } else {
    val pull = repo.pulls.pullNumber(pullNumber)

    results.probe("repo.pulls.pullNumber.get") {
      val response = pull.get()
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.pulls.pullNumber.comments.get") {
      val response = pull.comments.get(
        sort = Repos.OwnerPath.RepoPath.Pulls.PullNumberPath.Comments.Get.Sort.Updated,
        direction = Repos.OwnerPath.RepoPath.Pulls.PullNumberPath.Comments.Get.Direction.Desc,
        since = null,
        perPage = 5L,
        page = 1L,
      )
      describeList(response) { "#${it.id} ${it.path}" }
    }

    results.probe("repo.pulls.pullNumber.commits.get") {
      val response = pull.commits.get(perPage = 5L, page = 1L)
      describeList(response) { it.sha.take(12) }
    }

    results.probe("repo.pulls.pullNumber.files.get") {
      val response = pull.files.get(perPage = 5L, page = 1L)
      "${response.responseName()} ${preview(response)}"
    }

    results.probe("repo.pulls.pullNumber.reviews.get") {
      val response = pull.reviews.get(perPage = 5L, page = 1L)
      describeList(response) { "${it.id}:${it.state}" }
    }

    results.probe("repo.pulls.pullNumber.merge.get") {
      val response = pull.merge.get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val releaseTag = fixture.releaseTag
  if (releaseTag == null) {
    results.skip("repo.releases.tags.tag.get", "release tag unavailable from releases list")
  } else {
    results.probe("repo.releases.tags.tag.get") {
      val response = repo.releases.tags.tag(releaseTag).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val releaseId = fixture.releaseId
  if (releaseId == null) {
    results.skip("repo.releases.releaseId.get", "release id unavailable from releases list")
  } else {
    results.probe("repo.releases.releaseId.get") {
      val response = repo.releases.releaseId(releaseId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val releaseAssetId = fixture.releaseAssetId
  if (releaseAssetId == null) {
    results.skip("repo.releases.assets.assetId.get", "release asset id unavailable from releases list")
  } else {
    results.probe("repo.releases.assets.assetId.get") {
      val response = repo.releases.assets.assetId(releaseAssetId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val rulesetId = fixture.rulesetId
  if (rulesetId == null) {
    results.skip("repo.rulesets.rulesetId.get", "repo ruleset id unavailable")
  } else {
    results.probe("repo.rulesets.rulesetId.get") {
      val response = repo.rulesets.rulesetId(rulesetId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  val ruleSuiteId = fixture.ruleSuiteId
  if (ruleSuiteId == null) {
    results.skip("repo.rulesets.ruleSuites.ruleSuiteId.get", "repo rule suite id unavailable")
  } else {
    results.probe("repo.rulesets.ruleSuites.ruleSuiteId.get") {
      val response = repo.rulesets.ruleSuites.ruleSuiteId(ruleSuiteId).get()
      "${response.responseName()} ${preview(response)}"
    }
  }

  results.probe("repo.stats.codeFrequency.get") {
    val response = repo.stats.codeFrequency.get()
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.stats.commitActivity.get") {
    val response = repo.stats.commitActivity.get()
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.stats.contributors.get") {
    val response = repo.stats.contributors.get()
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.stats.participation.get") {
    val response = repo.stats.participation.get()
    "${response.responseName()} ${preview(response)}"
  }

  results.probe("repo.stats.punchCard.get") {
    val response = repo.stats.punchCard.get()
    "${response.responseName()} ${preview(response)}"
  }
}

fun main(): Unit = runBlocking {
  val token = githubToken()
  val api = GitHubV3RESTAPI(API_BASE_URL) {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
          explicitNulls = false
        }
      )
    }

    defaultRequest {
      header(HttpHeaders.UserAgent, "openapi-kt-github-smoke")
      header(HttpHeaders.Accept, "application/vnd.github+json")
      header("X-GitHub-Api-Version", "2022-11-28")
      if (token != null) {
        header(HttpHeaders.Authorization, "Bearer $token")
      }
    }
  }

  try {
    api.readOnlySmoke()
  } finally {
    api.close()
  }
}
