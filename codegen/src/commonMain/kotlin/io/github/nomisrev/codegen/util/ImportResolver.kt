package io.github.nomisrev.codegen.util

import io.github.nomisrev.codegen.ir.*

internal data class ImportContext(
  val currentPackage: String?,
  val imported: List<String>, // fully qualified names that are imported
) {
  fun renderSimpleOrFqn(qName: String): String {
    val simple = qName.substringAfterLast('.')
    // Special-case kotlin.jvm.* annotations and types: they are NOT default-imported in common
    // source sets. Use simple name only if explicitly imported; otherwise render FQN.
    if (qName.startsWith("kotlin.jvm.")) {
      return if (qName in imported) simple else qName
    }
    return if (
      qName in imported || currentPackage != null && qName.startsWith(currentPackage + ".")
    ) {
      simple
    } else if ('.' !in qName) {
      qName
    } else if (qName.startsWith("kotlin.")) {
      // For other kotlin.* packages (kotlin., kotlin.collections., etc.) use simple names,
      // relying on Kotlin's default imports.
      simple
    } else {
      qName
    }
  }
}

internal object ImportResolver {
  fun compute(file: KtFile): ImportContext {
    val pkg = file.pkg

    // Collect all qualified names from types found in the file
    val qualifiedNames = mutableListOf<String>()

    fun addType(t: KtType) {
      when (t) {
        is KtType.Simple -> {
          if ('.' in t.qualifiedName) qualifiedNames += t.qualifiedName
        }
        is KtType.Generic -> {
          addType(t.raw)
          t.args.forEach(::addType)
        }
        is KtType.Function -> {
          t.params.forEach(::addType)
          addType(t.returnType)
        }
      }
    }

    fun fromDecl(d: KtDeclaration) {
      when (d) {
        is KtTypeAlias -> addType(d.type)
        is KtClass -> {
          d.typeParams.forEach { tp -> tp.bounds.forEach(::addType) }
          d.primaryCtor?.params?.forEach { p ->
            addType(p.type)
            // collect annotations on constructor parameters
            p.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
          }
          d.properties.forEach { p -> addType(p.type) }
          d.functions.forEach { f ->
            f.params.forEach { p ->
              addType(p.type)
              // collect annotations on function parameters
              p.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
            }
            f.returnType?.let { addType(it) }
          }
          d.superTypes.forEach(::addType)
          d.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
          // Include annotations used on enum entries for import resolution
          d.enumEntries.forEach { e ->
            e.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
          }
        }
        is KtFunction -> {
          d.params.forEach { p ->
            addType(p.type)
            p.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
          }
          d.returnType?.let { addType(it) }
          d.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
        }
        is KtProperty -> {
          addType(d.type)
          d.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
        }
        is KtEnumEntry -> {
          d.annotations.forEach { a -> qualifiedNames += a.name.qualifiedName }
        }
      }
    }

    file.declarations.forEach(::fromDecl)

    // Count by simple name
    val bySimple = qualifiedNames.groupBy({ it.substringAfterLast('.') }, { it })

    // Determine which qualified names we can import
    val toImport = mutableSetOf<String>()

    for ((_, qns) in bySimple) {
      val unique = qns.distinct()
      if (unique.size == 1) {
        val qn = unique.first()
        // Skip same package and most kotlin.* default imports, but allow kotlin.jvm.*
        val isSamePackage = pkg != null && qn.startsWith("$pkg.")
        val isKotlinJvm = qn.startsWith("kotlin.jvm.")
        val isOtherKotlin = qn.startsWith("kotlin.") && !isKotlinJvm
        if (!isSamePackage && !isOtherKotlin) {
          toImport += qn
        }
      }
      // if multiple, prefer FQN (no import)
    }

    // Add any explicit imports from file (and let sorting/dedup handle the rest)
    val explicit = file.imports.map { it.qualifiedName }
    val finalImports = (toImport + explicit).distinct().sortedDescending()

    return ImportContext(currentPackage = pkg, imported = finalImports)
  }
}
