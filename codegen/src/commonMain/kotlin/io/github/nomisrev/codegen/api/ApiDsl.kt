package io.github.nomisrev.codegen.api

import io.github.nomisrev.codegen.ir.KtClass
import io.github.nomisrev.codegen.ir.KtClassKind
import io.github.nomisrev.codegen.ir.KtDeclaration
import io.github.nomisrev.codegen.ir.KtExpr
import io.github.nomisrev.codegen.ir.KtFile
import io.github.nomisrev.codegen.ir.KtFunction
import io.github.nomisrev.codegen.ir.KtModifier
import io.github.nomisrev.codegen.ir.KtParam
import io.github.nomisrev.codegen.ir.KtType

/**
 * Minimal fluent DSL to construct API interfaces and operations on top of the IR. This mirrors the
 * legacy generation pattern (interfaces with suspend functions) while keeping emitters unchanged.
 */

// Entry point: build a file
fun KFile(name: String, pkg: String? = null, block: KtFileBuilder.() -> Unit): KtFile =
  KtFileBuilder(name, pkg).apply(block).build()

class KtFileBuilder(private val name: String, private val pkg: String?) {
  private val declarations = mutableListOf<KtDeclaration>()

  fun apiInterface(name: String, block: InterfaceBuilder.() -> Unit) {
    val ib = InterfaceBuilder(name)
    ib.block()
    declarations += ib.build()
  }

  fun add(decl: KtDeclaration) {
    declarations += decl
  }

  fun build(): KtFile = KtFile(name = name, pkg = pkg, declarations = declarations.toList())
}

class InterfaceBuilder(private val name: String) {
  private val functions = mutableListOf<KtFunction>()

  fun suspendFun(name: String, block: FunBuilder.() -> Unit) {
    val fb = FunBuilder(name)
    fb.block()
    functions += fb.build(suspendFun = true)
  }

  fun funDecl(name: String, block: FunBuilder.() -> Unit) {
    val fb = FunBuilder(name)
    fb.block()
    functions += fb.build(suspendFun = false)
  }

  fun build(): KtClass =
    KtClass(name = name, kind = KtClassKind.Interface, functions = functions.toList())
}

class FunBuilder(private val name: String) {
  private val params = mutableListOf<KtParam>()
  private var returnType: KtType? = null

  fun param(name: String, type: KtType, default: String? = null) {
    params += KtParam(name = name, type = type, default = default?.let { KtExpr(it) })
  }

  fun returns(type: KtType) {
    this.returnType = type
  }

  fun build(suspendFun: Boolean): KtFunction =
    KtFunction(
      name = name,
      params = params.toList(),
      returnType = returnType,
      modifiers = if (suspendFun) listOf(KtModifier.Suspend) else emptyList(),
    )
}

// Type helpers
fun simple(qName: String, nullable: Boolean = false): KtType.Simple = KtType.Simple(qName, nullable)

fun generic(
  rawQualifiedName: String,
  vararg args: KtType,
  nullable: Boolean = false,
): KtType.Generic =
  KtType.Generic(raw = KtType.Simple(rawQualifiedName), args = args.toList(), nullable = nullable)
