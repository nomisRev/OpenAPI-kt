package io.github.nomisrev.render.test.client.`inline`.oneof.request.body.collection.`inline`.items

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

public interface Repos {
    public fun owner(owner: String): OwnerPath

    public interface OwnerPath {
        public fun repo(repo: String): RepoPath

        public interface RepoPath {
            public val issues: Issues

            public interface Issues {
                public fun issueNumber(issueNumber: Long): IssueNumberPath

                public interface IssueNumberPath {
                    public val labels: Labels

                    public interface Labels {
                        public val put: Put

                        public interface Put {
                            public suspend operator fun invoke(): Response

                            public suspend operator fun invoke(body: Labels): Response

                            public suspend operator fun invoke(body: List<String>): Response

                            public suspend operator fun invoke(body: LabelsNames): Response

                            public suspend operator fun invoke(body: List<Name>): Response

                            public suspend operator fun invoke(body: String): Response

                            @JvmInline
                            @Serializable
                            public value class Labels(public val labels: List<String>)

                            @JvmInline
                            @Serializable
                            public value class LabelsNames(
                                public val labels: List<Name>? = null,
                            ) {
                                @JvmInline
                                @Serializable
                                public value class Name(
                                    public val name: String,
                                )
                            }

                            @JvmInline
                            @Serializable
                            public value class Name(
                                public val name: String,
                            )

                            public data class Response(
                                public val `value`: List<Response>,
                            ) {
                                @JvmInline
                                @Serializable
                                public value class Name(
                                    public val name: String,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

internal class KtorRepos(
    private val client: HttpClient,
) : Repos {
    override fun owner(owner: String): Repos.OwnerPath = KtorReposOwnerPath(client, owner)
}

internal class KtorReposOwnerPath(
    private val client: HttpClient,
    private val owner: String,
) : Repos.OwnerPath {
    override fun repo(repo: String): Repos.OwnerPath.RepoPath = KtorReposOwnerPathRepoPath(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPath(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) : Repos.OwnerPath.RepoPath {
    override val issues: Repos.OwnerPath.RepoPath.Issues =
        KtorReposOwnerPathRepoPathIssues(client, owner, repo)
}

internal class KtorReposOwnerPathRepoPathIssues(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
) : Repos.OwnerPath.RepoPath.Issues {
    override fun issueNumber(issueNumber: Long): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath =
        KtorReposOwnerPathRepoPathIssuesIssueNumberPath(client, owner, repo, issueNumber)
}

internal class KtorReposOwnerPathRepoPathIssuesIssueNumberPath(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
    private val issueNumber: Long,
) : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath {
    override val labels: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels =
        KtorReposOwnerPathRepoPathIssuesIssueNumberPathLabels(client, owner, repo, issueNumber)
}

internal class KtorReposOwnerPathRepoPathIssuesIssueNumberPathLabels(
    private val client: HttpClient,
    private val owner: String,
    private val repo: String,
    private val issueNumber: Long,
) : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels {
    override val put: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put =
        object : Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put {
            private suspend inline fun <reified A> request(body: A): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response {
                val value: List<Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response> =
                    client.put("/repos/$owner/$repo/issues/$issueNumber/labels") {
                        body?.let {
                            contentType(ContentType.Application.Json)
                            setBody(body)
                        }
                    }.body()
                return Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response(value)
            }

            override suspend fun invoke(): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(null)

            override suspend operator fun invoke(body: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Labels): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(body)

            override suspend operator fun invoke(body: List<String>): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(body)

            override suspend operator fun invoke(body: Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.LabelsNames): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(body)

            override suspend fun invoke(body: List<Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Name>): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(body)

            override suspend operator fun invoke(body: String): Repos.OwnerPath.RepoPath.Issues.IssueNumberPath.Labels.Put.Response =
                request(body)
        }
}
