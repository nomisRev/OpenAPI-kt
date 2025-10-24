package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Enum.Closed
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Union.Case
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GitHubEdgeCaseNestedTypeNamingSpec {
  val spec = $$"""
    {
      "openapi": "3.0.3",
      "info": {
        "version": "1.1.4",
        "title": "GitHub v3 REST API",
        "description": "GitHub's v3 REST API.",
        "license": {
          "name": "MIT",
          "url": "https://spdx.org/licenses/MIT"
        },
        "termsOfService": "https://docs.github.com/articles/github-terms-of-service",
        "contact": {
          "name": "Support",
          "url": "https://support.github.com/contact?tags=dotcom-rest-api"
        },
        "x-github-plan": "api.github.com"
      },
      components: {
        schemas: {
          "repository-rule-detailed": {
            "title": "Repository Rule",
            "type": "object",
            "description": "A repository rule with ruleset details.",
            "oneOf": [
              {
                "allOf": [
                  {
                    "$ref": "#/components/schemas/repository-rule-creation"
                  },
                  {
                    "$ref": "#/components/schemas/repository-rule-ruleset-info"
                  }
                ]
              },
              {
                "allOf": [
                  {
                    "$ref": "#/components/schemas/repository-rule-update"
                  },
                  {
                    "$ref": "#/components/schemas/repository-rule-ruleset-info"
                  }
                ]
              }
            ]
          },
          "repository-rule-ruleset-info": {
            "title": "repository ruleset data for rule",
            "description": "User-defined metadata to store domain-specific information limited to 8 keys with scalar values.",
            "properties": {
              "ruleset_source_type": {
                "type": "string",
                "description": "The type of source for the ruleset that includes this rule.",
                "enum": [
                  "Repository",
                  "Organization"
                ]
              },
              "ruleset_source": {
                "type": "string",
                "description": "The name of the source of the ruleset that includes this rule."
              },
              "ruleset_id": {
                "type": "integer",
                "description": "The ID of the ruleset that includes this rule."
              }
            }
          },
          "repository-rule-creation": {
            "title": "creation",
            "description": "Only allow users with bypass permission to create matching refs.",
            "type": "object",
            "required": [
              "type"
            ],
            "properties": {
              "type": {
                "type": "string",
                "enum": [
                  "creation"
                ]
              }
            }
          },
          "repository-rule-update": {
            "title": "update",
            "description": "Only allow users with bypass permission to update matching refs.",
            "type": "object",
            "required": [
              "type"
            ],
            "properties": {
              "type": {
                "type": "string",
                "enum": [
                  "update"
                ]
              },
              "parameters": {
                "type": "object",
                "properties": {
                  "update_allows_fetch_and_merge": {
                    "type": "boolean",
                    "description": "Branch can pull changes from its upstream repository"
                  }
                },
                "required": [
                  "update_allows_fetch_and_merge"
                ]
              }
            }
          },
          "repository-rule-params-code-scanning-tool": {
            "title": "CodeScanningTool",
            "description": "A tool that must provide code scanning results for this rule to pass.",
            "type": "object",
            "properties": {
              "alerts_threshold": {
                "type": "string",
                "description": "The severity level at which code scanning results that raise alerts block a reference update. For more information on alert severity levels, see \"[About code scanning alerts](https://docs.github.com/code-security/code-scanning/managing-code-scanning-alerts/about-code-scanning-alerts#about-alert-severity-and-security-severity-levels).\"",
                "enum": [
                  "none",
                  "errors",
                  "errors_and_warnings",
                  "all"
                ]
              },
              "security_alerts_threshold": {
                "type": "string",
                "description": "The severity level at which code scanning results that raise security alerts block a reference update. For more information on security severity levels, see \"[About code scanning alerts](https://docs.github.com/code-security/code-scanning/managing-code-scanning-alerts/about-code-scanning-alerts#about-alert-severity-and-security-severity-levels).\"",
                "enum": [
                  "none",
                  "critical",
                  "high_or_higher",
                  "medium_or_higher",
                  "all"
                ]
              },
              "tool": {
                "type": "string",
                "description": "The name of a code scanning tool"
              }
            },
            "required": [
              "alerts_threshold",
              "security_alerts_threshold",
              "tool"
            ]
          }
        }
      }
    }
  """.trimIndent()

  @Test
  fun test() {
    val openAPI = OpenAPI.fromYaml(spec)
    val expected = Union(
      context = Named(name = "repository-rule-detailed"),
      cases = listOf(
        Case(
          context = Nested(
            inner = Named(name = "Update"),
            outer = Named(name = "repository-rule-detailed")
          ),
          model = Object(
            context = Nested(
              inner = Named(name = "Update"),
              outer = Named(name = "repository-rule-detailed")
            ),
            description = "Only allow users with bypass permission to update matching refs.",
            properties = listOf(
              Property(
                baseName = "type",
                model = Closed(
                  context = Nested(
                    inner = Named(name = "type"),
                    outer = Nested(
                      inner = Named(name = "Update"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                  ),
                  inner = Model.Primitive.String(default = null, description = null, constraint = null),
                  values = listOf("update"),
                  default = null,
                  description = null
                ),
                isRequired = true,
                isNullable = false,
                description = null
              ),
              Property(
                baseName = "parameters",
                model = Object(
                  context = Nested(
                    inner = Named(name = "parameters"),
                    outer = Nested(
                      inner = Named(name = "Update"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                  ),
                  description = null,
                  properties = listOf(
                    Property(
                      baseName = "update_allows_fetch_and_merge",
                      model = Model.Primitive.Boolean(
                        default = null,
                        description = "Branch can pull changes from its upstream repository"
                      ),
                      isRequired = true,
                      isNullable = false,
                      description = "Branch can pull changes from its upstream repository"
                    )
                  ),
                  inline = listOf(
                    Model.Primitive.Boolean(
                      default = null,
                      description = "Branch can pull changes from its upstream repository"
                    )
                  ),
                  additionalProperties = false
                ), isRequired = true, isNullable = false, description = null
              ),
              Property(
                baseName = "ruleset_source_type",
                model = Closed(
                  context = Nested(
                    inner = Named(name = "ruleset_source_type"),
                    outer = Nested(
                      inner = Named(name = "Update"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                  ),
                  inner = Model.Primitive.String(
                    default = null,
                    description = "The type of source for the ruleset that includes this rule.",
                    constraint = null
                  ),
                  values = listOf("Repository", "Organization"),
                  default = null,
                  description = "The type of source for the ruleset that includes this rule."
                ),
                isRequired = true,
                isNullable = false,
                description = "The type of source for the ruleset that includes this rule."
              ),
              Property(
                baseName = "ruleset_source",
                model = Model.Primitive.String(
                  default = null,
                  description = "The name of the source of the ruleset that includes this rule.",
                  constraint = null
                ),
                isRequired = true,
                isNullable = false,
                description = "The name of the source of the ruleset that includes this rule."
              ),
              Property(
                baseName = "ruleset_id",
                model = Model.Primitive.Int(
                  default = null,
                  description = "The ID of the ruleset that includes this rule.",
                  constraint = null
                ),
                isRequired = true,
                isNullable = false,
                description = "The ID of the ruleset that includes this rule."
              )
            ),
            inline = listOf(
              Closed(
                context = Nested(
                  inner = Named(name = "type"),
                  outer = Nested(
                    inner = Named(name = "Update"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(default = null, description = null, constraint = null),
                values = listOf("update"),
                default = null,
                description = null
              ), Object(
                context = Nested(
                  inner = Named(name = "parameters"),
                  outer = Nested(
                      inner = Named(name = "Update"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                ),
                description = null,
                properties = listOf(
                  Property(
                    baseName = "update_allows_fetch_and_merge",
                    model = Model.Primitive.Boolean(
                      default = null,
                      description = "Branch can pull changes from its upstream repository"
                    ),
                    isRequired = true,
                    isNullable = false,
                    description = "Branch can pull changes from its upstream repository"
                  )
                ),
                inline = listOf(
                  Model.Primitive.Boolean(
                    default = null,
                    description = "Branch can pull changes from its upstream repository"
                  )
                ),
                additionalProperties = false
              ), Closed(
                context = Nested(
                  inner = Named(name = "ruleset_source_type"),
                  outer = Nested(
                      inner = Named(name = "Update"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                ),
                inner = Model.Primitive.String(
                  default = null,
                  description = "The type of source for the ruleset that includes this rule.",
                  constraint = null
                ),
                values = listOf("Repository", "Organization"),
                default = null,
                description = "The type of source for the ruleset that includes this rule."
              ), Model.Primitive.String(
                default = null,
                description = "The name of the source of the ruleset that includes this rule.",
                constraint = null
              ), Model.Primitive.Int(
                default = null,
                description = "The ID of the ruleset that includes this rule.",
                constraint = null
              )
            ),
            additionalProperties = false
          )
        ),
        Case(
          context = Nested(
            inner = Named(name = "Creation"),
            outer = Named(name = "repository-rule-detailed")
          ),
          model = Object(
            context =Nested(
              inner = Named(name = "Creation"),
              outer = Named(name = "repository-rule-detailed")
            ),
            description = "Only allow users with bypass permission to create matching refs.",
            properties = listOf(
              Property(
                baseName = "type",
                model = Closed(
                  context = Nested(
                    inner = Named(name = "type"),
                    outer = Nested(
                      inner = Named(name = "Creation"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                  ),
                  inner = Model.Primitive.String(default = null, description = null, constraint = null),
                  values = listOf("creation"),
                  default = null,
                  description = null
                ),
                isRequired = true,
                isNullable = false,
                description = null
              ), Property(
                baseName = "ruleset_source_type",
                model = Closed(
                  context = Nested(
                    inner = Named(name = "ruleset_source_type"),
                    outer = Nested(
                      inner = Named(name = "Creation"),
                      outer = Named(name = "repository-rule-detailed")
                    )
                  ),
                  inner = Model.Primitive.String(
                    default = null,
                    description = "The type of source for the ruleset that includes this rule.",
                    constraint = null
                  ),
                  values = listOf("Repository", "Organization"),
                  default = null,
                  description = "The type of source for the ruleset that includes this rule.",
                ),
                isRequired = true,
                isNullable = false,
                description = "The type of source for the ruleset that includes this rule."
              ), Property(
                baseName = "ruleset_source",
                model = Model.Primitive.String(
                  default = null,
                  description = "The name of the source of the ruleset that includes this rule.",
                  constraint = null
                ),
                isRequired = true,
                isNullable = false,
                description = "The name of the source of the ruleset that includes this rule."
              ), Property(
                baseName = "ruleset_id",
                model = Model.Primitive.Int(
                  default = null,
                  description = "The ID of the ruleset that includes this rule.",
                  constraint = null
                ),
                isRequired = true,
                isNullable = false,
                description = "The ID of the ruleset that includes this rule."
              )
            ),
            inline = listOf(
              Closed(
                context = Nested(
                  inner = Named(name = "type"),
                  outer = Nested(
                    inner = Named(name = "Creation"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(default = null, description = null, constraint = null),
                values = listOf("creation"),
                default = null,
                description = null
              ),
              Closed(
                context = Nested(
                  inner = Named(name = "ruleset_source_type"),
                  outer = Nested(
                    inner = Named(name = "Creation"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(
                  default = null,
                  description = "The type of source for the ruleset that includes this rule.",
                  constraint = null
                ),
                values = listOf("Repository", "Organization"),
                default = null,
                description = "The type of source for the ruleset that includes this rule."
              ),
              Model.Primitive.String(
                default = null,
                description = "The name of the source of the ruleset that includes this rule.",
                constraint = null
              ),
              Model.Primitive.Int(
                default = null,
                description = "The ID of the ruleset that includes this rule.",
                constraint = null
              )
            ),
            additionalProperties = false
          )
        )
      ),
      default = null,
      description = "A repository rule with ruleset details.",
      inline = listOf(
        Object(
          context = Nested(
            inner = Named(name = "Creation"),
            outer = Named(name = "repository-rule-detailed")
          ),
          description = "Only allow users with bypass permission to create matching refs.",
          properties = listOf(
            Property(
              baseName = "type",
              model = Closed(
                context = Nested(
                  inner = Named(name = "type"),
                  outer = Nested(
                    inner = Named(name = "Creation"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(default = null, description = null, constraint = null),
                values = listOf("creation"),
                default = null,
                description = null
              ),
              isRequired = true,
              isNullable = false,
              description = null
            ), Property(
              baseName = "ruleset_source_type",
              model = Closed(
                context = Nested(
                  inner = Named(name = "ruleset_source_type"),
                  outer = Nested(
                    inner = Named(name = "Creation"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(
                  default = null,
                  description = "The type of source for the ruleset that includes this rule.",
                  constraint = null
                ),
                values = listOf("Repository", "Organization"),
                default = null,
                description = "The type of source for the ruleset that includes this rule."
              ),
              isRequired = true,
              isNullable = false,
              description = "The type of source for the ruleset that includes this rule."
            ), Property(
              baseName = "ruleset_source",
              model = Model.Primitive.String(
                default = null,
                description = "The name of the source of the ruleset that includes this rule.",
                constraint = null
              ),
              isRequired = true,
              isNullable = false,
              description = "The name of the source of the ruleset that includes this rule."
            ), Property(
              baseName = "ruleset_id",
              model = Model.Primitive.Int(
                default = null,
                description = "The ID of the ruleset that includes this rule.",
                constraint = null
              ),
              isRequired = true,
              isNullable = false,
              description = "The ID of the ruleset that includes this rule."
            )
          ),
          inline = listOf(
            Closed(
              context = Nested(
                inner = Named(name = "type"),
                outer = Nested(
                  inner = Named(name = "Creation"),
                  outer = Named(name = "repository-rule-detailed")
                )
              ),
              inner = Model.Primitive.String(default = null, description = null, constraint = null),
              values = listOf("creation"),
              default = null,
              description = null
            ),
            Closed(
              context = Nested(
                inner = Named(name = "ruleset_source_type"),
                outer = Nested(
                  inner = Named(name = "Creation"),
                  outer = Named(name = "repository-rule-detailed")
                )
              ),
              inner = Model.Primitive.String(
                default = null,
                description = "The type of source for the ruleset that includes this rule.",
                constraint = null
              ),
              values = listOf("Repository", "Organization"),
              default = null,
              description = "The type of source for the ruleset that includes this rule."
            ),
            Model.Primitive.String(
              default = null,
              description = "The name of the source of the ruleset that includes this rule.",
              constraint = null
            ),
            Model.Primitive.Int(
              default = null,
              description = "The ID of the ruleset that includes this rule.",
              constraint = null
            )
          ),
          additionalProperties = false
        ),
        Object(
          context = Nested(
            inner = Named(name = "Update"),
            outer = Named(name = "repository-rule-detailed")
          ),
          description = "Only allow users with bypass permission to update matching refs.",
          properties = listOf(
            Property(
              baseName = "type",
              model = Closed(
                context = Nested(
                  inner = Named(name = "type"),
                  outer = Nested(
                    inner = Named(name = "Update"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(default = null, description = null, constraint = null),
                values = listOf("update"),
                default = null,
                description = null
              ),
              isRequired = true,
              isNullable = false,
              description = null
            ), Property(
              baseName = "parameters",
              model = Object(
                context = Nested(
                  inner = Named(name = "parameters"),
                  outer = Nested(
                    inner = Named(name = "Update"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                description = null,
                properties = listOf(
                  Property(
                    baseName = "update_allows_fetch_and_merge",
                    model = Model.Primitive.Boolean(
                      default = null,
                      description = "Branch can pull changes from its upstream repository"
                    ),
                    isRequired = true,
                    isNullable = false,
                    description = "Branch can pull changes from its upstream repository"
                  )
                ),
                inline = listOf(
                  Model.Primitive.Boolean(
                    default = null,
                    description = "Branch can pull changes from its upstream repository"
                  )
                ),
                additionalProperties = false
              ),
              isRequired = true,
              isNullable = false,
              description = null
            ), Property(
              baseName = "ruleset_source_type",
              model = Closed(
                context = Nested(
                  inner = Named(name = "ruleset_source_type"),
                  outer = Nested(
                    inner = Named(name = "Update"),
                    outer = Named(name = "repository-rule-detailed")
                  )
                ),
                inner = Model.Primitive.String(
                  default = null,
                  description = "The type of source for the ruleset that includes this rule.",
                  constraint = null
                ),
                values = listOf("Repository", "Organization"),
                default = null,
                description = "The type of source for the ruleset that includes this rule."
              ),
              isRequired = true,
              isNullable = false,
              description = "The type of source for the ruleset that includes this rule."
            ), Property(
              baseName = "ruleset_source",
              model = Model.Primitive.String(
                default = null,
                description = "The name of the source of the ruleset that includes this rule.",
                constraint = null
              ),
              isRequired = true,
              isNullable = false,
              description = "The name of the source of the ruleset that includes this rule."
            ), Property(
              baseName = "ruleset_id",
              model = Model.Primitive.Int(
                default = null,
                description = "The ID of the ruleset that includes this rule.",
                constraint = null
              ),
              isRequired = true,
              isNullable = false,
              description = "The ID of the ruleset that includes this rule."
            )
          ),
          inline = listOf(
            Closed(
              context = Nested(
                inner = Named(name = "type"),
                outer = Nested(
                  inner = Named(name = "Update"),
                  outer = Named(name = "repository-rule-detailed")
                )
              ),
              inner = Model.Primitive.String(default = null, description = null, constraint = null),
              values = listOf("update"),
              default = null,
              description = null
            ),
            Object(
              context = Nested(
                inner = Named(name = "parameters"),
                outer = Nested(
                  inner = Named(name = "Update"),
                  outer = Named(name = "repository-rule-detailed")
                )
              ),
              description = null,
              properties = listOf(
                Property(
                  baseName = "update_allows_fetch_and_merge",
                  model = Model.Primitive.Boolean(
                    default = null,
                    description = "Branch can pull changes from its upstream repository"
                  ),
                  isRequired = true,
                  isNullable = false,
                  description = "Branch can pull changes from its upstream repository"
                )
              ),
              inline = listOf(
                Model.Primitive.Boolean(
                  default = null,
                  description = "Branch can pull changes from its upstream repository"
                )
              ),
              additionalProperties = false
            ),
            Closed(
              context = Nested(
                inner = Named(name = "ruleset_source_type"),
                outer = Nested(
                  inner = Named(name = "Update"),
                  outer = Named(name = "repository-rule-detailed")
                )
              ),
              inner = Model.Primitive.String(
                default = null,
                description = "The type of source for the ruleset that includes this rule.",
                constraint = null
              ),
              values = listOf("Repository", "Organization"),
              default = null,
              description = "The type of source for the ruleset that includes this rule."
            ),
            Model.Primitive.String(
              default = null,
              description = "The name of the source of the ruleset that includes this rule.",
              constraint = null
            ),
            Model.Primitive.Int(
              default = null,
              description = "The ID of the ruleset that includes this rule.",
              constraint = null
            )
          ),
          additionalProperties = false
        )
      ), discriminator = null
    )

    val actual = openAPI.models().find { (it as? Union)?.context == Named("repository-rule-detailed") }

    assertEquals(expected, actual)
  }
}