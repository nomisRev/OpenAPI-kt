package io.openai.model

import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * An array of tools the model may call while generating a response. You
 * can specify which tool to use by setting the `tool_choice` parameter.
 *
 * We support the following categories of tools:
 * - **Built-in tools**: Tools that are provided by OpenAI that extend the
 *   model's capabilities, like [web search](/docs/guides/tools-web-search)
 *   or [file search](/docs/guides/tools-file-search). Learn more about
 *   [built-in tools](/docs/guides/tools).
 * - **MCP Tools**: Integrations with third-party systems via custom MCP servers
 *   or predefined connectors such as Google Drive and SharePoint. Learn more about
 *   [MCP Tools](/docs/guides/tools-connectors-mcp).
 * - **Function calls (custom tools)**: Functions that are defined by you,
 *   enabling the model to call your own code with strongly typed arguments
 *   and outputs. Learn more about
 *   [function calling](/docs/guides/function-calling). You can also use
 *   custom tools to call your own code.
 *
 */
@JvmInline
@Serializable
public value class ToolsArray(
  public val items: List<Tool>,
)
