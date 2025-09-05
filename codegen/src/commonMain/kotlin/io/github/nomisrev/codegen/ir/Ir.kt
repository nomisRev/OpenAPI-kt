package io.github.nomisrev.codegen.ir

// Kotlin Declaration IR for M1

data class KtFile(
  val name: String,
  val pkg: String? = null,
  val imports: List<KtImport> = emptyList(),
  val declarations: List<KtDeclaration> = emptyList(),
  val kdoc: KtKDoc? = null,
)

data class KtImport(val qualifiedName: String, val alias: String? = null)

sealed interface KtDeclaration

data class KtTypeAlias(
  val name: String,
  val type: KtType,
  val annotations: List<KtAnnotation> = emptyList(),
  val kdoc: KtKDoc? = null,
  val modifiers: List<KtModifier> = emptyList(),
) : KtDeclaration

enum class KtClassKind {
  Class,
  Interface,
  Object,
  Enum,
}

enum class KtVisibility {
  Public,
  Internal,
  Private,
  Protected,
}

enum class KtModifier {
  Data,
  Inline,
  Value,
  Sealed,
  Open,
  Final,
  Abstract,
  Suspend,
  Override,
  Lateinit,
  Const,
  Companion,
}

data class KtClass(
  val name: String,
  val kind: KtClassKind,
  val visibility: KtVisibility = KtVisibility.Public,
  val modifiers: List<KtModifier> = emptyList(),
  val typeParams: List<KtTypeParam> = emptyList(),
  val primaryCtor: KtPrimaryConstructor? = null,
  val enumEntries: List<KtEnumEntry> = emptyList(),
  val properties: List<KtProperty> = emptyList(),
  val functions: List<KtFunction> = emptyList(),
  val companion: KtClass? = null,
  val superTypes: List<KtType> = emptyList(),
  val annotations: List<KtAnnotation> = emptyList(),
  val kdoc: KtKDoc? = null,
) : KtDeclaration

data class KtFunction(
  val name: String,
  val params: List<KtParam>,
  val returnType: KtType?,
  val typeParams: List<KtTypeParam> = emptyList(),
  val modifiers: List<KtModifier> = emptyList(),
  val annotations: List<KtAnnotation> = emptyList(),
  val kdoc: KtKDoc? = null,
  val body: KtBlock? = null,
) : KtDeclaration

data class KtProperty(
  val name: String,
  val type: KtType,
  val mutable: Boolean = false,
  val initializer: KtExpr? = null,
  val modifiers: List<KtModifier> = emptyList(),
  val annotations: List<KtAnnotation> = emptyList(),
  val kdoc: KtKDoc? = null,
) : KtDeclaration

data class KtEnumEntry(
  val name: String,
  val args: List<KtExpr> = emptyList(),
  val annotations: List<KtAnnotation> = emptyList(),
  val kdoc: KtKDoc? = null,
) : KtDeclaration

sealed interface KtType {
  val nullable: Boolean

  data class Simple(val qualifiedName: String, override val nullable: Boolean = false) : KtType

  data class Generic(
    val raw: Simple,
    val args: List<KtType>,
    override val nullable: Boolean = false,
  ) : KtType

  data class Function(
    val params: List<KtType>,
    val returnType: KtType,
    override val nullable: Boolean = false,
  ) : KtType
}

data class KtParam(
  val name: String,
  val type: KtType,
  val default: KtExpr? = null,
  val annotations: List<KtAnnotation> = emptyList(),
  val asProperty: Boolean = false,
  val mutable: Boolean = false,
)

data class KtTypeParam(
  val name: String,
  val bounds: List<KtType> = emptyList(),
  val variance: KtVariance? = null,
  val reified: Boolean = false,
)

enum class KtVariance {
  In,
  Out,
}

/** Primary constructor for classes (used in data classes, etc.). */
data class KtPrimaryConstructor(
  val params: List<KtParam>,
  val visibility: KtVisibility = KtVisibility.Public,
)

data class KtAnnotation(val name: KtType.Simple, val args: Map<String?, KtExpr> = emptyMap())

data class KtKDoc(val lines: List<String>)

data class KtExpr(val text: String) {
  override fun toString(): String = text
}

data class KtBlock(val text: String) {
  override fun toString(): String = text
}
