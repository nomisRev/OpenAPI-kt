package io.github.nomisrev.openapi.test

public fun template(
  indent: String = "  ",
  init: Template.() -> Unit
): String =
  Template(0, StringBuilder(), indent, mutableSetOf())
    .also(init)
    .toString()

public interface IndentedSyntax {
  public operator fun String.unaryPlus()
  public fun append(line: String)
}

public class Template(
  private val index: Int,
  private val content: StringBuilder,
  private val indentConfig: String,
  private val imports: MutableSet<String>
) : IndentedSyntax {
  private val indent: String = indentConfig.repeat(index)

  public override operator fun String.unaryPlus(): Unit =
    line(this)

  public fun addImport(import: String): Boolean =
    imports.add(import)

  public fun addImports(vararg imports: String): Boolean =
    this.imports.addAll(imports)

  public override fun append(line: String) {
    content.append("$indent$line")
  }

  public fun line(line: String) {
    content.append("$indent$line\n")
  }

  public fun line() {
    content.append("\n")
  }

  public fun <T> Iterable<T>.joinTo(
    separator: String = "\n",
    prefix: String = "",
    postfix: String = "\n",
    transform: (IndentedSyntax.(T) -> Unit)? = null
  ) {
    indented(index = { it }, separator, prefix, postfix, transform)
  }

  public fun <T> Iterable<T>.indented(
    index: (Int) -> Int = Int::inc,
    separator: String = "\n",
    prefix: String = "",
    postfix: String = "\n",
    transform: (IndentedSyntax.(T) -> Unit)? = null
  ) {
    val template = Template(index(this@Template.index), content, indentConfig, imports)
    fun transform(element: T) = transform?.invoke(template, element) ?: element
    template.content.append(prefix)
    firstOrNull()?.let { head ->
      transform(head)
      drop(1).forEach {
        template.content.append(separator)
        transform(it)
      }
      template.content.append(postfix)
    }
  }

  public fun indented(init: Template.() -> Unit) {
    Template(index + 1, content, indentConfig, imports).init()
  }

  public fun expression(
    text: String,
    init: Template.() -> Unit
  ): Unit = block(text, false, init)

  public fun block(
    text: String,
    closeAfter: Boolean = true,
    init: Template.() -> Unit
  ) {
    line(text)
    Template(index + 1, content, indentConfig, imports).init()
    if (closeAfter) +"}"
  }

  override fun toString(): String {
//    content.insert(0, "package test")
    content.insert(
      0,
      imports.joinToString(
        separator = "\n",
        postfix = "\n\n"
      ) { "import $it" }
    )
    return content.toString()
  }
}