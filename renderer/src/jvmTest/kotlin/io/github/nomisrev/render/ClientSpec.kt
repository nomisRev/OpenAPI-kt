package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertTrue

val clientSpec by testSuite {
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
}
