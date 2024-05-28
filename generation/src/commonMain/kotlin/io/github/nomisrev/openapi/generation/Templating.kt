package io.github.nomisrev.openapi.generation

/**
 * Small DSL for two functionalities:
 *  - automatic indenting
 *  - being able to add/inject imports
 */
public fun template(
  indent: String = "  ",
  init: Templating.() -> Unit
): Content =
  Template(0, StringBuilder(), indent, mutableSetOf())
    .also(init)
    .build()

@DslMarker
public annotation class TemplatingDSL

public data class Content(val imports: Set<String>, val code: String)

public interface Templating {
  @TemplatingDSL
  public fun append(line: String)

  @TemplatingDSL
  public fun addImports(vararg imports: String): Boolean

  @TemplatingDSL
  public fun indented(init: Templating.() -> Unit)

  @TemplatingDSL
  public fun <T> Collection<T>.indented(
    index: (Int) -> Int = Int::inc,
    separator: String = "\n",
    prefix: String = "",
    postfix: String = "\n",
    transform: (Templating.(T) -> Unit)? = null
  )

  @TemplatingDSL
  public operator fun String.unaryPlus(): Unit = line(this)

  @TemplatingDSL
  public fun addImport(import: String): Boolean = addImports(import)

  @TemplatingDSL
  public fun line(line: String): Unit = append("$line\n")

  @TemplatingDSL
  public fun line(): Unit = append("\n")

  @TemplatingDSL
  public fun <T> Collection<T>.joinTo(
    separator: String = "\n",
    prefix: String = "",
    postfix: String = "\n",
    transform: (Templating.(T) -> Unit)? = null
  ) {
    indented(index = { it }, separator, prefix, postfix, transform)
  }

  @TemplatingDSL
  public fun expression(
    text: String,
    init: Templating.() -> Unit
  ): Unit = block(text, false, init)

  @TemplatingDSL
  public fun block(
    text: String,
    closeAfter: Boolean = true,
    block: Templating.() -> Unit
  ) {
    line(text)
    indented(block)
    if (closeAfter) +"}"
  }
}

@TemplatingDSL
private class Template(
  private val index: Int,
  private val content: StringBuilder,
  private val indentConfig: String,
  private val imports: MutableSet<String>
) : Templating {
  private val indent: String = indentConfig.repeat(index)

  public override operator fun String.unaryPlus(): Unit =
    line(this)

  @TemplatingDSL
  public override fun addImport(import: String): Boolean =
    imports.add(import)

  @TemplatingDSL
  public override fun addImports(vararg imports: String): Boolean =
    this.imports.addAll(imports)

  @TemplatingDSL
  public override fun append(line: String) {
    content.append("$indent$line")
  }

  @TemplatingDSL
  public override fun <T> Collection<T>.indented(
    index: (Int) -> Int,
    separator: String,
    prefix: String,
    postfix: String,
    transform: (Templating.(T) -> Unit)?
  ) {
    val template = Template(index(this@Template.index), content, indentConfig, imports)
    fun transform(element: T) = transform?.invoke(template, element) ?: element
    if (isNotEmpty()) template.content.append(prefix)
    firstOrNull()?.let { head ->
      transform(head)
      drop(1).forEach {
        template.content.append(separator)
        transform(it)
      }
      if (isNotEmpty()) template.content.append(postfix)
    }
  }

  @TemplatingDSL
  public override fun indented(init: Templating.() -> Unit) {
    Template(index + 1, content, indentConfig, imports).init()
  }

  fun build(): Content =
    Content(imports, content.toString())
}
