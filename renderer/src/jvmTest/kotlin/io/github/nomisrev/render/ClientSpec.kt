package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

val clientSpec by testSuite {
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/widgets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": { "schema": { "type": "string" } }
                    }
                  },
                  "2XX": {
                    "description": "Successful response range",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "version": { "type": "integer", "format": "int32" }
                          },
                          "required": ["id"]
                        }
                      },
                      "application/xml": { "schema": { "type": "string" } }
                    }
                  },
                  "4XX": { "description": "Client error range" },
                  "5XX": {
                    "description": "Server error range",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": { "message": { "type": "string" } }
                        }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected response",
                    "content": {
                      "text/plain": { "schema": { "type": "string" } }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-ranges"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/simple-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/nested-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/collaborators": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/mixed-segments"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/health": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/root-operations"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/a/b/{c}/d/{e}/f": {
              "get": {
                "parameters": [
                  { "name": "c", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "e", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/deep-nesting"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/workflows/{workflow_id}/runs": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "type": "string",
                      "enum": ["queued", "in-progress"]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-param-enum-inline"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/workflows/{workflow_id}/runs": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "${'$'}ref": "#/components/schemas/WorkflowId"
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "WorkflowId": {
                "type": "string",
                "enum": ["queued", "in-progress"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-param-enum-ref"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/workflows/{workflow_id}/runs": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "oneOf": [
                        { "type": "integer", "format": "int32" },
                        { "type": "string", "enum": ["queued", "in-progress"] }
                      ]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-param-oneof-flat"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/workflows/{workflow_id}/runs": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "oneOf": [
                        { "type": "integer", "format": "int32" },
                        { "type": "string", "enum": ["queued", "in-progress"] }
                      ]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            },
            "/workflows/{workflow_id}/history": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "oneOf": [
                        { "type": "integer", "format": "int32" },
                        { "type": "string", "enum": ["queued", "in-progress"] }
                      ]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-param-oneof-flat-shared"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/workflows/{workflow_id}/runs": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "oneOf": [
                        { "type": "string", "enum": ["queued", "dlq"] },
                        { "type": "string", "enum": ["in-progress"] }
                      ]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            },
            "/workflows/{workflow_id}/history": {
              "get": {
                "parameters": [
                  {
                    "name": "workflow_id",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "oneOf": [
                        { "type": "string", "enum": ["queued", "dlq"] },
                        { "type": "string", "enum": ["in-progress"] }
                      ]
                    }
                  }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-param-oneof-multi-enum-shared"
    )

    // Phase 9 tests

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-basic"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "get": {
                "parameters": [
                  { "name": "query", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "X-Api-Key", "in": "header", "required": true, "schema": { "type": "string" } },
                  { "name": "session", "in": "cookie", "required": true, "schema": { "type": "string" } },
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-params"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "query", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "X-Request-Id", "in": "header", "required": true, "schema": { "type": "string" } },
                  { "name": "limit", "in": "query", "required": false, "schema": { "type": "integer", "format": "int32" } },
                  { "name": "X-Trace-Id", "in": "header", "required": false, "schema": { "type": "string" } },
                  { "name": "preference", "in": "cookie", "required": false, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-optional"
    )

    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "limit", "in": "query", "required": true, "schema": { "$ref": "#/components/schemas/Limit" } },
                  { "name": "offset", "in": "query", "required": false, "schema": { "$ref": "#/components/schemas/Offset" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Limit": {
                "type": "integer",
                "format": "int32",
                "default": 20
              },
              "Offset": {
                "type": "integer",
                "format": "int32",
                "default": 0
              }
            }
          }
        }
        """.trimIndent(),
        "client/primitive-input-flatten"
    )

    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{alert_number}": {
              "get": {
                "parameters": [
                  { "name": "alert_number", "in": "path", "required": true, "schema": { "$ref": "#/components/schemas/AlertNumber" } },
                  { "name": "tool_name", "in": "query", "required": false, "schema": { "$ref": "#/components/schemas/ToolName" } },
                  { "name": "X-Request-Id", "in": "header", "required": true, "schema": { "$ref": "#/components/schemas/RequestId" } },
                  { "name": "session", "in": "cookie", "required": false, "schema": { "$ref": "#/components/schemas/SessionAt" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "schemas": {
              "AlertNumber": {
                "type": "integer",
                "format": "int64",
                "description": "The security alert number."
              },
              "ToolName": {
                "type": "string"
              },
              "RequestId": {
                "type": "string",
                "format": "uuid"
              },
              "SessionAt": {
                "type": "string",
                "format": "date-time"
              }
            }
          }
        }
        """.trimIndent(),
        "client/primitive-input-flattened"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/tags": {
              "get": {
                "parameters": [
                  {
                    "name": "filter",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "object",
                      "properties": {
                        "value": { "type": "string" }
                      },
                      "required": ["value"]
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/primitive-input-object-guard"
    )

    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/filters": {
              "get": {
                "parameters": [
                  { "name": "state", "in": "query", "required": false, "schema": { "$ref": "#/components/schemas/State" } },
                  { "name": "selector", "in": "query", "required": false, "schema": { "$ref": "#/components/schemas/Selector" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "schemas": {
              "State": {
                "type": "string",
                "enum": ["open", "closed"]
              },
              "Selector": {
                "oneOf": [
                  { "type": "string" },
                  { "type": "integer", "format": "int32" }
                ]
              }
            }
          }
        }
        """.trimIndent(),
        "client/primitive-input-wrapped"
    )

    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "$ref": "#/components/schemas/CreatePetRequest" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/settings": {
              "patch": {
                "requestBody": {
                  "required": false,
                  "content": {
                    "application/json": {
                      "schema": { "$ref": "#/components/schemas/UpdateSettingsRequest" }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "schemas": {
              "CreatePetRequest": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" }
                },
                "required": ["name"]
              },
              "UpdateSettingsRequest": {
                "type": "object",
                "properties": {
                  "theme": { "type": "string" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-json"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/markdown": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "properties": {
                          "text": {
                            "description": "The Markdown text to render in HTML.",
                            "type": "string"
                          },
                          "mode": {
                            "description": "The rendering mode.",
                            "enum": [
                              "markdown",
                              "gfm"
                            ],
                            "default": "markdown",
                            "example": "markdown",
                            "type": "string"
                          },
                          "context": {
                            "description": "The repository context to use when creating references in `gfm` mode. For example, setting `context` to `octo-org/octo-repo` will change the text `#42` into an HTML link to issue 42 in the `octo-org/octo-repo` repository.",
                            "type": "string"
                          }
                        },
                        "required": [
                          "text"
                        ],
                        "type": "object"
                      }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "text/html": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-markdown-body"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/markdown": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "text/plain": {
                      "schema": { "type": "string" }
                    },
                    "text/x-markdown": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      },
                      "application/xml": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-request-type"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/issues": {
              "post": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "title": {
                            "oneOf": [
                              { "type": "string" },
                              { "type": "integer", "format": "int64" }
                            ]
                          },
                          "body": { "type": "string" },
                          "milestone": {
                            "oneOf": [
                              { "type": "string" },
                              { "type": "integer", "format": "int64" }
                            ]
                          }
                        },
                        "required": ["title"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-body-unions"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/widgets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "version": { "type": "integer", "format": "int32" }
                          },
                          "required": ["id"]
                        }
                      }
                    }
                  }
                }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "name": { "type": "string" }
                        },
                        "required": ["name"]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "name": { "type": "string" }
                          },
                          "required": ["id", "name"]
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-method-uniqueness"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/uploads/{uploadId}": {
              "post": {
                "parameters": [
                  { "name": "uploadId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "file": { "type": "string", "format": "binary" },
                          "checksum": { "type": "string" },
                          "retries": { "type": "integer", "format": "int32" },
                          "tags": {
                            "type": "array",
                            "items": { "type": "string" }
                          }
                        },
                        "required": ["file", "checksum", "tags"]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "207": {
                    "description": "Multi-Status",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  },
                  "422": {
                    "description": "Unprocessable Entity",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "boolean" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-complex-body-response"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/files": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "file": { "type": "string", "format": "binary" },
                          "purpose": { "type": "string" }
                        },
                        "required": ["file", "purpose"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-multipart"
    )

    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/realtime/calls": {
              "post": {
                "summary": "Create a new Realtime API call",
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "${'$'}ref": "#/components/schemas/RealtimeCallCreateRequest"
                      },
                      "encoding": {
                        "sdp": {
                          "contentType": "application/sdp"
                        },
                        "session": {
                          "contentType": "application/json"
                        }
                      }
                    },
                    "application/sdp": {
                      "schema": {
                        "type": "string"
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Realtime call created successfully.",
                    "headers": {
                      "Location": {
                        "description": "Relative URL containing the call ID.",
                        "schema": { "type": "string" }
                      }
                    },
                    "content": {
                      "application/sdp": {
                        "schema": {
                          "type": "string"
                        }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "RealtimeCallCreateRequest": {
                "type": "object",
                "required": ["sdp"],
                "properties": {
                  "sdp": { "type": "string" },
                  "session": {
                    "type": "object",
                    "required": ["type", "model"],
                    "properties": {
                      "type": { "type": "string" },
                      "model": { "type": "string" }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-realtime"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/oauth/token": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "grant_type": { "type": "string" },
                          "code": { "type": "string" },
                          "redirect_uri": { "type": "string" }
                        },
                        "required": ["grant_type", "code", "redirect_uri"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-form"
    )

    // A form body is sent as `application/x-www-form-urlencoded`, with the parameters the spec declares on it, not as
    // the `text/plain` Ktor labels a bare `String` body with: for a single form body, the form overload next to a
    // JSON body, and a form declared with `charset=utf-8`.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/tokens": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "grant_type": { "type": "string", "enum": ["authorization_code", "refresh_token"] },
                          "code": { "type": "string" },
                          "redirect_uri": { "type": "string" }
                        },
                        "required": ["grant_type", "code", "redirect_uri"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded; charset=utf-8": {
                      "schema": {
                        "type": "object",
                        "properties": { "text": { "type": "string" } },
                        "required": ["text"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/search": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "$ref": "#/components/schemas/SearchQuery" } },
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": { "query": { "type": "string" } },
                        "required": ["query"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "SearchQuery": {
                "type": "object",
                "properties": { "query": { "type": "string" }, "limit": { "type": "integer" } },
                "required": ["query", "limit"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/form-urlencoded-body"
    )

    // Form fields are serialized like query parameters (Encoding Object, default `style: form`, `explode: true`):
    // an optional field is left out when `null`, a scalar is its string form, an enum its wire value, an array is
    // repeated keys, or one value joined by `,` (`explode: false`), a space (`spaceDelimited`) or `|`
    // (`pipeDelimited`), and an object is its properties as keys (`style: form`) or as `name[key]` (`deepObject`).
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/profiles": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "name": { "type": "string" },
                          "nickname": { "type": "string" },
                          "count": { "type": "integer", "format": "int32" },
                          "age": { "type": "integer", "format": "int64" },
                          "ratio": { "type": "number", "format": "double" },
                          "active": { "type": "boolean" },
                          "role": { "type": "string", "enum": ["admin", "read-only"] },
                          "status": { "$ref": "#/components/schemas/Status" },
                          "born": { "type": "string", "format": "date" },
                          "seen": { "type": "string", "format": "date-time" },
                          "id": { "type": "string", "format": "uuid" },
                          "code": { "$ref": "#/components/schemas/Code" },
                          "tags": { "type": "array", "items": { "type": "string" } },
                          "roles": { "type": "array", "items": { "type": "string", "enum": ["admin", "read-only"] } },
                          "ids": { "type": "array", "items": { "type": "integer", "format": "int64" } },
                          "words": { "type": "array", "items": { "type": "string" } },
                          "path": { "type": "array", "items": { "type": "string" } },
                          "flags": { "type": "array", "items": { "type": "boolean" } }
                        },
                        "required": ["name", "count", "ratio", "role", "roles"]
                      },
                      "encoding": {
                        "ids": { "style": "form", "explode": false },
                        "words": { "style": "spaceDelimited" },
                        "path": { "style": "pipeDelimited" },
                        "flags": { "explode": true }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/filters": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "filter": {
                            "type": "object",
                            "properties": {
                              "status": { "$ref": "#/components/schemas/Status" },
                              "limit": { "type": "integer", "format": "int32" }
                            },
                            "required": ["status"]
                          },
                          "range": {
                            "type": "object",
                            "properties": {
                              "min": { "type": "integer", "format": "int32" },
                              "max": { "type": "integer", "format": "int32" }
                            },
                            "required": ["min"]
                          }
                        },
                        "required": ["filter"]
                      },
                      "encoding": {
                        "filter": { "style": "form", "explode": true },
                        "range": { "style": "deepObject", "explode": true }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Status": { "type": "string", "enum": ["OPEN", "CLOSED"] },
              "Code": { "type": "string" }
            }
          }
        }
        """.trimIndent(),
        "client/form-urlencoded-fields"
    )

    // An Encoding Object that declares neither `style` nor `explode` is `style: form` (its default) with
    // `explode: true` (the default of `form`): an array field is repeated keys, whatever else the encoding declares.
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/tags": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "tags": { "type": "array", "items": { "type": "string" } },
                          "labels": { "type": "array", "items": { "type": "string" } },
                          "ids": { "type": "array", "items": { "type": "integer", "format": "int64" } }
                        },
                        "required": ["tags"]
                      },
                      "encoding": {
                        "tags": { "contentType": "text/plain" },
                        "labels": { "allowReserved": true },
                        "ids": { "explode": false }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/form-urlencoded-encoding-defaults"
    )

    // Every parameter is serialized by its `style` and `explode` (OpenAPI 3.1.1, 4.8.12.4, RFC 6570), the defaults of
    // its location without them: query and cookie `form`, path and header `simple`, `explode` true for `form` only.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "query", "required": true, "schema": { "type": "array", "items": { "type": "integer", "format": "int64" } } },
                  { "name": "tags", "in": "query", "explode": false, "schema": { "type": "array", "items": { "type": "string" } } },
                  { "name": "words", "in": "query", "style": "spaceDelimited", "schema": { "type": "array", "items": { "type": "string" } } },
                  { "name": "pipes", "in": "query", "style": "pipeDelimited", "schema": { "type": "array", "items": { "type": "string" } } },
                  { "name": "q", "in": "query", "schema": { "type": "string" } },
                  { "name": "when", "in": "query", "schema": { "type": "string", "format": "date-time" } },
                  { "name": "status", "in": "query", "schema": { "$ref": "#/components/schemas/Status" } },
                  { "name": "statuses", "in": "query", "explode": false, "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Status" } } },
                  { "name": "code", "in": "query", "schema": { "$ref": "#/components/schemas/Code" } },
                  { "name": "codes", "in": "query", "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Code" } } },
                  {
                    "name": "filter", "in": "query", "style": "deepObject", "explode": true,
                    "schema": {
                      "type": "object",
                      "properties": { "status": { "$ref": "#/components/schemas/Status" }, "limit": { "type": "integer", "format": "int32" } },
                      "required": ["status"]
                    }
                  },
                  {
                    "name": "range", "in": "query",
                    "schema": {
                      "type": "object",
                      "properties": { "min": { "type": "integer", "format": "int32" }, "max": { "type": "integer", "format": "int32" } },
                      "required": ["min"]
                    }
                  },
                  {
                    "name": "color", "in": "query", "explode": false,
                    "schema": {
                      "type": "object",
                      "properties": { "R": { "type": "integer", "format": "int32" }, "G": { "type": "integer", "format": "int32" } },
                      "required": ["R", "G"]
                    }
                  },
                  { "name": "cwe", "in": "query", "schema": { "oneOf": [{ "type": "string" }, { "type": "array", "items": { "type": "string" } }] } },
                  { "name": "metadata", "in": "query", "schema": { "$ref": "#/components/schemas/Metadata" } },
                  { "name": "labels", "in": "query", "style": "deepObject", "explode": true, "schema": { "type": "object", "additionalProperties": { "type": "string" } } },
                  { "name": "attrs", "in": "query", "explode": false, "schema": { "type": "object", "additionalProperties": { "type": "integer", "format": "int32" } } },
                  { "name": "X-Ids", "in": "header", "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } },
                  { "name": "X-Status", "in": "header", "schema": { "$ref": "#/components/schemas/Status" } },
                  { "name": "X-Meta", "in": "header", "schema": { "type": "object", "additionalProperties": { "type": "string" } } },
                  {
                    "name": "X-Point", "in": "header", "explode": true,
                    "schema": {
                      "type": "object",
                      "properties": { "x": { "type": "integer", "format": "int32" }, "y": { "type": "integer", "format": "int32" } },
                      "required": ["x", "y"]
                    }
                  },
                  { "name": "session", "in": "cookie", "schema": { "type": "string" } },
                  { "name": "prefs", "in": "cookie", "explode": false, "schema": { "type": "array", "items": { "type": "string" } } },
                  { "name": "flags", "in": "cookie", "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Status" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/simple/{ids}": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "path", "required": true, "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Status" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/label/{ids}": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "path", "required": true, "style": "label", "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/label-exploded/{ids}": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "path", "required": true, "style": "label", "explode": true, "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/matrix/{ids}": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "path", "required": true, "style": "matrix", "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/matrix-exploded/{ids}": {
              "get": {
                "parameters": [
                  { "name": "ids", "in": "path", "required": true, "style": "matrix", "explode": true, "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/codes/{code}": {
              "get": {
                "parameters": [
                  { "name": "code", "in": "path", "required": true, "style": "matrix", "schema": { "$ref": "#/components/schemas/Code" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Status": { "type": "string", "enum": ["open", "in-progress"] },
              "Code": { "type": "string" },
              "Metadata": { "anyOf": [{ "type": "object", "additionalProperties": { "type": "string" } }, { "type": "null" }] }
            }
          }
        }
        """.trimIndent(),
        "client/parameter-styles"
    )

    // Path values are percent-encoded as a path segment (OpenAPI 3.1.1, 4.8.12.4; RFC 6570 3.2.1): all but
    // `A-Z a-z 0-9 - . _ ~`, so a `/`, `?`, `#` or `%` in a value stays inside its segment, while the `,`, `.`, `;`
    // and `=` the style writes stay literal.
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/simple/{id}": {
              "get": {
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/label/{id}": {
              "get": {
                "parameters": [{ "name": "id", "in": "path", "required": true, "style": "label", "schema": { "type": "string" } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/matrix/{id}": {
              "get": {
                "parameters": [{ "name": "id", "in": "path", "required": true, "style": "matrix", "schema": { "type": "string" } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/simple-array/{ids}": {
              "get": {
                "parameters": [{ "name": "ids", "in": "path", "required": true, "schema": { "type": "array", "items": { "type": "string" } } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/label-array/{ids}": {
              "get": {
                "parameters": [{ "name": "ids", "in": "path", "required": true, "style": "label", "schema": { "type": "array", "items": { "type": "string" } } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/label-exploded/{ids}": {
              "get": {
                "parameters": [{ "name": "ids", "in": "path", "required": true, "style": "label", "explode": true, "schema": { "type": "array", "items": { "type": "string" } } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/matrix-array/{ids}": {
              "get": {
                "parameters": [{ "name": "ids", "in": "path", "required": true, "style": "matrix", "schema": { "type": "array", "items": { "type": "string" } } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/matrix-exploded/{ids}": {
              "get": {
                "parameters": [{ "name": "ids", "in": "path", "required": true, "style": "matrix", "explode": true, "schema": { "type": "array", "items": { "type": "string" } } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/numbers/{n}": {
              "get": {
                "parameters": [{ "name": "n", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/kinds/{kind}": {
              "get": {
                "parameters": [{ "name": "kind", "in": "path", "required": true, "schema": { "type": "string", "enum": ["plain", "with space"] } }],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/refs/{ref}": {
              "get": {
                "parameters": [
                  {
                    "name": "ref", "in": "path", "required": true,
                    "schema": { "oneOf": [{ "type": "integer", "format": "int32" }, { "type": "string", "enum": ["a/b", "c"] }] }
                  }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/versions/{version}": {
              "get": {
                "parameters": [
                  {
                    "name": "version", "in": "path", "required": true,
                    "schema": { "oneOf": [{ "type": "integer", "format": "int32" }, { "type": "string" }] }
                  }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/path-parameter-encoding"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/owners": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/owners/{id}": {
              "get": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/server-base-path"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "query": { "type": "string" },
                          "filters": {
                            "type": "object",
                            "properties": {
                              "status": { "type": "string" }
                            }
                          }
                        },
                        "required": ["query"]
                      }
                    },
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "query": { "type": "string" },
                          "filters": {
                            "type": "object",
                            "properties": {
                              "status": { "type": "string" }
                            }
                          }
                        },
                        "required": ["query"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-body-form-ambiguous"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/legacy": {
              "get": {
                "deprecated": true,
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-deprecated"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  {
                    "name": "status",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "string",
                      "enum": ["active", "archived", "all"],
                      "default": "all"
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-enum"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/jobs": {
              "get": {
                "parameters": [
                  {
                    "name": "status",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "string",
                      "enum": ["OPEN", "CLOSED"]
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "mode": {
                            "type": "string",
                            "enum": ["FAST", "SLOW"]
                          }
                        },
                        "required": ["mode"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/enum-wire-tostring"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  {
                    "name": "status",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "string",
                      "enum": ["active", "archived", "all"],
                      "default": "all"
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              },
              "post": {
                "parameters": [
                  {
                    "name": "status",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "type": "string",
                      "enum": ["active", "archived", "all"],
                      "default": "all"
                    }
                  }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-shared-parameter"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/advisories": {
              "get": {
                "parameters": [
                  {
                    "name": "ghsa_id",
                    "in": "query",
                    "required": false,
                    "schema": { "type": "string" }
                  },
                  {
                    "name": "cve_id",
                    "in": "query",
                    "required": false,
                    "schema": { "type": "string" }
                  },
                  {
                    "name": "cwes",
                    "in": "query",
                    "required": false,
                    "schema": {
                      "oneOf": [
                        { "type": "string" },
                        {
                          "type": "array",
                          "items": { "type": "string" }
                        }
                      ]
                    }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-inline-oneof-parameter"
    )

    clientTest(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/alerts": {
              "get": {
                "parameters": [
                  { "$ref": "#/components/parameters/has" }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          },
          "components": {
            "parameters": {
              "has": {
                "name": "has",
                "in": "query",
                "required": false,
                "schema": {
                  "oneOf": [
                    { "type": "string" },
                    {
                      "type": "array",
                      "items": {
                        "type": "string",
                        "enum": ["patch"]
                      }
                    }
                  ]
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-oneof-components-parameter"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  {
                    "name": "limit",
                    "in": "query",
                    "required": true,
                    "schema": { "type": "integer", "format": "int32", "default": 20 }
                  },
                  {
                    "name": "offset",
                    "in": "query",
                    "required": false,
                    "schema": { "type": "integer", "format": "int32", "default": 0 }
                  }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/operations-defaults"
    )

    // Phase 10 tests — Response Handling

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-single"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "delete": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-unit"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-multiple"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      },
                      "application/xml": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      },
                      "application/scim+json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "name": { "type": "string" },
                          "tag": { "type": "string" }
                        },
                        "required": ["name"]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int64" }
                      },
                      "text/plain": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods-flattened-body"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "integer", "format": "int64" },
                            "name": { "type": "string" }
                          },
                          "required": ["id", "name"]
                        }
                      },
                      "text/plain": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods-single-status-inline"
    )

    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/Pet" }
                      },
                      "text/plain": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": {
                  "id": { "type": "integer", "format": "int64" },
                  "name": { "type": "string" }
                },
                "required": ["id", "name"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods-single-status-ref"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "integer", "format": "int64" },
                            "name": { "type": "string" }
                          },
                          "required": ["id", "name"]
                        }
                      },
                      "text/plain": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods-single-status-inline-with-error"
    )

    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "${'$'}ref": "#/components/schemas/Pet" }
                      },
                      "application/xml": {
                        "schema": {
                          "type": "object",
                          "properties": { "id": { "type": "integer", "format": "int64" } },
                          "required": ["id"]
                        }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": { "id": { "type": "integer", "format": "int64" } },
                "required": ["id"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-separate-methods-single-status-ref-and-inline"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-default"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets/{petId}": {
              "get": {
                "parameters": [
                  { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "204": {
                    "description": "No Content"
                  },
                  "404": {
                    "description": "Not Found",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-mixed"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-with-body"
    )

    // Phase 11 tests — Ktor Implementation + Server + Factory

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "servers": [
            {
              "url": "https://api.example.com",
              "description": "Production server"
            },
            {
              "url": "https://staging-api.example.com",
              "description": "Staging server"
            }
          ],
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/factory-servers"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "servers": [
            {
              "url": "https://{environment}.api.example.com/{version}",
              "description": "Configurable server",
              "variables": {
                "environment": {
                  "default": "prod",
                  "enum": ["prod", "staging", "dev"]
                },
                "version": {
                  "default": "v1"
                }
              }
            }
          ],
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/factory-server-variables"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "servers": [
            {
              "url": "https://{environment}.api.example.com/{version}",
              "description": "Production server",
              "variables": {
                "environment": {
                  "default": "prod",
                  "enum": ["prod", "staging", "dev"]
                },
                "version": {
                  "default": "v1"
                }
              }
            },
            {
              "url": "https://backup-api.example.com",
              "description": "Backup server"
            }
          ],
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/factory-servers-variable-default"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "parameters": [
                  { "name": "limit", "in": "query", "required": false, "schema": { "type": "integer", "format": "int32" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "type": "array", "items": { "type": "string" } }
                      }
                    }
                  }
                }
              },
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "type": "string" }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/json": {
                        "schema": { "type": "integer", "format": "int32" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/impl-full"
    )

    // Reproducer: inline oneOf requestBody on a nested path
    // Bug 1: type reference uses `UserCodespaces.Post.Body` instead of `User.Codespaces.Post.Body`
    // Bug 2: the `Body` sealed interface is never generated inside `fun interface Post { }`
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/user/codespaces": {
              "post": {
                "operationId": "codespaces/create-for-authenticated-user",
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "oneOf": [
                          {
                            "type": "object",
                            "required": ["repository_id"],
                            "properties": {
                              "repository_id": { "type": "integer" },
                              "ref": { "type": "string" }
                            }
                          },
                          {
                            "type": "object",
                            "required": ["pull_request"],
                            "properties": {
                              "pull_request": {
                                "type": "object",
                                "required": ["pull_request_number", "repository_id"],
                                "properties": {
                                  "pull_request_number": { "type": "integer" },
                                  "repository_id": { "type": "integer" }
                                }
                              }
                            }
                          }
                        ]
                      }
                    }
                  }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": { "type": "string" }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-oneof-request-body-nested-path"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/issues/{issue_number}/labels": {
              "put": {
                "operationId": "issues/set-labels",
                "parameters": [
                  {
                    "name": "owner",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  },
                  {
                    "name": "repo",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  },
                  {
                    "name": "issue_number",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "integer" }
                  }
                ],
                "requestBody": {
                  "required": false,
                  "content": {
                    "application/json": {
                      "schema": {
                        "oneOf": [
                          {
                            "type": "object",
                            "properties": {
                              "labels": {
                                "type": "array",
                                "minItems": 1,
                                "description": "The names of the labels to set for the issue.",
                                "items": {
                                  "type": "string"
                                }
                              }
                            }
                          },
                          {
                            "type": "array",
                            "minItems": 1,
                            "items": {
                              "type": "string"
                            }
                          },
                          {
                            "type": "object",
                            "properties": {
                              "labels": {
                                "type": "array",
                                "minItems": 1,
                                "items": {
                                  "type": "object",
                                  "properties": {
                                    "name": {
                                      "type": "string"
                                    }
                                  },
                                  "required": [
                                    "name"
                                  ]
                                }
                              }
                            }
                          },
                          {
                            "type": "array",
                            "minItems": 1,
                            "items": {
                              "type": "object",
                              "properties": {
                                "name": {
                                  "type": "string"
                                }
                              },
                              "required": [
                                "name"
                              ]
                            }
                          },
                          {
                            "type": "string"
                          }
                        ]
                      }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "Response",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "name": { "type": "string" }
                            },
                            "required": ["name"]
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-oneof-request-body-collection-inline-items"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/content-exclusions": {
              "put": {
                "requestBody": {
                  "description": "The content exclusion rules to set",
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "additionalProperties": {
                          "type": "array",
                          "items": {
                            "anyOf": [
                              {
                                "type": "string",
                                "description": "The path to the file that will be excluded."
                              },
                              {
                                "type": "object",
                                "properties": {
                                  "ifAnyMatch": {
                                    "type": "array",
                                    "items": {
                                      "type": "string"
                                    }
                                  }
                                },
                                "required": [
                                  "ifAnyMatch"
                                ],
                                "additionalProperties": false
                              },
                              {
                                "type": "object",
                                "properties": {
                                  "ifNoneMatch": {
                                    "type": "array",
                                    "items": {
                                      "type": "string"
                                    }
                                  }
                                },
                                "required": [
                                  "ifNoneMatch"
                                ],
                                "additionalProperties": false
                              }
                            ]
                          }
                        }
                      },
                      "examples": {
                        "default": {
                          "summary": "Example of content exclusion paths",
                          "value": {
                            "octo-repo": [
                              "/src/some-dir/kernel.rs"
                            ]
                          }
                        }
                      }
                    }
                  }
                },
                "responses": {
                  "204": {
                    "description": "No Content"
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-anyof-request-body-additional-properties"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/names": {
              "get": {
                "responses": {
                  "200": {
                    "description": "Response",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "name": { "type": "string" }
                            },
                            "required": ["name"]
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-inline-collection-items"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "id": { "type": "string" }
                            },
                            "required": ["id"]
                          }
                        }
                      }
                    }
                  },
                  "201": {
                    "description": "Created",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "name": { "type": "string" }
                            },
                            "required": ["name"]
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-inline-collection-items-multiple-statuses"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "id": { "type": "string" }
                            },
                            "required": ["id"]
                          }
                        }
                      },
                      "application/xml": {
                        "schema": {
                          "type": "array",
                          "items": {
                            "type": "object",
                            "properties": {
                              "name": { "type": "string" }
                            },
                            "required": ["name"]
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-inline-collection-items-separate-methods"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "id": { "type": "string" },
                            "name": { "type": "string" }
                          },
                          "required": ["id", "name"]
                        }
                      }
                    }
                  },
                  "503": {
                    "description": "Service Unavailable",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "message": { "type": "string" },
                            "retryAfter": { "type": "integer", "format": "int32" }
                          },
                          "required": ["message", "retryAfter"]
                        }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "fallbackMessage": { "type": "string" }
                          },
                          "required": ["fallbackMessage"]
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/response-inline-body-collision"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/interaction-limits": {
              "get": {
                "parameters": [
                  {
                    "name": "owner",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  },
                  {
                    "name": "repo",
                    "in": "path",
                    "required": true,
                    "schema": { "type": "string" }
                  }
                ],
                "responses": {
                  "200": {
                    "description": "Response",
                    "content": {
                      "application/json": {
                        "schema": {
                          "anyOf": [
                            {
                              "type": "object",
                              "required": ["limit", "origin"],
                              "properties": {
                                "limit": { "type": "string" },
                                "origin": { "type": "string" }
                              },
                              "additionalProperties": false
                            },
                            {
                              "type": "object",
                              "properties": {},
                              "additionalProperties": false
                            }
                          ]
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-anyof-response-nested-path"
    )

    // Reproducer: union case with inline enum property gets wrong ClassName
    // Bug: path property type is `Body.Source.BranchAndPath.Path` instead of `Source.BranchAndPath.Path`
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/pages": {
              "put": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "source": {
                            "oneOf": [
                              {
                                "type": "string",
                                "enum": ["gh-pages", "master", "master /docs"]
                              },
                              {
                                "type": "object",
                                "properties": {
                                  "branch": { "type": "string" },
                                  "path": {
                                    "type": "string",
                                    "enum": ["/", "/docs"]
                                  }
                                },
                                "required": ["branch", "path"]
                              }
                            ]
                          }
                        }
                      }
                    }
                  }
                },
                "responses": {
                  "204": { "description": "No Content" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/inline-union-case-with-nested-enum"
    )

    // Ktor's ContentNegotiation ignores String bodies, so `body<String>()` / `setBody(String)` bypass the
    // JSON converter. A `type: string` body under a JSON content type must still be a JSON string on the wire.
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/names": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "type": "string" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "type": "string" } } }
                  }
                }
              },
              "put": {
                "requestBody": {
                  "required": true,
                  "content": { "text/plain": { "schema": { "type": "string" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "text/plain": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/claims": {
              "post": {
                "requestBody": {
                  "content": { "application/json": { "schema": { "type": "string" } } }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": { "application/json": { "schema": { "type": ["string", "null"] } } }
                  },
                  "409": {
                    "description": "Conflict",
                    "content": { "application/problem+json": { "schema": { "type": "string" } } }
                  },
                  "5XX": {
                    "description": "Server error",
                    "content": { "application/json": { "schema": { "type": "string" } } }
                  },
                  "default": {
                    "description": "Unexpected",
                    "content": { "application/json": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/drafts": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "type": "string" } },
                    "text/plain": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags": {
              "put": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "oneOf": [
                          { "type": "string" },
                          { "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }
                        ]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": { "schema": { "type": "string" } },
                      "text/plain": { "schema": { "type": "string" } }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/json-string-bodies"
    )

    // A request body declared only under a structured-syntax `+json` media type (RFC 6839) is JSON: it is sent
    // through the client's JSON converter, with the declared media type as `Content-Type`.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/claims": {
              "patch": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/merge-patch+json": { "schema": { "$ref": "#/components/schemas/ClaimPatch" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/documents": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/vnd.api+json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "title": { "type": "string" },
                          "draft": { "type": "boolean" }
                        },
                        "required": ["title"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/operations": {
              "patch": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json-patch+json": {
                      "schema": { "type": "array", "items": { "$ref": "#/components/schemas/PatchOperation" } }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "put": {
                "requestBody": {
                  "content": {
                    "application/merge-patch+json": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "ClaimPatch": {
                "type": "object",
                "properties": {
                  "status": { "type": "string" },
                  "note": { "type": "string" }
                }
              },
              "PatchOperation": {
                "type": "object",
                "properties": {
                  "op": { "type": "string" },
                  "path": { "type": "string" },
                  "value": { "type": "string" }
                },
                "required": ["op", "path"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/json-suffix-request-bodies"
    )

    // A request body declared only under a media type without a dedicated rendering is still sent with its
    // declared media type, instead of being dropped from the operation.
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/reports": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "text/csv": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/avatars": {
              "put": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "image/png": { "schema": { "type": "string", "format": "binary" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/feeds": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/atom+xml": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/non-json-request-bodies"
    )

    // A wildcard media type is a range the server accepts, not a `Content-Type`: a request body declared under
    // `*/*`, `application/*` or `text/*` is sent with the concrete media type in that range the client produces for
    // its model (JSON for structured values, `text/plain` for text, `application/octet-stream` for bytes), and a
    // string response declared under a range that includes JSON is decoded by the `Content-Type` the server sends.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/objects": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "*/*": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/texts": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "*/*": { "schema": { "type": "string" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/files": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "*/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/app-objects": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/*": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/app-texts": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/*": { "schema": { "type": "string" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/app-files": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "text/*": { "schema": { "type": "string" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/labels": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "*/*": { "schema": { "type": "string" } },
                    "application/*": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/greeting": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "*/*": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/app-greeting": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/*": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/text-greeting": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "text/*": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/animal": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "*/*": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" },
                  "tag": { "type": "string" }
                },
                "required": ["name"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/wildcard-media-types"
    )

    // Media type parameters declared in the spec are part of the media type the server expects and sends: a request
    // body goes out with its declared `charset` (encoded in it) and other parameters such as JSON:API's `ext`, the
    // `Accept` header asks for the declared media type with its parameters, and a response is matched on its media
    // type whether or not the server labels it with the declared `charset`. A `multipart/form-data` `boundary` is
    // the transport's, never copied from the spec.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/json; charset=utf-8": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                },
                "responses": {
                  "201": {
                    "description": "Created",
                    "content": { "application/json; charset=utf-8": { "schema": { "$ref": "#/components/schemas/Pet" } } }
                  }
                }
              }
            },
            "/notes": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "text/plain; charset=iso-8859-1": { "schema": { "type": "string" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "text/plain; charset=iso-8859-1": { "schema": { "type": "string" } } }
                  }
                }
              }
            },
            "/latin-notes": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "text/*; charset=iso-8859-1": { "schema": { "type": "string" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/operations": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/vnd.api+json; ext=\"https://jsonapi.org/ext/atomic\"": {
                      "schema": { "$ref": "#/components/schemas/Pet" }
                    }
                  }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/vnd.api+json; ext=\"https://jsonapi.org/ext/atomic\"": {
                        "schema": { "$ref": "#/components/schemas/Pet" }
                      }
                    }
                  }
                }
              }
            },
            "/labels": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "text/plain; charset=utf-8": { "schema": { "type": "string" } },
                    "text/csv; charset=iso-8859-1": { "schema": { "type": "string" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/uploads": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data; boundary=spec-boundary": {
                      "schema": {
                        "type": "object",
                        "properties": { "title": { "type": "string" } },
                        "required": ["title"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/reports": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json; charset=utf-8": { "schema": { "$ref": "#/components/schemas/Pet" } },
                      "text/csv; charset=utf-8": { "schema": { "type": "string" } }
                    }
                  },
                  "400": {
                    "description": "Bad Request",
                    "content": {
                      "application/problem+json; charset=utf-8": { "schema": { "$ref": "#/components/schemas/Problem" } },
                      "text/plain; charset=utf-8": { "schema": { "type": "string" } }
                    }
                  },
                  "4XX": {
                    "description": "Client Error",
                    "content": {
                      "application/json; charset=utf-8": { "schema": { "$ref": "#/components/schemas/Problem" } },
                      "text/plain; charset=utf-8": { "schema": { "type": "string" } }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" },
                  "tag": { "type": "string" }
                },
                "required": ["name"]
              },
              "Problem": {
                "type": "object",
                "properties": { "title": { "type": "string" } },
                "required": ["title"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/media-type-parameters"
    )

    // A binary body declared under a media type range that admits neither `application/octet-stream` nor
    // `text/plain` (`image/*`, `video/*`, `font/*`) is sent verbatim with the concrete media type the caller passes:
    // only the caller knows whether its bytes are a PNG or a JPEG. The supplied media type is checked against the
    // declared range(s) before the request is sent. Binary bodies under several such ranges share one overload, and
    // the argument is renamed when an operation parameter already takes the name `contentType`.
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/avatars": {
              "put": {
                "requestBody": {
                  "required": true,
                  "content": { "image/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/thumbnails": {
              "post": {
                "parameters": [ { "name": "size", "in": "query", "schema": { "type": "integer" } } ],
                "requestBody": {
                  "content": { "image/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/media": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "image/*": { "schema": { "type": "string", "format": "binary" } },
                    "video/*": { "schema": { "type": "string", "format": "binary" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/documents": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "$ref": "#/components/schemas/Pet" } },
                    "image/*": { "schema": { "type": "string", "format": "binary" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/scans": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/octet-stream": { "schema": { "type": "string", "format": "binary" } },
                    "application/pdf": { "schema": { "type": "string", "format": "binary" } },
                    "image/*": { "schema": { "type": "string", "format": "binary" } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/fonts": {
              "post": {
                "parameters": [
                  { "name": "contentType", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": { "font/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" },
                  "tag": { "type": "string" }
                },
                "required": ["name"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/media-type-range-uploads"
    )

    fun wildcardBodyApi(vararg content: Pair<String, String>): String = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/images": {
              "put": {
                "requestBody": {
                  "required": true,
                  "content": {
                    ${content.joinToString(",\n") { [type, schema] -> "\"$type\": { \"schema\": $schema }" }}
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
    """.trimIndent()

    val wildcardConfig = RenderConfig(
        modelPackage = "io.github.nomisrev.render.test.client.wildcard.unsendable.model",
        apiPackage = "io.github.nomisrev.render.test.client.wildcard.unsendable.api",
        targets = setOf(KmpTarget.JVM),
    )
    val objectSchema = """{ "type": "object", "properties": { "name": { "type": "string" } } }"""

    test("an object body under a wildcard media type no producible media type matches fails generation") {
        val error = assertFailsWith<IllegalArgumentException> {
            OpenAPI.fromJson(wildcardBodyApi("image/*" to objectSchema)).toApiTree().generateClient(wildcardConfig)
        }
        assertTrue(error.message.orEmpty().contains("PUT /images (image/*)"), error.message)
    }

    test("a binary body under a wildcard media type no producible media type matches takes the caller's media type") {
        val result = OpenAPI.fromJson(
            wildcardBodyApi("image/*" to """{ "type": "string", "format": "binary" }""")
        ).toApiTree().generateClientWithDiagnostics(wildcardConfig)

        assertTrue(result.diagnostics.isEmpty(), result.diagnostics.toString())
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("invoke(body: ByteArray, contentType: ContentType)" in code, code)
        assertTrue("contentType(contentType)" in code, code)
    }

    test("a multipart boundary declared in the spec is never rendered") {
        val schema = """{ "type": "object", "properties": { "title": { "type": "string" } }, "required": ["title"] }"""
        val code = OpenAPI.fromJson(
            wildcardBodyApi(
                "multipart/form-data; boundary=spec-boundary" to schema,
                "application/x-www-form-urlencoded" to schema,
            )
        ).toApiTree().generateClient(wildcardConfig).joinToString("\n") { it.toString() }
        assertTrue("RequestType" in code, code)
        assertTrue("spec-boundary" !in code, code)
    }

    test("a body under an unsendable wildcard media type is skipped with a warning when another body exists") {
        val result = OpenAPI.fromJson(
            wildcardBodyApi("image/*" to objectSchema, "application/json" to objectSchema)
        ).toApiTree().generateClientWithDiagnostics(wildcardConfig)

        val diagnostic = result.diagnostics.single()
        assertEquals(GenerationDiagnosticSeverity.Warning, diagnostic.severity)
        assertTrue(diagnostic.message.contains("PUT /images (image/*)"), diagnostic.message)
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("contentType(ContentType.Application.Json)" in code, code)
        assertTrue("\"image\"" !in code, code)
    }

    // Ktor's form encoder percent-encodes the UTF-8 bytes of a value, so a form declared in another charset would go
    // out labelled with that charset but encoded in UTF-8, and the server would decode every non-ASCII character
    // wrongly.
    val formSchema = """{ "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }"""
    test("a form body declared in a charset other than UTF-8 fails generation when it is the only body") {
        val error = assertFailsWith<IllegalArgumentException> {
            OpenAPI.fromJson(
                wildcardBodyApi("application/x-www-form-urlencoded; charset=iso-8859-1" to formSchema)
            ).toApiTree().generateClient(wildcardConfig)
        }
        val message = error.message.orEmpty()
        assertTrue(message.contains("PUT /images (application/x-www-form-urlencoded; charset=iso-8859-1)"), message)
        assertTrue(message.contains("UTF-8"), message)
    }

    test("a form body declared in a charset other than UTF-8 is skipped with a warning when another body exists") {
        val result = OpenAPI.fromJson(
            wildcardBodyApi(
                "application/x-www-form-urlencoded; charset=iso-8859-1" to formSchema,
                "application/json" to formSchema,
            )
        ).toApiTree().generateClientWithDiagnostics(wildcardConfig)

        val diagnostic = result.diagnostics.single()
        assertEquals(GenerationDiagnosticSeverity.Warning, diagnostic.severity)
        assertTrue(diagnostic.message.contains("charset=iso-8859-1"), diagnostic.message)
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("contentType(ContentType.Application.Json)" in code, code)
        assertTrue("formUrlEncode" !in code, code)
    }

    // A form field is generated only when its type and Encoding Object `style` / `explode` have a serialization the
    // client writes (`client/form-urlencoded-fields`); any other field fails generation as the only body, rather
    // than generating code that does not compile or sends a body the server reads differently.
    fun formFieldApi(property: String, encoding: String?): String = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/forms": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": { "type": "object", "properties": { "field": $property } }
                      ${encoding?.let { ""","encoding": { "field": $it }""" }.orEmpty()}
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
    """.trimIndent()

    val scalarObject = """{ "type": "object", "properties": { "a": { "type": "string" } } }"""
    val stringArray = """{ "type": "array", "items": { "type": "string" } }"""
    val unsupportedFormFields = listOf(
        // `deepObject` defines no array serialization.
        "an array with deepObject" to formFieldApi(stringArray, """{ "style": "deepObject", "explode": true }"""),
        "an array with a style forms do not define" to formFieldApi(stringArray, """{ "style": "matrix" }"""),
        "an array of objects" to formFieldApi("""{ "type": "array", "items": $scalarObject }""", null),
        "an array of nullable items" to
            formFieldApi("""{ "type": "array", "items": { "type": ["string", "null"] } }""", null),
        // Without `style` or `explode`, an object field is its `contentType`, `application/json`.
        "an object without encoding" to formFieldApi(scalarObject, null),
        "an object with form and explode false" to formFieldApi(scalarObject, """{ "style": "form", "explode": false }"""),
        "an object with spaceDelimited" to formFieldApi(scalarObject, """{ "style": "spaceDelimited" }"""),
        "an object with a non-scalar property" to formFieldApi(
            """{ "type": "object", "properties": { "a": $stringArray } }""",
            """{ "style": "deepObject", "explode": true }""",
        ),
        "a map" to formFieldApi(
            """{ "type": "object", "additionalProperties": { "type": "string" } }""",
            """{ "style": "deepObject", "explode": true }""",
        ),
    )
    for ([description, json] in unsupportedFormFields) {
        test("a form field of $description has no supported form serialization") {
            val error = assertFailsWith<IllegalArgumentException> {
                OpenAPI.fromJson(json).toApiTree().generateClient(wildcardConfig)
            }
            val message = error.message.orEmpty()
            assertTrue(
                message.contains("POST /forms (application/x-www-form-urlencoded) contains form fields [field]"),
                message,
            )
        }
    }

    // A parameter is generated only when the client writes the serialization its type, location, `style` and
    // `explode` define (`client/parameter-styles`); any other parameter fails generation, rather than sending the
    // `toString()` of its Kotlin value or a layout the server reads differently.
    fun parameterApi(location: String, schema: String, extra: String = ""): String {
        val path = if (location == "path") "/items/{p}" else "/items"
        return """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "$path": {
              "get": {
                "parameters": [{ "name": "p", "in": "$location", "required": true, "schema": $schema$extra }],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent()
    }

    val intArray = """{ "type": "array", "items": { "type": "integer" } }"""
    val unsupportedParameters = listOf(
        "a query array with deepObject" to
            parameterApi("query", intArray, """, "style": "deepObject", "explode": true"""),
        "a query array with spaceDelimited and explode true" to
            parameterApi("query", intArray, """, "style": "spaceDelimited", "explode": true"""),
        "a query array with simple, a style queries do not define" to
            parameterApi("query", intArray, """, "style": "simple""""),
        "a header array with form, a style headers do not define" to
            parameterApi("header", intArray, """, "style": "form""""),
        "a query array of objects" to parameterApi("query", """{ "type": "array", "items": $scalarObject }"""),
        "a query map of objects" to
            parameterApi("query", """{ "type": "object", "additionalProperties": $scalarObject }"""),
        "a path object" to parameterApi("path", scalarObject),
    )
    for ([description, json] in unsupportedParameters) {
        test("a parameter of $description has no supported serialization") {
            val result = OpenAPI.fromJson(json).toApiTree().generateClientWithDiagnostics(wildcardConfig)

            val diagnostic = result.diagnostics.single()
            assertEquals(GenerationDiagnosticSeverity.Error, diagnostic.severity)
            assertTrue(diagnostic.message.startsWith("GET /items"), diagnostic.message)
            assertTrue(diagnostic.message.contains("parameter p"), diagnostic.message)
            assertTrue(diagnostic.message.contains("no supported serialization"), diagnostic.message)
        }
    }

    // A required parameter whose schema admits `null` takes a nullable argument, which is left out when `null` like an
    // optional one, instead of being percent-encoded or joined without a null check.
    test("a required query parameter whose schema admits null is left out when null") {
        val result = OpenAPI.fromJson(parameterApi("query", """{ "type": ["string", "null"] }"""))
            .toApiTree().generateClientWithDiagnostics(wildcardConfig)

        assertTrue(result.diagnostics.isEmpty(), result.diagnostics.toString())
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("p: String?" in code, code)
        assertTrue("if (p != null) {" in code, code)
    }

    // An Encoding Object without `style` or `explode` is `style: form` with `explode: true`, its defaults, so an array
    // field is repeated keys (`client/form-urlencoded-encoding-defaults`), not rejected as if it were `explode: false`.
    test("a form array field whose encoding declares neither style nor explode is generated as repeated keys") {
        val result = OpenAPI.fromJson(formFieldApi(stringArray, """{ "contentType": "text/plain" }"""))
            .toApiTree().generateClientWithDiagnostics(wildcardConfig)

        assertTrue(result.diagnostics.isEmpty(), result.diagnostics.toString())
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("""appendAll("field", field)""" in code, code)
    }

    test("a form body declared in UTF-8 under any spelling of the charset is generated") {
        val result = OpenAPI.fromJson(
            wildcardBodyApi("application/x-www-form-urlencoded; charset=UTF-8" to formSchema)
        ).toApiTree().generateClientWithDiagnostics(wildcardConfig)

        assertTrue(result.diagnostics.isEmpty(), result.diagnostics.toString())
        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue(
            """contentType(ContentType.Application.FormUrlEncoded.withParameter("charset", "UTF-8"))""" in code,
            code,
        )
    }

    test("ambiguous form overload emits warning and is omitted when json body is available") {
        val json = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "query": { "type": "string" },
                          "filters": {
                            "type": "object",
                            "properties": {
                              "status": { "type": "string" }
                            }
                          }
                        },
                        "required": ["query"]
                      }
                    },
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "query": { "type": "string" },
                          "filters": {
                            "type": "object",
                            "properties": {
                              "status": { "type": "string" }
                            }
                          }
                        },
                        "required": ["query"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent()

        val result = OpenAPI.fromJson(json)
            .toApiTree()
            .generateClientWithDiagnostics(
                RenderConfig(
                    modelPackage = "io.github.nomisrev.render.test.client.operations.body.form.ambiguous.model",
                    apiPackage = "io.github.nomisrev.render.test.client.operations.body.form.ambiguous.api",
                    targets = setOf(KmpTarget.JVM),
                )
            )

        assertEquals(1, result.diagnostics.size)
        assertEquals(GenerationDiagnosticSeverity.Warning, result.diagnostics.single().severity)
        assertTrue(result.diagnostics.single().message.contains("Skipping unsupported form overload"))
    }

    test("ambiguous form-only request body fails generation") {
        val json = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "query": { "type": "string" },
                          "filters": {
                            "type": "object",
                            "properties": {
                              "status": { "type": "string" }
                            }
                          }
                        },
                        "required": ["query"]
                      }
                    }
                  }
                },
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent()

        val error = assertFailsWith<IllegalArgumentException> {
            OpenAPI.fromJson(json)
                .toApiTree()
                .generateClient(
                    RenderConfig(
                        modelPackage = "io.github.nomisrev.render.test.client.operations.body.form.ambiguous.model",
                        apiPackage = "io.github.nomisrev.render.test.client.operations.body.form.ambiguous.api",
                        targets = setOf(KmpTarget.JVM),
                    )
                )
        }

        assertTrue(error.message.orEmpty().contains("form fields [filters] that have no supported form serialization"))
    }

    test("flattened request body overload limit is reported as an error") {
        val json = flattenedBodyOverloadLimitJson(requiredUnionFieldCount = 6)

        val result = OpenAPI.fromJson(json)
            .toApiTree()
            .generateClientWithDiagnostics(
                RenderConfig(
                    modelPackage = "io.github.nomisrev.render.test.client.flattened.body.limit.model",
                    apiPackage = "io.github.nomisrev.render.test.client.flattened.body.limit.api",
                    targets = setOf(KmpTarget.JVM),
                )
            )

        assertEquals(1, result.diagnostics.size)
        assertEquals(GenerationDiagnosticSeverity.Error, result.diagnostics.single().severity)
        assertTrue(result.diagnostics.single().message.contains("flattened request-body overloads"))
        assertTrue(result.diagnostics.single().message.contains("32"))
    }

    test("flattened request body overload limit fails generation") {
        val json = flattenedBodyOverloadLimitJson(requiredUnionFieldCount = 6)

        val error = assertFailsWith<IllegalArgumentException> {
            OpenAPI.fromJson(json)
                .toApiTree()
                .generateClient(
                    RenderConfig(
                        modelPackage = "io.github.nomisrev.render.test.client.flattened.body.limit.model",
                        apiPackage = "io.github.nomisrev.render.test.client.flattened.body.limit.api",
                        targets = setOf(KmpTarget.JVM),
                    )
                )
        }

        assertTrue(error.message.orEmpty().contains("flattened request-body overloads"))
        assertTrue(error.message.orEmpty().contains("32"))
    }
}

private fun flattenedBodyOverloadLimitJson(requiredUnionFieldCount: Int): String {
    val propertyEntries = (1..requiredUnionFieldCount).joinToString(",\n") { index ->
        """
        "field$index": {
          "oneOf": [
            { "type": "string" },
            { "type": "integer", "format": "int32" }
          ]
        }
        """.trimIndent()
    }
    val requiredEntries = (1..requiredUnionFieldCount).joinToString(", ") { "\"field$it\"" }
    return """
    {
      "openapi": "3.1.0",
      "info": { "title": "Api", "version": "0.0.1" },
      "paths": {
        "/search": {
          "post": {
            "requestBody": {
              "required": true,
              "content": {
                "application/json": {
                  "schema": {
                    "type": "object",
                    "properties": {
                      $propertyEntries
                    },
                    "required": [$requiredEntries]
                  }
                }
              }
            },
            "responses": { "200": { "description": "OK" } }
          }
        }
      }
    }
    """.trimIndent()
}
