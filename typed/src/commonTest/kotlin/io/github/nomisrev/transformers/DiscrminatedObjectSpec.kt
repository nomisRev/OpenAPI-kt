package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference
import kotlin.to

val discriminatedObjectSpec by testSuite {
    val id = Schema(type = Schema.Type.Basic.String, readOnly = true)
    val query = Schema(type = Schema.Type.Basic.String, readOnly = false)
    val savedQuery = Schema(
        allOf = listOf(
            ReferenceOr.schema("WatchFolder"),
            ReferenceOr.value(
                Schema(
                    type = Schema.Type.Basic.Object,
                    properties = mapOf(
                        "id" to ReferenceOr.value(id),
                        "query" to ReferenceOr.value(query),
                        "issues" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Array,
                                items = ReferenceOr.schema("Issue"),
                                readOnly = true
                            )
                        ),
                        "visibleFor" to ReferenceOr.schema("UserGroup"),
                        "updateableBy" to ReferenceOr.schema("UserGroup"),
                        "readSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings"),
                        "updateSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings")
                    )
                )
            )
        )
    )
    val watchFolder = Schema(
        allOf = listOf(
            ReferenceOr.schema("IssueFolder"),
            ReferenceOr.value(
                Schema(
                    type = Schema.Type.Basic.Object,
                    properties = mapOf(
                        "id" to ReferenceOr.value(id),
                        "owner" to ReferenceOr.schema("User"),
                        "visibleFor" to ReferenceOr.schema("UserGroup"),
                        "updateableBy" to ReferenceOr.schema("UserGroup"),
                        "readSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings"),
                        "updateSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings")
                    )
                )
            )
        )
    )

    val name = Schema(type = Schema.Type.Basic.String, readOnly = false)
    val type = Schema(type = Schema.Type.Basic.String, readOnly = true)
    val issueFolder = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf(
            "id" to ReferenceOr.value(id),
            "name" to ReferenceOr.value(name),
            "type" to ReferenceOr.value(type)
        ),
        discriminator = Schema.Discriminator(
            $$"$type", mapOf(
                "IssueFolder" to "#/components/schemas/IssueFolder",
                "WatchFolder" to "#/components/schemas/WatchFolder",
                "IssueTag" to "#/components/schemas/IssueTag",
                "Tag" to "#/components/schemas/Tag",
                "SavedQuery" to "#/components/schemas/SavedQuery",
                "Project" to "#/components/schemas/Project"
            )
        )
    )

    val issueTag = Schema(allOf = listOf(ReferenceOr.schema("Tag")))
    val tag = Schema(
        allOf = listOf(
            ReferenceOr.schema("WatchFolder"),
            ReferenceOr.value(
                Schema(
                    type = Schema.Type.Basic.Object,
                    properties = mapOf(
                        "id" to ReferenceOr.value(id),
                        "issues" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Array,
                                items = ReferenceOr.schema("Issue"),
                                readOnly = false
                            )
                        ),
                        "color" to ReferenceOr.schema("FieldStyle"),
                        "untagOnResolve" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Boolean,
                                readOnly = false
                            )
                        ),
                        "visibleFor" to ReferenceOr.schema("UserGroup"),
                        "updateableBy" to ReferenceOr.schema("UserGroup"),
                        "readSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings"),
                        "tagSharingSettings" to ReferenceOr.schema("TagSharingSettings"),
                        "updateSharingSettings" to ReferenceOr.schema("WatchFolderSharingSettings"),
                    )
                )
            )
        )
    )
    val project = Schema(
        allOf = listOf(
            ReferenceOr.schema("IssueFolder"),
            ReferenceOr.value(
                Schema(
                    type = Schema.Type.Basic.Object,
                    properties = mapOf(
                        "id" to ReferenceOr.value(id),
                        "archived" to ReferenceOr.value(Schema(type = Schema.Type.Basic.Boolean, readOnly = false)),
                        "createdBy" to ReferenceOr.schema("User"),
                        "customFields" to ReferenceOr.value(Schema(type = Schema.Type.Basic.Object, readOnly = true)),
                        "description" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String, readOnly = false)),
                        "fromEmail" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String, readOnly = false)),
                        "iconUrl" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String, readOnly = true)),
                        "issues" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Array,
                                items = ReferenceOr.schema("Issue"),
                                readOnly = false
                            )
                        ),
                        "leader" to ReferenceOr.schema("User"),
                        "name" to ReferenceOr.value(name),
                        "replyToEmail" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String, readOnly = false)),
                        "shortName" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String, readOnly = false)),
                        "startingNumber" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Integer,
                                format = "int64",
                                readOnly = false
                            )
                        ),
                        "team" to ReferenceOr.schema("ProjectTeam"),
                        "template" to ReferenceOr.value(Schema(type = Schema.Type.Basic.Boolean, readOnly = false)),
                    )
                )
            )
        )
    )
    val api = api
        .reference("SavedQuery", savedQuery)
        .reference("WatchFolder", watchFolder)
        .reference("IssueFolder", issueFolder)
        .reference("IssueTag", issueTag)
        .reference("Tag", tag)
        .reference("Project", project)

    test("check") {

        registry(api) {
            val x = ReferenceOr.schema("SavedQuery").toModel(NamingContext.ObjectProperty("test"), SchemaContext.Write)
            val y = ReferenceOr.schema("SavedQuery").toModel(NamingContext.ObjectProperty("test"), SchemaContext.Read)
        }
    }
}